package fi.hsl.jore.importer.feature.batch.line_header.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLineIdMapping;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.feature.network.line.repository.ILineTestRepository;
import fi.hsl.jore.importer.feature.network.line_header.dto.Jore3LineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.LineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.PersistableLineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.generated.LineHeaderPK;
import fi.hsl.jore.importer.feature.network.line_header.repository.ILineHeaderTestRepository;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@IntTest
public class LineHeaderImportRepositoryTest {

    private final ILineHeaderImportRepository importRepository;
    private final ILineHeaderTestRepository targetRepository;
    private final ILineTestRepository lineRepository;

    public LineHeaderImportRepositoryTest(
            @Autowired final ILineHeaderImportRepository importRepository,
            @Autowired final ILineHeaderTestRepository targetRepository,
            @Autowired final ILineTestRepository lineRepository) {
        this.importRepository = importRepository;
        this.targetRepository = targetRepository;
        this.lineRepository = lineRepository;
    }

    @Nested
    @DisplayName("Commit staging to the target table")
    @Sql(scripts = "/sql/importer/drop_tables.sql")
    class CommitStagingToTarget {

        private static final String LINE_NUMBER = "1005";
        private static final String DISPLAY_LINE_NUMBER = "5";
        private static final LegacyHslMunicipalityCode LINE_LEGACY_HSL_MUNICIPALITY_CODE =
                LegacyHslMunicipalityCode.of('1');
        private static final ExternalId LINE_EXT_ID = ExternalIdUtil.forLine(LineId.from(LINE_NUMBER));
        private static final NetworkType NETWORK = NetworkType.ROAD;
        private static final MultilingualString NAME =
                MultilingualString.empty().with(Locale.ENGLISH, "name");
        private static final MultilingualString NAME_SHORT =
                MultilingualString.empty().with(Locale.ENGLISH, "name-short");

        private static final MultilingualString ORIGIN_1 =
                MultilingualString.empty().with(Locale.ENGLISH, "origin-1");

        private static final MultilingualString ORIGIN_2 =
                MultilingualString.empty().with(Locale.ENGLISH, "origin-2");

        private static final DateRange VALID_TIME =
                DateRange.between(LocalDate.of(2020, 5, 6), LocalDate.of(2021, 6, 25));

        private static final ExternalId HEADER_EXT_ID = ExternalIdUtil.forLineHeader(
                LineId.from(LINE_NUMBER), VALID_TIME.range().lowerEndpoint());

        private final TypeOfLine TYPE_OF_LINE = TypeOfLine.STOPPING_BUS_SERVICE;

        @BeforeEach
        public void beforeEach() {
            assertThat(
                    "Target repository should be empty at the start of the test", targetRepository.empty(), is(true));
            assertThat("Line repository should be empty at the start of the test", lineRepository.empty(), is(true));
        }

        @Test
        public void whenNoStagedRowsAndCommit_thenReturnEmptyResult() {
            assertThat(importRepository.commitStagingToTarget(), is(HashMap.empty()));
        }

        @Test
        public void whenNewStagedRowsAndCommit_andTargetDbEmpty_thenReturnResultWithInsertedId() {
            // Insert the parent line
            lineRepository.insert(PersistableLine.of(
                    LINE_EXT_ID, DISPLAY_LINE_NUMBER, NETWORK, TYPE_OF_LINE, LINE_LEGACY_HSL_MUNICIPALITY_CODE));

            importRepository.submitToStaging(List.of(
                    Jore3LineHeader.of(HEADER_EXT_ID, LINE_EXT_ID, NAME, NAME_SHORT, ORIGIN_1, ORIGIN_2, VALID_TIME)));

            final Map<RowStatus, Set<LineHeaderPK>> result = importRepository.commitStagingToTarget();

            assertThat("Only INSERTs should occur", result.keySet(), is(HashSet.of(RowStatus.INSERTED)));

            final Set<LineHeaderPK> ids = result.get(RowStatus.INSERTED).get();

            assertThat("Only a single row is inserted", ids.size(), is(1));

            final LineHeaderPK id = ids.get();

            assertThat("Target repository should contain a single row", targetRepository.count(), is(1));

            assertThat(
                    "Target repository (including history) should contain a single row",
                    targetRepository.countHistory(),
                    is(1));

            final Optional<LineHeader> maybeLineHeader = targetRepository.findById(id);
            assertThat("The inserted row is found in the target repository", maybeLineHeader.isPresent(), is(true));
        }

