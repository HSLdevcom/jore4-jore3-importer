package fi.hsl.jore.importer.feature.batch.node.support;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.ImportableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
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
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NodeImportRepositoryTest extends IntegrationTest {

    private static final Point POINT_1 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0)
    );

    private static final Point POINT_2 = GeometryUtil.toPoint(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.158988620, 24.939328727, 0)
    );

    private final INodeImportRepository importRepository;
    private final INodeRepository targetRepository;

    public NodeImportRepositoryTest(@Autowired final INodeImportRepository importRepository,
                                    @Autowired final INodeRepository targetRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
    }

    @Test
    public void whenNoStagedRowsAndCommit_thenReturnEmptyResult() {
        assertThat(importRepository.commitStagingToTarget(),
                   is(HashMap.empty()));
    }

    @Test
    public void whenNewStagedRowsAndCommit_andTargetDbEmpty_thenReturnResultWithInsertedId() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

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
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        final List<NodePK> existing = targetRepository.upsert(
                List.of(PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, POINT_1))
        );

        final NodePK existingId = existing.get(0);

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
                   node.location(),
                   is(POINT_2));
    }

    @Test
    public void whenStagedRowsWithNoChangesAndCommit_andTargetNotEmpty_thenReturnEmptyResult() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        final PersistableNode sourceNode = PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, POINT_1);

        final List<NodePK> existing = targetRepository.upsert(
                List.of(sourceNode)
        );

        final NodePK existingId = existing.get(0);

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
                   node.location(),
                   is(POINT_1));
    }

    @Test
    public void whenNoStagedRowsAndCommit_andTargetNotEmpty_thenReturnResultWithDeletedId() {
        assertThat("Target repository should be empty before import",
                   targetRepository.empty(),
                   is(true));

        // Insert two nodes into the target table (as if imported previously)
        final PersistableNode firstNode = PersistableNode.of(ExternalId.of("a"), NodeType.CROSSROADS, POINT_1);
        final PersistableNode secondNode = PersistableNode.of(ExternalId.of("b"), NodeType.CROSSROADS, POINT_2);

        final List<NodePK> existing = targetRepository.upsert(
                List.of(firstNode,
                        secondNode)
        );

        final NodePK firstId = existing.get(0);
        final NodePK secondId = existing.get(1);

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
