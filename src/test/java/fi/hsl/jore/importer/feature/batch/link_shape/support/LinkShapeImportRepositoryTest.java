package fi.hsl.jore.importer.feature.batch.link_shape.support;

import static fi.hsl.jore.importer.TestGeometryUtil.geometriesMatch;
import static fi.hsl.jore.importer.util.JoreCollectionUtils.getFirst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.TestGeometryUtil;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkTestRepository;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.Jore3LinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.LinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.PersistableLinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.generated.LinkShapePK;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.repository.ILinkShapeTestRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeTestRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkShapeImportRepositoryTest extends IntegrationTest {

    private static final LineString LINE_1 = TestGeometryUtil.randomLine();
    private static final LineString LINEPOINTS_1 = TestGeometryUtil.randomLine(5);
    private static final LineString LINE_2 = TestGeometryUtil.randomLine();
    private static final LineString LINEPOINTS_2 = TestGeometryUtil.randomLine(5);
    private static final Point POINT_1 = TestGeometryUtil.randomPoint();
    private static final Point POINT_2 = TestGeometryUtil.randomPoint();
    private static final Point POINT_3 = TestGeometryUtil.randomPoint();
    private static final Point POINT_4 = TestGeometryUtil.randomPoint();

    private final ILinkShapeImportRepository importRepository;
    private final ILinkShapeTestRepository targetRepository;
    private final ILinkTestRepository linkRepository;
    private final INodeTestRepository nodeRepository;

    public LinkShapeImportRepositoryTest(
            @Autowired final ILinkShapeImportRepository importRepository,
            @Autowired final ILinkShapeTestRepository targetRepository,
            @Autowired final ILinkTestRepository linkRepository,
            @Autowired final INodeTestRepository nodeRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
        this.linkRepository = linkRepository;
        this.nodeRepository = nodeRepository;
    }

    @BeforeEach
    public void beforeEach() {
        assertThat("Target repository should be empty at the start of the test", targetRepository.empty(), is(true));
    }

    @Test
    public void whenNoStagedRowsAndCommit_thenReturnEmptyResult() {
        assertThat(importRepository.commitStagingToTarget(), is(Collections.emptyMap()));
    }

    @Test
    public void whenStagedRowsAndCommit_andTargetLinkHasNoPoints_thenReturnResultWithUpdatedShape() {
        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2),
                PersistableNode.of(ExternalId.of("3"), NodeType.CROSSROADS, POINT_3),
                PersistableNode.of(ExternalId.of("4"), NodeType.CROSSROADS, POINT_4));

        // Insert the real links (without linkpoints)
        final List<LinkPK> inserted = linkRepository.insert(
                PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, nodeIds.get(0), nodeIds.get(1)),
                PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, LINE_2, nodeIds.get(2), nodeIds.get(3)));
        final LinkPK parentLinkPk = inserted.get(0);
        final Link parentLink = linkRepository.findById(parentLinkPk).orElseThrow();

        importRepository.submitToStaging(List.of(Jore3LinkShape.of(parentLink.externalId(), LINEPOINTS_1)));

        final Map<RowStatus, Set<LinkShapePK>> result = importRepository.commitStagingToTarget();

        assertThat("Only INSERTs should occur", result.keySet(), is(Set.of(RowStatus.INSERTED)));

        final Set<LinkShapePK> ids = result.get(RowStatus.INSERTED);

        assertThat("Only a single shape is inserted", ids.size(), is(1));

        final LinkShape shape = targetRepository.findById(getFirst(ids)).orElseThrow();

        assertThat(
                "Target link should have the original geometry",
                geometriesMatch(shape.geometry(), LINEPOINTS_1),
                is(true));
    }

    @Test
    public void whenStagedRowsAndCommit_andTargetLinkHasPoints_thenReturnResultWithUpdatedShape() {
        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(ExternalId.of("1"), NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(ExternalId.of("2"), NodeType.CROSSROADS, POINT_2),
                PersistableNode.of(ExternalId.of("3"), NodeType.CROSSROADS, POINT_3),
                PersistableNode.of(ExternalId.of("4"), NodeType.CROSSROADS, POINT_4));

        // Insert the real links (without linkpoints)
        final List<LinkPK> inserted = linkRepository.insert(
                PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, LINE_1, nodeIds.get(0), nodeIds.get(1)),
                PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, LINE_2, nodeIds.get(2), nodeIds.get(3)));
        final LinkPK parentLinkPk = inserted.get(0);
        final Link parentLink = linkRepository.findById(parentLinkPk).orElseThrow();

        // Assign a shape to the first link directly

        targetRepository.insert(PersistableLinkShape.of(parentLink.pk(), parentLink.externalId(), LINEPOINTS_1));

        // Stage some new points for the same link
        importRepository.submitToStaging(List.of(Jore3LinkShape.of(parentLink.externalId(), LINEPOINTS_2)));

        final Map<RowStatus, Set<LinkShapePK>> result = importRepository.commitStagingToTarget();

        assertThat("Only UPDATEs should occur", result.keySet(), is(Set.of(RowStatus.UPDATED)));

        final Set<LinkShapePK> ids = result.get(RowStatus.UPDATED);

        assertThat("Only a single shape is updated", ids.size(), is(1));

        final LinkShapePK shapeId = getFirst(ids);
        final LinkShape updatedShape = targetRepository.findById(shapeId).orElseThrow();

        assertThat(
                "Target shape should have the updated line points",
                geometriesMatch(updatedShape.geometry(), LINEPOINTS_2),
                is(true));
    }
}
