package fi.hsl.jore.importer.feature.batch.line.support;

import static fi.hsl.jore.importer.util.JoreCollectionUtils.getFirst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import fi.hsl.jore.importer.feature.network.line.dto.Line;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.feature.network.line.repository.ILineTestRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@IntTest
public class LineImportRepositoryTest {

    private final ILineImportRepository importRepository;
    private final ILineTestRepository targetRepository;

    public LineImportRepositoryTest(
            @Autowired final ILineImportRepository importRepository,
            @Autowired final ILineTestRepository targetRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
    }

    @Nested
    @DisplayName("Commit staging to the target table")
    @Sql(scripts = "/sql/importer/drop_tables.sql")
    class CommitStagingToTarget {

        private static final String LINE_NUMBER = "1005";
        private static final String DISPLAY_LINE_NUMBER = "5";
        private final LegacyHslMunicipalityCode LINE_LEGACY_HSL_MUNICIPALITY_CODE = LegacyHslMunicipalityCode.of('1');
        private final ExternalId EXT_ID = ExternalIdUtil.forLine(LineId.from(LINE_NUMBER));
        private final NetworkType NETWORK = NetworkType.ROAD;
        private final TypeOfLine TYPE_OF_LINE = TypeOfLine.STOPPING_BUS_SERVICE;

        @BeforeEach
        public void beforeEach() {
            assertThat(
                    "Target repository should be empty at the start of the test", targetRepository.empty(), is(true));
        }

        @Test
        public void whenNoStagedRowsAndCommit_thenReturnEmptyResult() {
            assertThat(importRepository.commitStagingToTarget(), is(Collections.emptyMap()));
        }

        @Test
        public void whenNewStagedRowsAndCommit_andTargetDbEmpty_thenReturnResultWithInsertedId() {
            importRepository.submitToStaging(List.of(PersistableLine.of(
                    EXT_ID, DISPLAY_LINE_NUMBER, NETWORK, TYPE_OF_LINE, LINE_LEGACY_HSL_MUNICIPALITY_CODE)));

            final Map<RowStatus, Set<LinePK>> result = importRepository.commitStagingToTarget();

            assertThat("Only INSERTs should occur", result.keySet(), is(Set.of(RowStatus.INSERTED)));

            final Set<LinePK> ids = result.get(RowStatus.INSERTED);

            assertThat("Only a single row is inserted", ids.size(), is(1));

            final LinePK id = getFirst(ids);

            assertThat("Target repository should contain a single row", targetRepository.count(), is(1));

            assertThat(
                    "Target repository (including history) should contain a single row",
                    targetRepository.countHistory(),
                    is(1));

            final Optional<Line> maybeLine = targetRepository.findById(id);
            assertThat("The inserted row is found in the target repository", maybeLine.isPresent(), is(true));
        }

        @Test
        public void whenNoStagedRowsAndCommit_andTargetNotEmpty_thenReturnResultWithDeletedId() {
            final LinePK existingId = targetRepository.insert(PersistableLine.of(
                    EXT_ID, DISPLAY_LINE_NUMBER, NETWORK, TYPE_OF_LINE, LINE_LEGACY_HSL_MUNICIPALITY_CODE));

            final Map<RowStatus, Set<LinePK>> result = importRepository.commitStagingToTarget();

            assertThat("Only DELETEs should occur", result.keySet(), is(Set.of(RowStatus.DELETED)));

            final Set<LinePK> ids = result.get(RowStatus.DELETED);

            assertThat("Only a single row is deleted", ids, is(Set.of(existingId)));

            assertThat("Target repository should be empty after import", targetRepository.empty(), is(true));

            assertThat(
                    "Target repository (including history) should contain a single row",
                    targetRepository.countHistory(),
                    is(1));
        }
    }
}
