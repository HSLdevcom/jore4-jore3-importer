package fi.hsl.jore.importer.feature.batch.node.support;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.TestGeometryUtil;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.ImportableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeTestRepository;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static fi.hsl.jore.importer.TestGeometryUtil.geometriesMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NodeImportRepositoryTest extends IntegrationTest {

    private static final Point POINT_1 = TestGeometryUtil.randomPoint();
    private static final Point POINT_2 = TestGeometryUtil.randomPoint();

    private final INodeImportRepository importRepository;
    private final INodeTestRepository targetRepository;

    public NodeImportRepositoryTest(@Autowired final INodeImportRepository importRepository,
                                    @Autowired final INodeTestRepository targetRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
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
    public void whenNewStagedRowsAndCommit_andTargetDbEmpty_thenReturnResultWithInsertedId() {
        importRepository.submitToStaging(
                List.of(ImportableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, POINT_1))
        );

        final Map<RowStatus, Set<NodePK>> result = importRepository.commitStagingToTarget();

        assertThat("Only INSERTs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.INSERTED)));

        final Set<NodePK> ids = result.get(RowStatus.INSERTED).get();

        assertThat("Only a single row is inserted",
                   ids.size(),
                   is(1));

        final NodePK id = ids.get();

        assertThat("Target repository should contain a single row",
                   targetRepository.findAllIds(),
                   is(ids));

        final Optional<Node> maybeNode = targetRepository.findById(id);
        assertThat("The inserted row is found in the target repository",
                   maybeNode.isPresent(),
                   is(true));
    }

    @Test
    public void whenStagedRowsWithChangesAndCommit_andTargetNotEmpty_thenReturnResultWithUpdatedId() {
        final NodePK existingId = targetRepository.insert(
                PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, POINT_1)
        );

        assertThat("Target repository should now contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        importRepository.submitToStaging(
                List.of(ImportableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, POINT_2))
        );

        final Map<RowStatus, Set<NodePK>> result = importRepository.commitStagingToTarget();

        assertThat("Only UPDATEs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.UPDATED)));

        final Set<NodePK> ids = result.get(RowStatus.UPDATED).get();

        assertThat("Only a single row is updated",
                   ids,
                   is(HashSet.of(existingId)));

        assertThat("Target repository should still contain a single row",
                   targetRepository.findAllIds(),
                   is(ids));

        final Node node = targetRepository.findById(existingId).orElseThrow();

        assertThat("The updated rows location was changed",
                   geometriesMatch(node.location(), POINT_2),
                   is(true));
    }

    @Test
    public void whenStagedRowsWithNoChangesAndCommit_andTargetNotEmpty_thenReturnEmptyResult() {
        final PersistableNode sourceNode = PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, POINT_1);

        final NodePK existingId = targetRepository.insert(
                sourceNode
        );

        assertThat("Target repository should now contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        // We submit the same node
        importRepository.submitToStaging(
                List.of(ImportableNode.of(sourceNode.externalId(),
                                          sourceNode.nodeType(),
                                          sourceNode.location()))
        );

        final Map<RowStatus, Set<NodePK>> result = importRepository.commitStagingToTarget();

        assertThat("No operations should occur",
                   result.keySet(),
                   is(HashSet.empty()));

        assertThat("Target repository should still contain a single row",
                   targetRepository.findAllIds(),
                   is(HashSet.of(existingId)));

        final Node node = targetRepository.findById(existingId).orElseThrow();
        assertThat("The target row was not changed",
                   geometriesMatch(node.location(), POINT_1),
                   is(true));
    }

    @Test
    public void whenNoStagedRowsAndCommit_andTargetNotEmpty_thenReturnResultWithDeletedId() {
        // Insert two nodes into the target table (as if imported previously)
        final PersistableNode firstNode = PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, POINT_1);
        final PersistableNode secondNode = PersistableNode.of(ExternalId.of("b"), NodeType.CROSSROADS, POINT_2);

        final NodePK firstId = targetRepository.insert(firstNode);
        final NodePK secondId = targetRepository.insert(secondNode);

        // We submit only the latter node as-is, simulating the case where the first node is removed at the source
        importRepository.submitToStaging(
                List.of(ImportableNode.of(secondNode.externalId(),
                                          secondNode.nodeType(),
                                          secondNode.location()))
        );

        final Map<RowStatus, Set<NodePK>> result = importRepository.commitStagingToTarget();

        assertThat("Only DELETEs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.DELETED)));

        final Set<NodePK> ids = result.get(RowStatus.DELETED).get();

        assertThat("Only a single row is deleted",
                   ids,
                   is(HashSet.of(firstId)));

        assertThat("Target repository should contain the second node",
                   targetRepository.findAllIds(),
                   is(HashSet.of(secondId)));
    }
}
