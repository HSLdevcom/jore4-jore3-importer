package fi.hsl.jore.importer.feature.batch.line.support;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.network.line.dto.Line;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.feature.network.line.repository.ILineTestRepository;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LineImportRepositoryTest extends IntegrationTest {

    private static final String LINE_NUMBER = "1005";
    private static final String DISPLAY_LINE_NUMBER = "5";
    private static final ExternalId EXT_ID = ExternalIdUtil.forLine(LineId.from(LINE_NUMBER));
    private static final NetworkType NETWORK = NetworkType.ROAD;

    private final ILineImportRepository importRepository;
    private final ILineTestRepository targetRepository;

    public LineImportRepositoryTest(@Autowired final ILineImportRepository importRepository,
                                    @Autowired final ILineTestRepository targetRepository) {
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
                List.of(PersistableLine.of(EXT_ID,
                                           DISPLAY_LINE_NUMBER,
                                           NETWORK))
        );

        final Map<RowStatus, Set<LinePK>> result = importRepository.commitStagingToTarget();

        assertThat("Only INSERTs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.INSERTED)));

        final Set<LinePK> ids = result.get(RowStatus.INSERTED).get();

        assertThat("Only a single row is inserted",
                   ids.size(),
                   is(1));

        final LinePK id = ids.get();

        assertThat("Target repository should contain a single row",
                   targetRepository.count(),
                   is(1));

        assertThat("Target repository (including history) should contain a single row",
                   targetRepository.countHistory(),
                   is(1));

        final Optional<Line> maybeLine = targetRepository.findById(id);
        assertThat("The inserted row is found in the target repository",
                   maybeLine.isPresent(),
                   is(true));
    }

    @Test
    public void whenNoStagedRowsAndCommit_andTargetNotEmpty_thenReturnResultWithDeletedId() {
        final LinePK existingId = targetRepository.insert(
                PersistableLine.of(EXT_ID,
                                   DISPLAY_LINE_NUMBER,
                                   NETWORK)
        );

        final Map<RowStatus, Set<LinePK>> result = importRepository.commitStagingToTarget();

        assertThat("Only DELETEs should occur",
                   result.keySet(),
                   is(HashSet.of(RowStatus.DELETED)));

        final Set<LinePK> ids = result.get(RowStatus.DELETED).get();

        assertThat("Only a single row is deleted",
                   ids,
                   is(HashSet.of(existingId)));

        assertThat("Target repository should be empty after import",
                   targetRepository.empty(),
                   is(true));

        assertThat("Target repository (including history) should contain a single row",
                   targetRepository.countHistory(),
                   is(1));
    }
}