        @Test
        public void whenStagedRowsWithChangesAndCommit_andTargetNotEmpty_thenReturnResultWithIdOfUpdatedRow() {
            // Insert the parent line
            final LinePK lineId = lineRepository.insert(PersistableLine.of(
                    LINE_EXT_ID, DISPLAY_LINE_NUMBER, NETWORK, TYPE_OF_LINE, LINE_LEGACY_HSL_MUNICIPALITY_CODE));

            final LineHeaderPK existingId = targetRepository.insert(
                    PersistableLineHeader.of(HEADER_EXT_ID, lineId, NAME, NAME_SHORT, ORIGIN_1, ORIGIN_2, VALID_TIME));

            assertThat("Target repository should now contain a single row", targetRepository.count(), is(1));

            final MultilingualString newName = NAME.with(Locale.GERMAN, "foo");

            importRepository.submitToStaging(List.of(Jore3LineHeader.of(
                    HEADER_EXT_ID, LINE_EXT_ID, newName, NAME_SHORT, ORIGIN_1, ORIGIN_2, VALID_TIME)));

            final Map<RowStatus, Set<LineHeaderPK>> result = importRepository.commitStagingToTarget();

            assertThat("Only UPDATEs should occur", result.keySet(), is(HashSet.of(RowStatus.UPDATED)));

            final Set<LineHeaderPK> ids = result.get(RowStatus.UPDATED).get();

            assertThat("Only a single row is updated", ids, is(HashSet.of(existingId)));

            assertThat("Target repository should still contain a single row", targetRepository.count(), is(1));

            assertThat(
                    "Target repository (including history) should contain both the original and modified row",
                    targetRepository.countHistory(),
                    is(2));

            final Optional<LineHeader> maybeLineHeader = targetRepository.findById(existingId);
            assertThat("The updated row is found in the target repository", maybeLineHeader.isPresent(), is(true));

            assertThat(
                    "The updated rows name was changed", maybeLineHeader.get().name(), is(newName));
        }

        @Test
        public void
                whenStagedRowsWithExternalIdChangesAndCommit_andTargetNotEmpty_thenReturnResultWithDeletedAndInsertedIds() {
            // Insert the parent line
            final LinePK lineId = lineRepository.insert(PersistableLine.of(
                    LINE_EXT_ID, DISPLAY_LINE_NUMBER, NETWORK, TYPE_OF_LINE, LINE_LEGACY_HSL_MUNICIPALITY_CODE));

            final LineHeaderPK existingId = targetRepository.insert(
                    PersistableLineHeader.of(HEADER_EXT_ID, lineId, NAME, NAME_SHORT, ORIGIN_1, ORIGIN_2, VALID_TIME));

            assertThat("Target repository should now contain a single row", targetRepository.count(), is(1));

            // Valid time (start date) is part of the external ID
            // => Changes in valid time changes the external ID
            // => We can't track changes in the external ID
            // => It looks like the old entity was removed and a new entity has been created
            final DateRange newValidTime = DateRange.between(LocalDate.of(2020, 5, 8), LocalDate.of(2021, 6, 20));
            final ExternalId newExternalId = ExternalIdUtil.forLineHeader(
                    LineId.from(LINE_NUMBER), newValidTime.range().lowerEndpoint());

            importRepository.submitToStaging(List.of(Jore3LineHeader.of(
                    newExternalId, LINE_EXT_ID, NAME, NAME_SHORT, ORIGIN_1, ORIGIN_2, newValidTime)));

            final Map<RowStatus, Set<LineHeaderPK>> result = importRepository.commitStagingToTarget();

            assertThat(
                    "Only INSERTs and DELETEs should occur",
                    result.keySet(),
                    is(HashSet.of(RowStatus.INSERTED, RowStatus.DELETED)));

            final Set<LineHeaderPK> deletedIds = result.get(RowStatus.DELETED).get();

            assertThat("Only a single row is deleted", deletedIds, is(HashSet.of(existingId)));

            assertThat("Target repository should still contain a single row", targetRepository.count(), is(1));

            assertThat(
                    "Target repository (including history) should contain both the original and modified row",
                    targetRepository.countHistory(),
                    is(2));

            final Optional<LineHeader> maybeLineHeader = targetRepository.findById(existingId);
            assertThat("The deleted row is no longer in the target repository", maybeLineHeader.isEmpty(), is(true));

            final LineHeader updatedHeader = targetRepository.findAll().get(0);

            assertThat(
                    "The inserted rows valid time matches the input value",
                    updatedHeader.validTime(),
                    is(newValidTime));
        }

