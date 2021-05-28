package fi.hsl.jore.importer.feature.batch.point.support;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.TestGeometryUtil;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.ImmutableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkTestRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeTestRepository;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import static fi.hsl.jore.importer.TestGeometryUtil.geometriesMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkPointImportRepositoryTest extends IntegrationTest {

    private static final LineString LINE_1 = TestGeometryUtil.randomLine();
    private static final LineString LINEPOINTS_1 = TestGeometryUtil.randomLine(5);
    private static final LineString LINE_2 = TestGeometryUtil.randomLine();
    private static final LineString LINEPOINTS_2 = TestGeometryUtil.randomLine(5);
    private static final Point POINT_1 = TestGeometryUtil.randomPoint();
    private static final Point POINT_2 = TestGeometryUtil.randomPoint();
    private static final Point POINT_3 = TestGeometryUtil.randomPoint();
    private static final Point POINT_4 = TestGeometryUtil.randomPoint();

    private final ILinkPointImportRepository importRepository;
    private final ILinkTestRepository targetRepository;
    private final INodeTestRepository nodeRepository;

    public LinkPointImportRepositoryTest(@Autowired final ILinkPointImportRepository importRepository,
                                         @Autowired final ILinkTestRepository targetRepository,
                                         @Autowired final INodeTestRepository nodeRepository) {
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

        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2),
                PersistableNode.of(ExternalId.of("3"), NodeType.CROSSROADS, POINT_3),
                PersistableNode.of(ExternalId.of("4"), NodeType.CROSSROADS, POINT_4)
        );

        // Insert the real links (without linkpoints)
        final List<LinkPK> inserted = targetRepository.insert(
                PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, nodeIds.get(0), nodeIds.get(1)),
                PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, LINE_2, nodeIds.get(2), nodeIds.get(3))
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
                   geometriesMatch(link.geometry(), LINE_1),
                   is(true));
        assertThat("Target link should have line points",
                   link.points().isPresent(),
                   is(true));
        assertThat("Target link should have the correct line points",
                   geometriesMatch(link.points().get(), LINEPOINTS_1),
                   is(true));
    }

    @Test
    public void whenStagedRowsAndCommit_andTargetLinkHasPoints_thenReturnResultWithUpdatedId() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2),
                PersistableNode.of(ExternalId.of("3"), NodeType.CROSSROADS, POINT_3),
                PersistableNode.of(ExternalId.of("4"), NodeType.CROSSROADS, POINT_4)
        );

        // Insert the real links (without linkpoints)
        final List<LinkPK> inserted = targetRepository.insert(
                PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, nodeIds.get(0), nodeIds.get(1)),
                PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, LINE_2, nodeIds.get(2), nodeIds.get(3))
        );
        final LinkPK targetLink = inserted.get(0);

        // Assign some points to the first link directly
        targetRepository.update(
                ImmutableLink.copyOf(targetRepository.findById(targetLink).orElseThrow())
                             .withPoints(LINEPOINTS_1)
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
                   geometriesMatch(link.geometry(), LINE_1),
                   is(true));
        assertThat("Target link should have line points",
                   link.points().isPresent(),
                   is(true));
        assertThat("Target link should have the updated line points",
                   geometriesMatch(link.points().get(), LINEPOINTS_2),
                   is(true));
    }
}
