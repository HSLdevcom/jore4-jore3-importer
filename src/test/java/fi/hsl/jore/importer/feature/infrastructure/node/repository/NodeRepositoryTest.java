package fi.hsl.jore.importer.feature.infrastructure.node.repository;

import fi.hsl.jore.importer.IntegrationTest;
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
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class NodeRepositoryTest extends IntegrationTest {

    private final INodeTestRepository nodeRepository;

    private static final Point GEOM = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0)
    );

    private static final Point GEOM2 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.158988620, 24.939328727, 0)
    );

    public NodeRepositoryTest(@Autowired final INodeTestRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @Test
    public void insertMultipleNodes() {
        assertThat(nodeRepository.empty(),
                   is(true));

        final List<NodePK> keys = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, GEOM),
                PersistableNode.of(ExternalId.of("b"), NodeType.CROSSROADS, GEOM2)
        );

        final List<Node> nodesFromDb = nodeRepository.findAll();

        assertThat(GEOM,
                   is(nodesFromDb.get(0).location()));
        assertThat(GEOM2,
                   is(nodesFromDb.get(1).location()));
        assertThat(GEOM.getSRID(),
                   is(GeometryUtil.SRID_WGS84));
        assertThat(GEOM.getSRID(),
                   is(nodesFromDb.get(0).location().getSRID()));
        assertThat(nodesFromDb.map(IHasPK::pk).toSet(),
                   is(keys.toSet()));
    }

    @Test
    public void updateNodeWithNewGeometry() {
        assertThat("repository should be empty in the beginning",
                   nodeRepository.empty(),
                   is(true));

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
                   GEOM,
                   is(originalNode.location()));
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
                   GEOM2,
                   is(nodesFromDb2.get(0).location()));
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
        assertThat("repository should be empty in the beginning",
                   nodeRepository.empty(),
                   is(true));

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
                   GEOM,
                   is(originalNode.location()));
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
