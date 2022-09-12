package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fi.hsl.jore.importer.TestJsonUtil.equalJson;
import static fi.hsl.jore.jore4.jooq.route.Tables.LINE;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class Jore4LineRepositoryTest {

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
    private static final TypeOfLine TYPE_OF_LINE = TypeOfLine.STOPPING_BUS_SERVICE;
    private static final int PRIORITY = 10;

    private static final LocalDate VALIDITY_PERIOD_START = LocalDate.of(2019, 1, 1);
    private static final LocalDate VALIDITY_PERIOD_END = LocalDate.of(2020, 1, 1);

    private static final LegacyHslMunicipalityCode LEGACY_HSL_MUNICIPALITY_CODE = LegacyHslMunicipalityCode.HELSINKI;

    private final Jore4LineRepository repository;
    private final Table targetTable;
    private final Jore4ValidityPeriodTestRepository testRepository;

    @Autowired
    Jore4LineRepositoryTest(final Jore4LineRepository repository,
                            @Qualifier("jore4DataSource") final DataSource targetDataSource) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "route.line");
        this.testRepository = new Jore4ValidityPeriodTestRepository(targetDataSource,
                ValidityPeriodTargetTable.LINE
        );
    }

    @Nested
    @DisplayName("Insert line into the database")
    @Sql(
            scripts = {
                    "/sql/transmodel/drop_tables.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    class InsertLineIntoDatabase {

        private final Jore4Line INPUT = Jore4Line.of(
                LINE_ID,
                EXTERNAL_LINE_ID,
                LABEL,
                JoreLocaleUtil.createMultilingualString(FINNISH_NAME, SWEDISH_NAME),
                JoreLocaleUtil.createMultilingualString(FINNISH_SHORT_NAME, SWEDISH_SHORT_NAME),
                PRIMARY_VEHICLE_MODE,
                TYPE_OF_LINE,
                PRIORITY,
                Optional.of(VALIDITY_PERIOD_START),
                Optional.of(VALIDITY_PERIOD_END),
                LEGACY_HSL_MUNICIPALITY_CODE
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
                    .is(equalJson(EXPECTED_JORE4_NAME));
        }

        @Test
        @DisplayName("Should insert the correct short name into the database")
        void shouldInsertCorrectShortNameIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.SHORT_NAME_I18N.getName())
                    .is(equalJson(EXPECTED_JORE4_SHORT_NAME));
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

            final LocalDate validityStart = testRepository.findValidityPeriodStartDate();
            Assertions.assertThat(validityStart)
                    .as(LINE.VALIDITY_START.getName())
                    .isEqualTo(VALIDITY_PERIOD_START);
        }

        @Test
        @DisplayName("Should insert the correct validity period end time into the database")
        void shouldInsertCorrectValidityPeriodEndTimeIntoDatabase() {
            repository.insert(List.of(INPUT));

            final LocalDate validityEnd = testRepository.findValidityPeriodEndDate();
            Assertions.assertThat(validityEnd)
                    .as(LINE.VALIDITY_END.getName())
                    .isEqualTo(VALIDITY_PERIOD_END);
        }

        @Test
        @DisplayName("Should insert the correct legacy HSL municipality code")
        void shouldInsertCorrectLegacyHslMunicipalityCodeIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(LINE.LEGACY_HSL_MUNICIPALITY_CODE.getName())
                    .isEqualTo(LEGACY_HSL_MUNICIPALITY_CODE.getJore4Value());
        }
    }
}
