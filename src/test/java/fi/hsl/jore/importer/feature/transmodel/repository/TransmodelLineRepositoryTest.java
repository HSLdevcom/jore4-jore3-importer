package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;
import fi.hsl.jore.importer.feature.transmodel.entity.VehicleMode;
import org.assertj.core.api.Assertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static fi.hsl.jore.importer.feature.transmodel.util.TimestampFactory.offsetDateTimeFromLocalDateTime;
import static fi.hsl.jore.jore4.jooq.route.Tables.LINE;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class TransmodelLineRepositoryTest {

    private static final UUID LINE_ID = UUID.fromString("237fa7b1-bd07-4206-b018-4144750ec689");
    private static final String EXTERNAL_LINE_ID = "9009";
    private static final String LABEL = "1";

    private static final String EXPECTED_JORE4_NAME = "{\"fi_FI\":\"Vantaanportti-Lentoasema-Kerava\",\"sv_SE\":\"Vandaporten-Flygstationen-Kervo\"}";
    private static final String EXPECTED_JORE4_SHORT_NAME = "{\"fi_FI\":\"Vantaanp-Kerava\",\"sv_SE\":\"Vandap-Kervo\"}";

    private static final String FINNISH_NAME = "Vantaanportti-Lentoasema-Kerava";
    private static final String FINNISH_SHORT_NAME = "Vantaanp-Kerava";
    private static final String SWEDISH_NAME = "Vandaporten-Flygstationen-Kervo";
    private static final String SWEDISH_SHORT_NAME = "Vandap-Kervo";

    private static final VehicleMode PRIMARY_VEHICLE_MODE = VehicleMode.BUS;
    private static final int PRIORITY = 10;

    private static final OffsetDateTime VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                    LocalDate.of(2019, 1, 1),
                    OPERATING_DAY_START_TIME
            )
    );
    private static final OffsetDateTime VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                    LocalDate.of(2020, 1, 1),
                    OPERATING_DAY_END_TIME
            )
    );

    private final TransmodelLineRepository repository;
    private final Table targetTable;
    private final TransmodelValidityPeriodTestRepository testRepository;

    @Autowired
    TransmodelLineRepositoryTest(final TransmodelLineRepository repository,
                                 @Qualifier("jore4DataSource") final DataSource targetDataSource) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "route.line");
        this.testRepository = new TransmodelValidityPeriodTestRepository(targetDataSource,
                ValidityPeriodTargetTable.LINE
        );
    }

    @Nested
    @DisplayName("Insert line into the database")
    @Sql(
            scripts = {
                    "/sql/transmodel/drop_tables.sql",
                    "/sql/transmodel/populate_vehicle_modes.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    class InsertLineIntoDatabase {

        private final TransmodelLine INPUT = TransmodelLine.of(
                LINE_ID,
                EXTERNAL_LINE_ID,
                LABEL,
                JoreLocaleUtil.createMultilingualString(FINNISH_NAME, SWEDISH_NAME),
                JoreLocaleUtil.createMultilingualString(FINNISH_SHORT_NAME, SWEDISH_SHORT_NAME),
                PRIMARY_VEHICLE_MODE,
                PRIORITY,
                Optional.of(VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE),
                Optional.of(VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE)
        );

        @Test
        @DisplayName("Should insert one line into the database")
        void shouldInsertOneLineIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should insert the correct id into the database")
        void shouldInsertCorrectIdIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.LINE_ID.getName())
                    .isEqualTo(LINE_ID);
        }

        @Test
        @DisplayName("Should insert the correct label into the database")
        void shouldInsertCorrectLabelIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.LABEL.getName())
                    .isEqualTo(LABEL);
        }

        @Test
        @DisplayName("Should insert the correct name into the database")
        void shouldInsertCorrectNameIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.NAME_I18N.getName())
                    .isEqualTo(EXPECTED_JORE4_NAME);
        }

        @Test
        @DisplayName("Should insert the correct short name into the database")
        void shouldInsertCorrectShortNameIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.SHORT_NAME_I18N.getName())
                    .isEqualTo(EXPECTED_JORE4_SHORT_NAME);
        }

        @Test
        @DisplayName("Should insert the correct primary vehicle mode into the database")
        void shouldInsertCorrectPrimaryVehicleModeIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.PRIMARY_VEHICLE_MODE.getName())
                    .isEqualTo(PRIMARY_VEHICLE_MODE.getValue());
        }

        @Test
        @DisplayName("Should insert the correct priority into the database")
        void shouldInsertCorrectPriorityIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.PRIORITY.getName())
                    .isEqualTo(PRIORITY);
        }

        @Test
        @DisplayName("Should insert the correct validity period start time into the database")
        void shouldInsertCorrectValidityPeriodStartTimeIntoDatabase() {
            repository.insert(List.of(INPUT));

            final OffsetDateTime validityStart = testRepository.findValidityPeriodStartTimestampAtFinnishTimeZone();
            Assertions.assertThat(validityStart)
                    .as(LINE.VALIDITY_START.getName())
                    .isEqualTo(VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE);
        }

        @Test
        @DisplayName("Should insert the correct validity period end time into the database")
        void shouldInsertCorrectValidityPeriodEndTimeIntoDatabase() {
            repository.insert(List.of(INPUT));

            final OffsetDateTime validityEnd = testRepository.findValidityPeriodEndTimestampAtFinnishTimeZone();
            Assertions.assertThat(validityEnd)
                    .as(LINE.VALIDITY_END.getName())
                    .isEqualTo(VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE);
        }
    }
}
