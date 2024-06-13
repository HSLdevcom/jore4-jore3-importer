package fi.hsl.jore.importer.feature.infrastructure.link.repository;

import static fi.hsl.jore.importer.TestGeometryUtil.geometriesMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.TestGeometryUtil;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeTestRepository;
import fi.hsl.jore.importer.util.GeometryUtil;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkRepositoryTest extends IntegrationTest {

    private final ILinkTestRepository linkRepository;
    private final INodeTestRepository nodeRepository;

    private static final LineString GEOM = TestGeometryUtil.randomLine();
    private static final LineString GEOM2 = TestGeometryUtil.randomLine();
    private static final Point POINT_1 = TestGeometryUtil.randomPoint();
    private static final Point POINT_2 = TestGeometryUtil.randomPoint();

    public LinkRepositoryTest(
            @Autowired final ILinkTestRepository linkRepository, @Autowired final INodeTestRepository nodeRepository) {
        this.linkRepository = linkRepository;
        this.nodeRepository = nodeRepository;
    }

    @BeforeEach
    public void beforeEach() {
        assertThat("Node repository should be empty at the start of the test", nodeRepository.empty(), is(true));
        assertThat("Link repository should be empty at the start of the test", linkRepository.empty(), is(true));
    }

    @Test
    public void insertSingleLink() {
        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2));
        final NodePK startNode = nodeIds.get(0);
        final NodePK endNode = nodeIds.get(1);

        final PersistableLink link = PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, GEOM, startNode, endNode);

        assertThat(linkRepository.empty(), is(true));

        final LinkPK linkId = linkRepository.insert(link);

        final Link linkFromDb = linkRepository.findById(linkId).orElseThrow();

        assertThat(geometriesMatch(linkFromDb.geometry(), GEOM), is(true));
        assertThat(linkFromDb.geometry().getSRID(), is(GEOM.getSRID()));
        assertThat(linkFromDb.geometry().getSRID(), is(GeometryUtil.SRID_WGS84));
        assertThat(linkFromDb.startNode(), is(startNode));
        assertThat(linkFromDb.endNode(), is(endNode));
    }

    @Test
    public void insertMultipleLinks() {
        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2));
        final NodePK startNode = nodeIds.get(0);
        final NodePK endNode = nodeIds.get(1);

        final List<LinkPK> keys = linkRepository.insert(
                PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, GEOM, startNode, endNode),
                PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, GEOM2, startNode, endNode));

        final List<Link> linksFromDb = linkRepository.findAll();

        assertThat(linksFromDb.stream().map(IHasPK::pk).collect(Collectors.toSet()), is(Set.copyOf(keys)));

        for (final Link link : linksFromDb) {
            assertThat(link.geometry().getSRID(), is(GeometryUtil.SRID_WGS84));
            assertThat(link.startNode(), is(startNode));
            assertThat(link.endNode(), is(endNode));
        }

        assertThat(geometriesMatch(linksFromDb.get(0).geometry(), GEOM), is(true));
        assertThat(geometriesMatch(linksFromDb.get(1).geometry(), GEOM2), is(true));
    }
}
