package fi.hsl.jore.importer.feature.infrastructure.node.repository;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.TestGeometryUtil;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.ImmutableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import static fi.hsl.jore.importer.TestGeometryUtil.geometriesMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class NodeRepositoryTest extends IntegrationTest {

    private final INodeTestRepository nodeRepository;

    private static final Point GEOM = TestGeometryUtil.randomPoint();
    private static final Point GEOM2 = TestGeometryUtil.randomPoint();

    public NodeRepositoryTest(@Autowired final INodeTestRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @BeforeEach
    public void beforeEach() {
        assertThat("Node repository should be empty at the start of the test",
                   nodeRepository.empty(),
                   is(true));
    }

    @Test
    public void insertMultipleNodes() {
        final List<NodePK> keys = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, GEOM),
                PersistableNode.of(ExternalId.of("b"), NodeType.CROSSROADS, GEOM2)
        );

        final List<Node> nodesFromDb = nodeRepository.findAll();

        assertThat(geometriesMatch(nodesFromDb.get(0).location(), GEOM),
                   is(true));
        assertThat(geometriesMatch(nodesFromDb.get(1).location(), GEOM2),
                   is(true));
        assertThat(GEOM.getSRID(),
                   is(GeometryUtil.SRID_WGS84));
        assertThat(GEOM.getSRID(),
                   is(nodesFromDb.get(0).location().getSRID()));
        assertThat(nodesFromDb.map(IHasPK::pk).toSet(),
                   is(keys.toSet()));
    }

    @Test
    public void updateNodeWithNewGeometry() {
        final ExternalId extId = ExternalId.of("a");
        final PersistableNode nodeToInsert = PersistableNode.of(extId, NodeType.CROSSROADS, GEOM);
        final NodePK id = nodeRepository.insert(nodeToInsert);
        final Node insertedNode = nodeRepository.findById(id).orElseThrow();

        final List<Node> nodesFromDb = nodeRepository.findAll();

        assertThat("Repository should contain a single node",
                   nodesFromDb.size(),
                   is(1));

        final Node originalNode = nodesFromDb.get(0);

        assertThat("Node should have the initial geometry",
                   geometriesMatch(GEOM, originalNode.location()),
                   is(true));
        assertThat("Insert and find() should return the same keys",
                   nodesFromDb.map(IHasPK::pk).toSet(),
                   is(HashSet.of(id)));

        final Set<NodePK> keys2 = HashSet.of(
                nodeRepository.update(ImmutableNode.copyOf(insertedNode)
                                                   .withLocation(GEOM2))
        );

        final List<Node> nodesFromDb2 = nodeRepository.findAll();

        assertThat("Repository should contain a single node",
                   nodesFromDb.size(),
                   is(1));

        final Node updatedNode = nodesFromDb2.get(0);

        assertThat("Node geometry should have been updated",
                   geometriesMatch(GEOM2, nodesFromDb2.get(0).location()),
                   is(true));
        assertThat("Update and find() should return the same keys",
                   nodesFromDb2.map(IHasPK::pk).toSet(),
                   is(keys2));
        assertThat("Node id should not change",
                   HashSet.of(id),
                   is(keys2));
        // Updated node should be identical to the original node..
        assertThat(updatedNode.pk(),
                   is(originalNode.pk()));
        assertThat(updatedNode.externalId(),
                   is(originalNode.externalId()));
        // .. except for the geometry and the system time
        assertThat(updatedNode.location(),
                   is(not(originalNode.location())));
        assertThat(updatedNode.systemTime(),
                   is(not(originalNode.systemTime())));
    }

    @Test
    public void updateNodeWithSameGeometry() {
        final ExternalId extId = ExternalId.of("a");
        final PersistableNode nodeToInsert = PersistableNode.of(extId, NodeType.CROSSROADS, GEOM);

        final NodePK id = nodeRepository.insert(nodeToInsert);
        final Node insertedNode = nodeRepository.findById(id).orElseThrow();

        final List<Node> nodesFromDb = nodeRepository.findAll();

        assertThat("Repository should contain a single node",
                   nodesFromDb.size(),
                   is(1));

        final Node originalNode = nodesFromDb.get(0);

        assertThat("Node should have the initial geometry",
                   geometriesMatch(GEOM, originalNode.location()),
                   is(true));
        assertThat("Insert and find() should return the same keys",
                   nodesFromDb.map(IHasPK::pk).toSet(),
                   is(HashSet.of(id)));

        final Set<NodePK> keys2 = HashSet.of(
                nodeRepository.update(insertedNode)
        );

        final List<Node> nodesFromDb2 = nodeRepository.findAll();

        assertThat("Repository should contain a single node",
                   nodesFromDb.size(),
                   is(1));

        final Node updatedNode = nodesFromDb2.get(0);

        assertThat("Update and find() should return the same keys",
                   nodesFromDb2.map(IHasPK::pk).toSet(),
                   is(keys2));
        assertThat("Node id should not change",
                   HashSet.of(id),
                   is(keys2));
        assertThat("Updated node should be identical to the original node",
                   updatedNode,
                   is(originalNode));
    }
}