        @Test
        public void whenStagedRowsWithNoChangesAndCommit_andTargetNotEmpty_thenReturnEmptyResult() {
            // Insert the parent line
            final LinePK lineId = lineRepository.insert(PersistableLine.of(
                    LINE_EXT_ID, DISPLAY_LINE_NUMBER, NETWORK, TYPE_OF_LINE, LINE_LEGACY_HSL_MUNICIPALITY_CODE));

            final LineHeaderPK existingId = targetRepository.insert(
                    PersistableLineHeader.of(HEADER_EXT_ID, lineId, NAME, NAME_SHORT, ORIGIN_1, ORIGIN_2, VALID_TIME));

            assertThat("Target repository should now contain a single row", targetRepository.count(), is(1));

            assertThat(
                    "Target repository (including history) should contain a single row",
                    targetRepository.countHistory(),
                    is(1));

            // We submit the same line
            importRepository.submitToStaging(List.of(
                    Jore3LineHeader.of(HEADER_EXT_ID, LINE_EXT_ID, NAME, NAME_SHORT, ORIGIN_1, ORIGIN_2, VALID_TIME)));

            final Map<RowStatus, Set<LineHeaderPK>> result = importRepository.commitStagingToTarget();

            assertThat("No operations should occur", result.keySet(), is(HashSet.empty()));

            assertThat("Target repository should still contain a single row", targetRepository.count(), is(1));

            final Optional<LineHeader> maybeLineHeader = targetRepository.findById(existingId);
            assertThat("The target row is found in the target repository", maybeLineHeader.isPresent(), is(true));

            assertThat("The target row was not changed", maybeLineHeader.get().name(), is(NAME));
        }

        @Test
        public void whenNoStagedRowsAndCommit_andTargetNotEmpty_thenReturnResultWithDeletedId() {
            // Insert the parent line
            final LinePK lineId = lineRepository.insert(PersistableLine.of(
                    LINE_EXT_ID, DISPLAY_LINE_NUMBER, NETWORK, TYPE_OF_LINE, LINE_LEGACY_HSL_MUNICIPALITY_CODE));

            final LineHeaderPK existingId = targetRepository.insert(
                    PersistableLineHeader.of(HEADER_EXT_ID, lineId, NAME, NAME_SHORT, ORIGIN_1, ORIGIN_2, VALID_TIME));

            final Map<RowStatus, Set<LineHeaderPK>> result = importRepository.commitStagingToTarget();

            assertThat("Only DELETEs should occur", result.keySet(), is(HashSet.of(RowStatus.DELETED)));

            final Set<LineHeaderPK> ids = result.get(RowStatus.DELETED).get();

            assertThat("Only a single row is deleted", ids, is(HashSet.of(existingId)));

            assertThat("Target repository should be empty after import", targetRepository.empty(), is(true));

            assertThat(
                    "Target repository (including history) should contain a single row",
                    targetRepository.countHistory(),
                    is(1));
        }
    }

    @Nested
    @DisplayName("Set the identifier of Jore 4 line to importer line headers")
    @Sql(
            scripts = {
                "/sql/importer/drop_tables.sql",
                "/sql/importer/populate_lines.sql",
                "/sql/importer/populate_line_headers.sql"
            })
    class SetJore4Ids {

        private final ExternalId EXT_ID_OF_LINE_HEADER = ExternalId.of("1001-20211004");
        private final UUID JORE4_ID_OF_LINE = UUID.fromString("51f2686b-166c-4157-bd70-647337e44c8c");

        @Nested
        @DisplayName("When the line header isn't found")
        class WhenLineHeaderIsNotFound {

            private static final String UNKNOWN_EXT_ID_OF_LINE_HEADER = "999999999";
            private final List<PersistableLineIdMapping> INPUT =
                    List.of(PersistableLineIdMapping.of(UNKNOWN_EXT_ID_OF_LINE_HEADER, JORE4_ID_OF_LINE));

            @Test
            @DisplayName("Shouldn't update the Jore 4 ID of the existing line header")
            void shouldNotUpdateJore4IdToExistingLineHeader() {
                importRepository.setJore4Ids(INPUT);

                final LineHeader existingLineHeader =
                        targetRepository.findByExternalId(EXT_ID_OF_LINE_HEADER).get();
                assertThat(existingLineHeader.jore4IdOfLine().isEmpty(), is(true));
            }
        }

        @Nested
        @DisplayName("When the line header is found")
        class WhenLineHeaderIsFound {

            private final List<PersistableLineIdMapping> INPUT =
                    List.of(PersistableLineIdMapping.of(EXT_ID_OF_LINE_HEADER.value(), JORE4_ID_OF_LINE));

            @Test
            @DisplayName("Should update the Jore 4 ID of the existing line header")
            void shouldUpdateJore4IdToExistingLineHeader() {
                importRepository.setJore4Ids(INPUT);

                final LineHeader existingLineHeader =
                        targetRepository.findByExternalId(EXT_ID_OF_LINE_HEADER).get();
                assertThat(existingLineHeader.jore4IdOfLine(), equalTo(Optional.of(JORE4_ID_OF_LINE)));
            }
        }
    }
}
