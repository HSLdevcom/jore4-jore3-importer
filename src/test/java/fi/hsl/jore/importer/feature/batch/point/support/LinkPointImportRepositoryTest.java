package fi.hsl.jore.importer.feature.batch.point.support;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeRepository;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkPointImportRepositoryTest extends IntegrationTest {

    private static final LineString LINE_1 = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0),
            new Coordinate(60.168896355, 24.945549266, 0)
    );

    private static final LineString LINEPOINTS_1 = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0),
            new Coordinate(60.168990620, 24.946028727, 0),
            new Coordinate(60.168896355, 24.945549266, 0)
    );

    private static final LineString LINE_2 = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.158988620, 24.939328727, 0),
            new Coordinate(60.158896355, 24.935549266, 0)
    );

    private static final LineString LINEPOINTS_2 = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.158988620, 24.939328727, 0),
            new Coordinate(60.158908620, 24.937328727, 0),
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

    private static final Point POINT_3 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0)
    );

    private static final Point POINT_4 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.158988620, 24.939328727, 0)
    );

    private final ILinkPointImportRepository importRepository;
    private final ILinkRepository targetRepository;
    private final INodeRepository nodeRepository;

    public LinkPointImportRepositoryTest(@Autowired final ILinkPointImportRepository importRepository,
                                         @Autowired final ILinkRepository targetRepository,
                                         @Autowired final INodeRepository nodeRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
        this.nodeRepository = nodeRepository;
    }

    @Test
    public void whenNoStagedRowsAndCommit_thenReturnEmptyResult() {
        assertThat(importRepository.commitStagingToTarget(),
                   is(HashMap.empty()));
    }

    @Test
    public void whenStagedRowsAndCommit_andTargetLinkHasNoPoints_thenReturnResultWithUpdatedId() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        final List<NodePK> nodeIds = nodeRepository.upsert(
                List.of(
                        PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                        PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2),
                        PersistableNode.of(ExternalId.of("4"), NodeType.CROSSROADS, POINT_3),
                        PersistableNode.of(ExternalId.of("5"), NodeType.CROSSROADS, POINT_4)
                )
        );

        // Insert the real links (without linkpoints)
        final List<LinkPK> inserted = targetRepository.upsert(
                List.of(PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, nodeIds.get(0), nodeIds.get(1)),
                        PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, LINE_2, nodeIds.get(2), nodeIds.get(3)))
        );
        final LinkPK targetLink = inserted.get(0);

        importRepository.submitToStaging(
                List.of(LinkGeometry.of(ExternalId.of("a"), NetworkType.ROAD, LINEPOINTS_1))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("Only UPDATEs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.UPDATED)));

        final Set<LinkPK> ids = result.get(RowStatus.UPDATED).get();

        assertThat("Update should target the original link",
                   ids,
                   is(HashSet.of(targetLink)));

        final LinkPK id = ids.get();

        assertThat("Target repository should contain both links",
                   targetRepository.findAllIds(),
                   is(HashSet.ofAll(inserted)));

        final Link link = targetRepository.findById(id).orElseThrow();

        assertThat("Target link should have the original geometry",
                   link.geometry(),
                   is(LINE_1));
        assertThat("Target link should have line points",
                   link.points().isPresent(),
                   is(true));
        assertThat("Target link should have the correct line points",
                   link.points().get(),
                   is(LINEPOINTS_1));
    }

    @Test
    public void whenStagedRowsAndCommit_andTargetLinkHasPoints_thenReturnResultWithUpdatedId() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        final List<NodePK> nodeIds = nodeRepository.upsert(
                List.of(
                        PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                        PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2),
                        PersistableNode.of(ExternalId.of("4"), NodeType.CROSSROADS, POINT_3),
                        PersistableNode.of(ExternalId.of("5"), NodeType.CROSSROADS, POINT_4)
                )
        );

        // Insert the real links (without linkpoints)
        final List<LinkPK> inserted = targetRepository.upsert(
                List.of(PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, nodeIds.get(0), nodeIds.get(1)),
                        PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, LINE_2, nodeIds.get(2), nodeIds.get(3)))
        );
        final LinkPK targetLink = inserted.get(0);

        // Assign some points to the first link directly
        targetRepository.updateLinkPoints(
                List.of(LinkGeometry.of(ExternalId.of("a"), NetworkType.ROAD, LINEPOINTS_1))
        );

        // Stage some new points for the same link
        importRepository.submitToStaging(
                List.of(LinkGeometry.of(ExternalId.of("a"), NetworkType.ROAD, LINEPOINTS_2))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("Only UPDATEs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.UPDATED)));

        final Set<LinkPK> ids = result.get(RowStatus.UPDATED).get();

        assertThat("Update should target the original link",
                   ids,
                   is(HashSet.of(targetLink)));

        final LinkPK id = ids.get();

        assertThat("Target repository should contain both links",
                   targetRepository.findAllIds(),
                   is(HashSet.ofAll(inserted)));

        final Link link = targetRepository.findById(id).orElseThrow();

        assertThat("Target link should have the original geometry",
                   link.geometry(),
                   is(LINE_1));
        assertThat("Target link should have line points",
                   link.points().isPresent(),
                   is(true));
        assertThat("Target link should have the updated line points",
                   link.points().get(),
                   is(LINEPOINTS_2));
    }
}
