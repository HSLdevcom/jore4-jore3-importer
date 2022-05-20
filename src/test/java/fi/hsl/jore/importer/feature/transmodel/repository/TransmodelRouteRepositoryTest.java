package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.IntTest;
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
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil.createMultilingualString;
import static fi.hsl.jore.importer.feature.transmodel.util.TimestampFactory.offsetDateTimeFromLocalDateTime;
import static fi.hsl.jore.jore4.jooq.internal_route.Tables.ROUTE;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class TransmodelRouteRepositoryTest {

    private static final UUID ROUTE_ID = UUID.fromString("ed878662-dd94-4cad-90a6-597ee10ebd42");
    private static final String FINNISH_NAME = "Keskustori - Etelä-Hervanta";
    private static final String SWEDISH_NAME = "Central torget - Södra Hervanta";
    private static final String LABEL = "30";
    private static final UUID LINE_ID = UUID.fromString("5aa7d9fc-2cf9-466d-8ac0-f442d60c261f");
    private static final int PRIORITY = 10;
    private static final TransmodelRouteDirection ROUTE_DIRECTION = TransmodelRouteDirection.INBOUND;
    private static final UUID ROUTE_DIRECTION_ID = UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");
    private static final UUID START_SCHEDULED_STOP_POINT_ID = UUID.fromString("45e83727-41fb-4e75-ad71-7e54d58f23ac");
    private static final UUID END_SCHEDULED_STOP_POINT_ID = UUID.fromString("48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d");

    private static final OffsetDateTime VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                    LocalDate.of(2021, 1, 1),
                    OPERATING_DAY_START_TIME
            )
    );
    private static final OffsetDateTime VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                    LocalDate.of(2022, 1, 1),
                    OPERATING_DAY_END_TIME
            )
    );

    private final TransmodelRouteRepository repository;
    private final Table targetTable;
    private final TransmodelValidityPeriodTestRepository testRepository;

    @Autowired
    TransmodelRouteRepositoryTest(final TransmodelRouteRepository repository,
                                 @Qualifier("jore4DataSource") final DataSource targetDataSource) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "internal_route.route");
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
                LINE_ID,
                PRIORITY,
                START_SCHEDULED_STOP_POINT_ID,
                END_SCHEDULED_STOP_POINT_ID,
                Optional.of(VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE),
                Optional.of(VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE)
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
                    .value(ROUTE.ROUTE_ID.getName())
                    .isEqualTo(ROUTE_ID);
        }

        @Test
        @DisplayName("Should save a new route with the correct description")
        void shouldSaveNewRouteWithCorrectDescription() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE.DESCRIPTION_I18N.getName())
                    .isEqualTo(EXPECTED_DESCRIPTION);
        }

        @Test
        @DisplayName("Should save a new route with the correct direction")
        void shouldSaveNewRouteWithCorrectDirection() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE.DIRECTION.getName())
                    .isEqualTo(ROUTE_DIRECTION.getValue());
        }

        @Test
        @DisplayName("Should save a new route with the correct label")
        void shouldSaveNewRouteWithCorrectLabel() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE.LABEL.getName())
                    .isEqualTo(LABEL);
        }

        @Test
        @DisplayName("Should save a new route with the correct line id")
        void shouldSaveNewRouteWithCorrectLineId() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE.ON_LINE_ID.getName())
                    .isEqualTo(LINE_ID);
        }

        @Test
        @DisplayName("Should save a new route with the correct priority")
        void shouldSaveNewRouteWithCorrectPriority() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE.PRIORITY.getName())
                    .isEqualTo(PRIORITY);
        }

        @Test
        @DisplayName("Should save a new route with the correct start scheduled stop point id")
        void shouldSaveNewRouteWithCorrectStartScheduledStopPointId() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE.STARTS_FROM_SCHEDULED_STOP_POINT_ID.getName())
                    .isEqualTo(START_SCHEDULED_STOP_POINT_ID);
        }

        @Test
        @DisplayName("Should save a new route with the correct end scheduled stop point id")
        void shouldSaveNewRouteWithCorrectEndScheduledStopPointId() {
            repository.insert(List.of(INPUT));
            assertThat(targetTable)
                    .row()
                    .value(ROUTE.ENDS_AT_SCHEDULED_STOP_POINT_ID.getName())
                    .isEqualTo(END_SCHEDULED_STOP_POINT_ID);
        }

        @Test
        @DisplayName("Should save a new route with the correct validity period start time")
        void shouldSaveNewRouteWithCorrectValidityPeriodStartTime() {
            repository.insert(List.of(INPUT));

            final OffsetDateTime validityPeriodStart = testRepository.findValidityPeriodStartTimestampAtFinnishTimeZone();
            Assertions.assertThat(validityPeriodStart)
                    .as(ROUTE.VALIDITY_START.getName())
                    .isEqualTo(VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE);
        }

        @Test
        @DisplayName("Should save a new route with the correct validity period end time")
        void shouldSaveNewRouteWithCorrectValidityPeriodEndTime() {
            repository.insert(List.of(INPUT));

            final OffsetDateTime validityPeriodEnd = testRepository.findValidityPeriodEndTimestampAtFinnishTimeZone();
            Assertions.assertThat(validityPeriodEnd)
                    .as(ROUTE.VALIDITY_END.getName())
                    .isEqualTo(VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE);
        }
    }
}
