package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.transmodel.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRoute;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteDirection;
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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fi.hsl.jore.importer.TestJsonUtil.equalJson;
import static fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil.createMultilingualString;
import static fi.hsl.jore.jore4.jooq.route.Tables.ROUTE_;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class TransmodelRouteRepositoryTest {

    private static final UUID ROUTE_ID = UUID.fromString("ed878662-dd94-4cad-90a6-597ee10ebd42");
    private static final String FINNISH_NAME = "Keskustori - Etelä-Hervanta";
    private static final String SWEDISH_NAME = "Central torget - Södra Hervanta";
    private static final String LABEL = "30";
    private static final Short HIDDEN_VARIANT = 2;
    private static final UUID LINE_ID = UUID.fromString("5aa7d9fc-2cf9-466d-8ac0-f442d60c261f");
    private static final int PRIORITY = 10;
    private static final TransmodelRouteDirection ROUTE_DIRECTION = TransmodelRouteDirection.INBOUND;
    private static final UUID ROUTE_DIRECTION_ID = UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");
    private static final LegacyHslMunicipalityCode ROUTE_LEGACY_HSL_MUNICIPALITY_CODE = LegacyHslMunicipalityCode.HELSINKI;

    private static final LocalDate VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE =
            LocalDate.of(2021, 1, 1);
    private static final LocalDate VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE =
            LocalDate.of(2022, 1, 1);

    private final TransmodelRouteRepository repository;
    private final Table targetTable;
    private final TransmodelValidityPeriodTestRepository testRepository;

    @Autowired
    TransmodelRouteRepositoryTest(final TransmodelRouteRepository repository,
                                 @Qualifier("jore4DataSource") final DataSource targetDataSource) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "route.route");
        this.testRepository = new TransmodelValidityPeriodTestRepository(targetDataSource,
                ValidityPeriodTargetTable.ROUTE
        );
    }

    @Nested
    @DisplayName("Insert routes into the database")
    @Sql(
            scripts = {
                    "/sql/transmodel/drop_tables.sql",
                    "/sql/transmodel/populate_infrastructure_links.sql",
                    "/sql/transmodel/populate_lines.sql",
                    "/sql/transmodel/populate_scheduled_stop_points.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    class InsertRoutesIntoDatabase {

        private static final String EXPECTED_DESCRIPTION = "{\"fi_FI\":\"Keskustori - Etelä-Hervanta\",\"sv_SE\":\"Central torget - Södra Hervanta\"}";

        private final TransmodelRoute INPUT = TransmodelRoute.of(
                ROUTE_ID,
                createMultilingualString(FINNISH_NAME, SWEDISH_NAME),
                ROUTE_DIRECTION,
                ROUTE_DIRECTION_ID,
                LABEL,
                Optional.of(HIDDEN_VARIANT),
                LINE_ID,
                PRIORITY,
                Optional.of(VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE),
                Optional.of(VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE),
                ROUTE_LEGACY_HSL_MUNICIPALITY_CODE
        );

        @Test
        @DisplayName("Should insert one route into the database")
        void shouldInsertOneRouteIntoDatabase() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should save a new route with correct id")
        void shouldSaveNewRouteWithCorrectId() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE_.ROUTE_ID.getName())
                    .isEqualTo(ROUTE_ID);
        }

        @Test
        @DisplayName("Should save a new route with the correct description")
        void shouldSaveNewRouteWithCorrectDescription() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE_.DESCRIPTION_I18N.getName())
                    .is(equalJson(EXPECTED_DESCRIPTION));
        }

        @Test
        @DisplayName("Should save a new route with the correct direction")
        void shouldSaveNewRouteWithCorrectDirection() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE_.DIRECTION.getName())
                    .isEqualTo(ROUTE_DIRECTION.getValue());
        }

        @Test
        @DisplayName("Should save a new route with the correct label")
        void shouldSaveNewRouteWithCorrectLabel() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE_.LABEL.getName())
                    .isEqualTo(LABEL);
        }

        @Test
        @DisplayName("Should save a new route with the correct hidden variant")
        void shouldSaveNewRouteWithCorrectHiddenVariant() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE_.VARIANT.getName())
                    .isEqualTo(HIDDEN_VARIANT);
        }

        @Test
        @DisplayName("Should save a new route with the correct line id")
        void shouldSaveNewRouteWithCorrectLineId() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE_.ON_LINE_ID.getName())
                    .isEqualTo(LINE_ID);
        }

        @Test
        @DisplayName("Should save a new route with the correct priority")
        void shouldSaveNewRouteWithCorrectPriority() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE_.PRIORITY.getName())
                    .isEqualTo(PRIORITY);
        }

        @Test
        @DisplayName("Should save a new route with the correct legacy HSL municipality code")
        void shouldSaveNewRouteWithCorrectLegacyHslMunicipalityCode() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(ROUTE_.LEGACY_HSL_MUNICIPALITY_CODE.getName())
                    .isEqualTo(LegacyHslMunicipalityCode.HELSINKI.getJore4Value());
        }

        @Test
        @DisplayName("Should save a new route with the correct validity period start time")
        void shouldSaveNewRouteWithCorrectValidityPeriodStartTime() {
            repository.insert(List.of(INPUT));

            final LocalDate validityPeriodStart = testRepository.findValidityPeriodStartDate();
            Assertions.assertThat(validityPeriodStart)
                    .as(ROUTE_.VALIDITY_START.getName())
                    .isEqualTo(VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE);
        }

        @Test
        @DisplayName("Should save a new route with the correct validity period end time")
        void shouldSaveNewRouteWithCorrectValidityPeriodEndTime() {
            repository.insert(List.of(INPUT));

            final LocalDate validityPeriodEnd = testRepository.findValidityPeriodEndDate();
            Assertions.assertThat(validityPeriodEnd)
                    .as(ROUTE_.VALIDITY_END.getName())
                    .isEqualTo(VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE);
        }
    }
}
