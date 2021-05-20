package fi.hsl.jore.importer.feature.batch.link.support;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.TestGeometryUtil;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
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
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static fi.hsl.jore.importer.TestGeometryUtil.geometriesMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkImportRepositoryTest extends IntegrationTest {

    private static final LineString LINE_1 = TestGeometryUtil.randomLine();
    private static final LineString LINE_2 = TestGeometryUtil.randomLine();
    private static final Point POINT_1 = TestGeometryUtil.randomPoint();
    private static final Point POINT_2 = TestGeometryUtil.randomPoint();
    private static final Point POINT_3 = TestGeometryUtil.randomPoint();
    private static final Point POINT_4 = TestGeometryUtil.randomPoint();

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
        final NodeId startNodeId = NodeId.of("1");
        final ExternalId startNodeExtId = ExternalIdUtil.forNode(startNodeId);
        final NodePK startNodePk = nodeRepository.insert(
                PersistableNode.of(startNodeExtId, NodeType.CROSSROADS, POINT_1)
        );
        final NodeId endNodeId = NodeId.of("2");
        final ExternalId endNodeExtId = ExternalIdUtil.forNode(endNodeId);
        final NodePK endNodePk = nodeRepository.insert(
                PersistableNode.of(endNodeExtId, NodeType.CROSSROADS, POINT_2)
        );

        final TransitType transitType = TransitType.BUS;
        final NetworkType networkType = NetworkType.ROAD;
        final ExternalId externalId = ExternalIdUtil.forLink(transitType,
                                                             startNodeId,
                                                             endNodeId);
        importRepository.submitToStaging(
                List.of(ImportableLink.of(externalId, networkType, LINE_1, startNodeExtId, endNodeExtId))
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
                   is(startNodePk));

        assertThat(link.endNode(),
                   is(endNodePk));
    }

    @Test
    public void whenStagedRowsWithChangesAndCommit_andTargetNotEmpty_thenReturnResultWithUpdatedId() {
        final NodeId startNodeId = NodeId.of("1");
        final ExternalId startNodeExtId = ExternalIdUtil.forNode(startNodeId);
        final NodePK startNodePk = nodeRepository.insert(
                PersistableNode.of(startNodeExtId, NodeType.CROSSROADS, POINT_1)
        );
        final NodeId endNodeId = NodeId.of("2");
        final ExternalId endNodeExtId = ExternalIdUtil.forNode(endNodeId);
        final NodePK endNodePk = nodeRepository.insert(
                PersistableNode.of(endNodeExtId, NodeType.CROSSROADS, POINT_2)
        );

        final TransitType transitType = TransitType.BUS;
        final NetworkType networkType = NetworkType.ROAD;
        final ExternalId externalId = ExternalIdUtil.forLink(transitType,
                                                             startNodeId,
                                                             endNodeId);
        final LinkPK existingId = targetRepository.insert(
                PersistableLink.of(externalId, networkType, LINE_1, startNodePk, endNodePk)
        );

        assertThat("Target repository should now contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        importRepository.submitToStaging(
                List.of(ImportableLink.of(externalId, networkType, LINE_2, startNodeExtId, endNodeExtId))
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
                   geometriesMatch(link.geometry(), LINE_2),
                   is(true));
        assertThat(link.startNode(),
                   is(startNodePk));
        assertThat(link.endNode(),
                   is(endNodePk));
    }

    @Test
    public void whenStagedRowsWithNoChangesAndCommit_andTargetNotEmpty_thenReturnEmptyResult() {
        final NodeId startNodeId = NodeId.of("1");
        final ExternalId startNodeExtId = ExternalIdUtil.forNode(startNodeId);
        final NodePK startNodePk = nodeRepository.insert(
                PersistableNode.of(startNodeExtId, NodeType.CROSSROADS, POINT_1)
        );
        final NodeId endNodeId = NodeId.of("2");
        final ExternalId endNodeExtId = ExternalIdUtil.forNode(endNodeId);
        final NodePK endNodePk = nodeRepository.insert(
                PersistableNode.of(endNodeExtId, NodeType.CROSSROADS, POINT_2)
        );

        final TransitType transitType = TransitType.BUS;
        final NetworkType networkType = NetworkType.ROAD;
        final ExternalId externalId = ExternalIdUtil.forLink(transitType,
                                                             startNodeId,
                                                             endNodeId);
        final PersistableLink sourceLink = PersistableLink.of(externalId, networkType, LINE_1, startNodePk, endNodePk);

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
                                          startNodeExtId,
                                          endNodeExtId))
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
                   geometriesMatch(link.geometry(), LINE_1),
                   is(true));
        assertThat(link.startNode(),
                   is(startNodePk));
        assertThat(link.endNode(),
                   is(endNodePk));
    }

    @Test
    public void whenStagedRowsWithExternalIdChangesAndCommit_andTargetNotEmpty_thenReturnDeleteAndInsertResult() {
        final NodeId startNodeId = NodeId.of("1");
        final ExternalId startNodeExtId = ExternalIdUtil.forNode(startNodeId);
        final NodePK startNodePk = nodeRepository.insert(
                PersistableNode.of(startNodeExtId, NodeType.CROSSROADS, POINT_1)
        );
        final NodeId endNodeId = NodeId.of("2");
        final ExternalId endNodeExtId = ExternalIdUtil.forNode(endNodeId);
        final NodePK endNodePk = nodeRepository.insert(
                PersistableNode.of(endNodeExtId, NodeType.CROSSROADS, POINT_2)
        );

        final TransitType transitType = TransitType.BUS;
        final NetworkType networkType = NetworkType.ROAD;
        final ExternalId externalId = ExternalIdUtil.forLink(transitType,
                                                             startNodeId,
                                                             endNodeId);
        final PersistableLink sourceLink = PersistableLink.of(externalId, networkType, LINE_1, startNodePk, endNodePk);

        final LinkPK existingId = targetRepository.insert(sourceLink);

        assertThat("Target repository should now contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        // We submit the same link, but with new jore transit type (and hence new external id)
        final TransitType newTransitType = TransitType.TRAIN;
        final NetworkType newNetworkType = NetworkType.RAILWAY;
        final ExternalId newExternalId = ExternalIdUtil.forLink(newTransitType,
                                                                startNodeId,
                                                                endNodeId);
        importRepository.submitToStaging(
                List.of(ImportableLink.of(newExternalId,
                                          newNetworkType,
                                          sourceLink.geometry(),
                                          startNodeExtId,
                                          endNodeExtId))
        );

        final Map<RowStatus, Set<LinkPK>> result = importRepository.commitStagingToTarget();

        assertThat("Only INSERT and DELETE operations should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.INSERTED,
                                 RowStatus.DELETED)));

        assertThat("The original link is deleted",
                   result.get(RowStatus.DELETED).get(),
                   is(HashSet.of(existingId)));

        final Optional<Link> oldLink = targetRepository.findById(existingId);

        assertThat("The original link is deleted",
                   oldLink.isEmpty(),
                   is(true));

        assertThat("A single link exists in the target repository",
                   targetRepository.count(),
                   is(1));

        final Link modifiedLink = targetRepository.findAll().get(0);

        assertThat("The target row was not changed",
                   geometriesMatch(modifiedLink.geometry(), LINE_1),
                   is(true));
        assertThat(modifiedLink.startNode(),
                   is(startNodePk));
        assertThat(modifiedLink.endNode(),
                   is(endNodePk));
    }

    @Test
    public void whenStagedRowsAndCommit_andTargetContainsExtraRows_thenReturnResultWithDeletedId() {

        final NodeId firstNodeId = NodeId.of("1");
        final NodeId secondNodeId = NodeId.of("2");
        final NodeId thirdNodeId = NodeId.of("3");
        final NodeId fourthNodeId = NodeId.of("4");

        final ExternalId firstNodeExtId = ExternalIdUtil.forNode(firstNodeId);
        final ExternalId secondNodeExtId = ExternalIdUtil.forNode(secondNodeId);
        final ExternalId thirdNodeExtId = ExternalIdUtil.forNode(thirdNodeId);
        final ExternalId fourthNodeExtId = ExternalIdUtil.forNode(fourthNodeId);

        final List<NodePK> nodeIds = nodeRepository.insert(
                PersistableNode.of(firstNodeExtId, NodeType.CROSSROADS, POINT_1),
                PersistableNode.of(secondNodeExtId, NodeType.CROSSROADS, POINT_2),
                PersistableNode.of(thirdNodeExtId, NodeType.CROSSROADS, POINT_3),
                PersistableNode.of(fourthNodeExtId, NodeType.CROSSROADS, POINT_4)
        );
        // Insert two links into the target table (as if imported previously)
        final TransitType transitType = TransitType.BUS;
        final NetworkType networkType = NetworkType.ROAD;
        final ExternalId firstLinkExternalId = ExternalIdUtil.forLink(transitType,
                                                                      firstNodeId,
                                                                      secondNodeId);
        final ExternalId secondLinkExternalId = ExternalIdUtil.forLink(transitType,
                                                                       thirdNodeId,
                                                                       fourthNodeId);
        final PersistableLink firstLink = PersistableLink.of(firstLinkExternalId, networkType, LINE_1, nodeIds.get(0), nodeIds.get(1));
        final PersistableLink secondLink = PersistableLink.of(secondLinkExternalId, networkType, LINE_2, nodeIds.get(2), nodeIds.get(3));

        final LinkPK firstId = targetRepository.insert(firstLink);
        final LinkPK secondId = targetRepository.insert(secondLink);

        // We submit only the latter link as-is, simulating the case where the first link is removed at the source
        importRepository.submitToStaging(
                List.of(ImportableLink.of(secondLink.externalId(),
                                          secondLink.networkType(),
                                          secondLink.geometry(),
                                          thirdNodeExtId,
                                          fourthNodeExtId))
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
