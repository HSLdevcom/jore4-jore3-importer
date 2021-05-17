package fi.hsl.jore.importer.feature.infrastructure.link.repository;

import fi.hsl.jore.importer.IntegrationTest;
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
import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkRepositoryTest extends IntegrationTest {

    private final ILinkTestRepository linkRepository;
    private final INodeTestRepository nodeRepository;

    private static final LineString GEOM = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0),
            new Coordinate(60.168896355, 24.945549266, 0)
    );

    private static final LineString GEOM2 = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.158988620, 24.939328727, 0),
            new Coordinate(60.158896355, 24.935549266, 0)
    );

    private static final Point POINT_1 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0)
    );

    private static final Point POINT_2 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.158988620, 24.939328727, 0)
    );

    public LinkRepositoryTest(@Autowired final ILinkTestRepository linkRepository,
                              @Autowired final INodeTestRepository nodeRepository) {
        this.linkRepository = linkRepository;
        this.nodeRepository = nodeRepository;
    }

    @BeforeEach
    public void beforeEach() {
        assertThat("Node repository should be empty at the start of the test",
                   nodeRepository.empty(),
                   is(true));
        assertThat("Link repository should be empty at the start of the test",
                   linkRepository.empty(),
                   is(true));
    }

    @Test
    public void insertSingleLink() {
        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2)
        );
        final NodePK startNode = nodeIds.get(0);
        final NodePK endNode = nodeIds.get(1);

        final PersistableLink link = PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, GEOM, startNode, endNode);

        assertThat(linkRepository.empty(),
                   is(true));

        final LinkPK linkId = linkRepository.insert(link);

        final Link linkFromDb = linkRepository.findById(linkId).orElseThrow();

        assertThat(linkFromDb.geometry(),
                   is(GEOM));
        assertThat(linkFromDb.geometry().getSRID(),
                   is(GEOM.getSRID()));
        assertThat(linkFromDb.geometry().getSRID(),
                   is(GeometryUtil.SRID_WGS84));
        assertThat(linkFromDb.startNode(),
                   is(startNode));
        assertThat(linkFromDb.endNode(),
                   is(endNode));
    }

    @Test
    public void insertMultipleLinks() {
        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2)
        );
        final NodePK startNode = nodeIds.get(0);
        final NodePK endNode = nodeIds.get(1);

        final List<LinkPK> keys = linkRepository.insert(
                PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, GEOM, startNode, endNode),
                PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, GEOM2, startNode, endNode)
        );

        final List<Link> linksFromDb = linkRepository.findAll();

        assertThat(linksFromDb.map(IHasPK::pk).toSet(),
                   is(keys.toSet()));

        for (final Link link : linksFromDb) {
            assertThat(link.geometry().getSRID(),
                       is(GeometryUtil.SRID_WGS84));
            assertThat(link.startNode(),
                       is(startNode));
            assertThat(link.endNode(),
                       is(endNode));
        }

        assertThat(linksFromDb.get(0).geometry(),
                   is(GEOM));
        assertThat(linksFromDb.get(1).geometry(),
                   is(GEOM2));
    }
}
