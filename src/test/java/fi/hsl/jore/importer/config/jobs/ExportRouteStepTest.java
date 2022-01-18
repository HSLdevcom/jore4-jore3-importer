package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteDirection;
import fi.hsl.jore.importer.feature.transmodel.repository.TransmodelValidityPeriodTestRepository;
import fi.hsl.jore.importer.feature.transmodel.repository.ValidityPeriodTargetTable;
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static fi.hsl.jore.importer.feature.transmodel.util.TimestampFactory.offsetDateTimeFromLocalDateTime;
import static fi.hsl.jore.jore4.jooq.internal_route.Tables.ROUTE;
import static org.assertj.db.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/destination/drop_tables.sql",
        "/sql/destination/populate_infrastructure_nodes.sql",
        "/sql/destination/populate_lines_with_transmodel_ids.sql",
        "/sql/destination/populate_routes.sql",
        "/sql/destination/populate_route_directions.sql",
        "/sql/destination/populate_route_points_for_jore4_export.sql",
        "/sql/destination/populate_route_stop_points_for_jore4_export.sql",
        "/sql/destination/populate_scheduled_stop_points_for_jore4_export.sql"
})
@Sql(
        scripts = {
                "/sql/transmodel/drop_tables.sql",
                "/sql/transmodel/populate_vehicle_modes.sql",
                "/sql/transmodel/populate_infrastructure_links.sql",
                "/sql/transmodel/populate_lines.sql",
                "/sql/transmodel/populate_scheduled_stop_points.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
public class ExportRouteStepTest  extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("exportRoutesStep");

    private final String EXPECTED_DIRECTION = TransmodelRouteDirection.INBOUND.getValue();
    private final UUID EXPECTED_LINE_TRANSMODEL_ID = UUID.fromString("5aa7d9fc-2cf9-466d-8ac0-f442d60c261f");
    private static final String EXPECTED_LABEL = "1";
    private static final String EXPECTED_DESCRIPTION = "{\"fi_FI\":\"Keskustori - Etelä-Hervanta vanha\",\"sv_SE\":\"Central torget - Södra Hervanta gamla\"}";
    private static final int EXPECTED_PRIORITY = 10;

    private final UUID EXPECTED_START_SCHEDULED_STOP_POINT_TRANSMODEL_ID = UUID.fromString("45e83727-41fb-4e75-ad71-7e54d58f23ac");
    private final UUID EXPECTED_END_SCHEDULED_STOP_POINT_TRANSMODEL_ID = UUID.fromString("48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d");

    private static final OffsetDateTime VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                    LocalDate.of(2021, 10, 4),
                    OPERATING_DAY_START_TIME
            )
    );
    private static final OffsetDateTime VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                LocalDate.of(2022, 1, 1),
                OPERATING_DAY_END_TIME
            )
    );

    private static final fi.hsl.jore.importer.jooq.network.tables.NetworkRoutes IMPORTER_ROUTE = fi.hsl.jore.importer.jooq.network.Tables.NETWORK_ROUTES;
    private static final fi.hsl.jore.jore4.jooq.internal_route.tables.Route JORE4_ROUTE = fi.hsl.jore.jore4.jooq.internal_route.Tables.ROUTE;

    private final Table importerTargetTable;
    private final Table jore4TargetTable;
    private final TransmodelValidityPeriodTestRepository testRepository;

    @Autowired
    public ExportRouteStepTest(final @Qualifier("importerDataSource") DataSource importerDataSource,
                               final @Qualifier("jore4DataSource") DataSource jore4DataSource) {
        this.importerTargetTable = new Table(importerDataSource, "network.network_routes");
        this.jore4TargetTable = new Table(jore4DataSource, "internal_route.route");
        this.testRepository = new TransmodelValidityPeriodTestRepository(jore4DataSource,
                ValidityPeriodTargetTable.ROUTE
        );
    }

    @Test
    @DisplayName("Should insert one line into the Jore 4 database")
    void shouldInsertOneLineIntoJoreDatabase() {
        runSteps(STEPS);

        assertThat(jore4TargetTable).hasNumberOfRows(1);
    }

    @Test
    @DisplayName("Should generate a new id for the exported line")
    void shouldGenerateNewIdForExportedLine() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.ROUTE_ID.getName())
                .isNotNull();
    }

    @Test
    @DisplayName("Should save the exported route with the correct description")
    void shouldSaveExportLineWithCorrectDescription() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.DESCRIPTION_I18N.getName())
                .isEqualTo(EXPECTED_DESCRIPTION);
    }

    @Test
    @DisplayName("Should save the exported route with the correct direction")
    void shouldSaveExportedRouteWithCorrectDirection() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.DIRECTION.getName())
                .isEqualTo(EXPECTED_DIRECTION);
    }

    @Test
    @DisplayName("Should save the exported route with the correct label")
    void shouldSaveExportedRouteWithCorrectLabel() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.LABEL.getName())
                .isEqualTo(EXPECTED_LABEL);
    }

    @Test
    @DisplayName("Should save the exported route with the correct line id")
    void shouldSaveExportedRouteWithCorrectLineId() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.ON_LINE_ID.getName())
                .isEqualTo(EXPECTED_LINE_TRANSMODEL_ID);
    }

    @Test
    @DisplayName("Should save the exported route with the correct priority")
    void shouldSaveExportedRouteWithCorrectPriority() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.PRIORITY.getName())
                .isEqualTo(EXPECTED_PRIORITY);
    }

    @Test
    @DisplayName("Should save the exported route with the correct start scheduled stop point id")
    void shouldSaveExportedRouteWithCorrectStartScheduledStopPointId() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.STARTS_FROM_SCHEDULED_STOP_POINT_ID.getName())
                .isEqualTo(EXPECTED_START_SCHEDULED_STOP_POINT_TRANSMODEL_ID);
    }

    @Test
    @DisplayName("Should save the exported route with the correct end scheduled stop point id")
    void shouldSaveExportedRouteWithCorrectEndScheduledStopPointId() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.ENDS_AT_SCHEDULED_STOP_POINT_ID.getName())
                .isEqualTo(EXPECTED_END_SCHEDULED_STOP_POINT_TRANSMODEL_ID);
    }

    @Test
    @DisplayName("Should save the exported route with the correct validity period start time")
    void shouldSaveExportedRouteWithCorrectValidityPeriodStartTime() {
        runSteps(STEPS);

        final OffsetDateTime validityPeriodStart = testRepository.findValidityPeriodStartTimestampAtFinnishTimeZone();
        Assertions.assertThat(validityPeriodStart)
                .as(ROUTE.VALIDITY_START.getName())
                .isEqualTo(VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE);
    }

    @Test
    @DisplayName("Should save the exported route with the correct validity period end time")
    void shouldSaveExportedRouteWithCorrectValidityPeriodEndTime() {
        runSteps(STEPS);

        final OffsetDateTime validityPeriodEnd = testRepository.findValidityPeriodEndTimestampAtFinnishTimeZone();
        Assertions.assertThat(validityPeriodEnd)
                .as(ROUTE.VALIDITY_END.getName())
                .isEqualTo(VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE);
    }

    @Test
    @DisplayName("Should update the transmodel id of the route found from the importer's database")
    void shouldUpdateTransmodelIdOfRouteFoundFromImportersDatabase() {
        runSteps(STEPS);

        assertThat(importerTargetTable)
                .row()
                .value(IMPORTER_ROUTE.NETWORK_ROUTE_TRANSMODEL_ID.getName())
                .isNotNull();
    }
}