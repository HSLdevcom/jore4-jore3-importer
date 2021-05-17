package fi.hsl.jore.importer.feature.batch.link.support;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.ImportableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkTestRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeTestRepository;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkImportRepositoryTest extends IntegrationTest {

    private static final LineString LINE_1 = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0),
            new Coordinate(60.168896355, 24.945549266, 0)
    );

    private static final LineString LINE_2 = GeometryUtil.toLineString(
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

    private static final Point POINT_3 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.128988620, 24.239328727, 0)
    );

    private static final Point POINT_4 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.138988620, 24.339328727, 0)
    );

    private final ILinkImportRepository importRepository;
    private final ILinkTestRepository targetRepository;
    private final INodeTestRepository nodeRepository;

    public LinkImportRepositoryTest(@Autowired final ILinkImportRepository importRepository,
                                    @Autowired final ILinkTestRepository targetRepository,
                                    @Autowired final INodeTestRepository nodeRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
        this.nodeRepository = nodeRepository;
    }

    @BeforeEach
    public void beforeEach() {
        assertThat("Target repository should be empty at the start of the test",
                   targetRepository.empty(),
                   is(true));
    }

    @Test
    public void whenNoStagedRowsAndCommit_thenReturnEmptyResult() {
        assertThat(importRepository.commitStagingToTarget(),
                   is(HashMap.empty()));
    }

    @Test
    public void whenNewStagedRowsAndCommit_andTargetDbContainsNodes_thenReturnResultWithInsertedIdAndNodeRefs() {
        final NodePK firstNodeId = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1)
        );
        final NodePK secondNodeId = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2)
        );

        importRepository.submitToStaging(
                List.of(ImportableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, ExternalId.of("1"), ExternalId.of("2")))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("Only INSERTs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.INSERTED)));

        final Set<LinkPK> ids = result.get(RowStatus.INSERTED).get();

        assertThat("Only a single row is inserted",
                   ids.size(),
                   is(1));

        assertThat("Target repository should contain only the inserted row",
                   targetRepository.findAllIds(),
                   is(ids));

        final LinkPK id = ids.get();

        final Link link = targetRepository.findById(id).orElseThrow();

        assertThat(link.startNode(),
                   is(firstNodeId));

        assertThat(link.endNode(),
                   is(secondNodeId));
    }

    @Test
    public void whenStagedRowsWithChangesAndCommit_andTargetNotEmpty_thenReturnResultWithUpdatedId() {
        final NodePK startNode = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1)
        );
        final NodePK endNode = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2)
        );

        final LinkPK existingId = targetRepository.insert(
                PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, startNode, endNode)
        );

        assertThat("Target repository should now contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        importRepository.submitToStaging(
                List.of(ImportableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_2, ExternalId.of("1"), ExternalId.of("2")))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("Only UPDATEs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.UPDATED)));

        final Set<LinkPK> ids = result.get(RowStatus.UPDATED).get();

        assertThat("Only a single row is updated",
                   ids,
                   is(HashSet.of(existingId)));

        assertThat("Target repository should still contain a single row",
                   targetRepository.findAllIds(),
                   is(ids));

        final Link link = targetRepository.findById(existingId).orElseThrow();

        assertThat("The updated rows location was changed",
                   link.geometry(),
                   is(LINE_2));
        assertThat(link.startNode(),
                   is(startNode));
        assertThat(link.endNode(),
                   is(endNode));
    }

    @Test
    public void whenStagedRowsWithNoChangesAndCommit_andTargetNotEmpty_thenReturnEmptyResult() {
        final NodePK startNode = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1)
        );
        final NodePK endNode = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2)
        );
        final PersistableLink sourceLink = PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, startNode, endNode);

        final LinkPK existingId = targetRepository.insert(
                sourceLink
        );

        assertThat("Target repository should now contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        // We submit the same link
        importRepository.submitToStaging(
                List.of(ImportableLink.of(sourceLink.externalId(),
                                          sourceLink.networkType(),
                                          sourceLink.geometry(),
                                          ExternalId.of("1"),
                                          ExternalId.of("2")))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("No operations should occur",
                   result.keySet(),
                   is(HashSet.empty()));

        assertThat("Target repository should still contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        final Link link = targetRepository.findById(existingId).orElseThrow();

        assertThat("The target row was not changed",
                   link.geometry(),
                   is(LINE_1));
        assertThat(link.startNode(),
                   is(startNode));
        assertThat(link.endNode(),
                   is(endNode));
    }

    @Test
    public void whenStagedRowsAndCommit_andTargetContainsExtraRows_thenReturnResultWithDeletedId() {
        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2),
                PersistableNode.of(ExternalId.of("3"), NodeType.CROSSROADS, POINT_3),
                PersistableNode.of(ExternalId.of("4"), NodeType.CROSSROADS, POINT_4)
        );
        // Insert two links into the target table (as if imported previously)
        final PersistableLink firstLink = PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, nodeIds.get(0), nodeIds.get(1));
        final PersistableLink secondLink = PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, LINE_2, nodeIds.get(2), nodeIds.get(3));

        final LinkPK firstId = targetRepository.insert(firstLink);
        final LinkPK secondId = targetRepository.insert(secondLink);

        // We submit only the latter link as-is, simulating the case where the first link is removed at the source
        importRepository.submitToStaging(
                List.of(ImportableLink.of(secondLink.externalId(),
                                          secondLink.networkType(),
                                          secondLink.geometry(),
                                          ExternalId.of("3"),
                                          ExternalId.of("4")))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("Only DELETEs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.DELETED)));

        final Set<LinkPK> ids = result.get(RowStatus.DELETED).get();

        assertThat("Only a single row is deleted",
                   ids,
                   is(HashSet.of(firstId)));

        assertThat("Target repository should contain the second link",
                   targetRepository.findAllIds(),
                   is(HashSet.of(secondId)));
    }
}
