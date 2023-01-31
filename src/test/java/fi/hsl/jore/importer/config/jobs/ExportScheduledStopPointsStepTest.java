package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import fi.hsl.jore.importer.feature.jore4.repository.Jore4ValidityPeriodTestRepository;
import fi.hsl.jore.importer.feature.jore4.repository.ScheduledStopPointTestLocation;
import fi.hsl.jore.importer.feature.jore4.repository.ValidityPeriodTargetTable;
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;
import java.time.LocalDate;

import static fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection.BACKWARD;
import static fi.hsl.jore.jore4.jooq.service_pattern.Tables.VEHICLE_MODE_ON_SCHEDULED_STOP_POINT;
import static org.assertj.db.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/importer/drop_tables.sql",
        "/sql/importer/populate_infrastructure_nodes.sql",
        "/sql/importer/populate_scheduled_stop_points.sql"
})
@Sql(
        scripts = {
                "/sql/jore4/drop_tables.sql",
                "/sql/jore4/populate_infrastructure_links.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
@ExtendWith(SoftAssertionsExtension.class)
class ExportScheduledStopPointsStepTest extends BatchIntegrationTest {

    private static final Jore4ScheduledStopPointDirection DIRECTION_ON_INFRALINK = BACKWARD;
    private static final String EXPECTED_INFRASTRUCTURE_LINK_ID = "554c63e6-87b2-4dc8-a032-b6b0e2607696";
    private static final String LABEL = "H1234";
    private static final int EXPECTED_PRIORITY = 10;

    private static final LocalDate EXPECTED_VALIDITY_PERIOD_START = LocalDate.of(1990, 1, 1);
    private static final LocalDate EXPECTED_VALIDITY_PERIOD_END = LocalDate.of(2051, 1, 1);
    private static final double X_COORDINATE = 6.0;
    private static final double Y_COORDINATE = 5.0;

    private static final String EXPECTED_SCHEDULED_STOP_POINT_VEHICLE_MODE = VehicleMode.BUS.getValue();

    private static final List<String> STEPS = List.of("exportScheduledStopPointsStep");

    private static final fi.hsl.jore.importer.jooq.network.tables.ScheduledStopPoints IMPORTER_SCHEDULED_STOP_POINT = fi.hsl.jore.importer.jooq.network.Tables.SCHEDULED_STOP_POINTS;
    private static final fi.hsl.jore.jore4.jooq.service_pattern.tables.ScheduledStopPoint JORE4_SCHEDULED_STOP_POINT = fi.hsl.jore.jore4.jooq.service_pattern.Tables.SCHEDULED_STOP_POINT;


    private final JdbcTemplate jdbcTemplate;
    private final Table importerTargetTable;
    private final Table jore4ScheduledStopPointTargetTable;
    private final Table jore4VehicleModeTargetTable;
    private final Jore4ValidityPeriodTestRepository testRepository;

    @Autowired
    ExportScheduledStopPointsStepTest(final @Qualifier("importerDataSource") DataSource importerDataSource,
                                      final @Qualifier("jore4DataSource") DataSource jore4DataSource) {
        this.jdbcTemplate = new JdbcTemplate(jore4DataSource);
        this.importerTargetTable = new Table(importerDataSource, "network.scheduled_stop_points");
        this.jore4ScheduledStopPointTargetTable = new Table(jore4DataSource, "service_pattern.scheduled_stop_point");
        this.jore4VehicleModeTargetTable = new Table(jore4DataSource, "service_pattern.vehicle_mode_on_scheduled_stop_point");
        this.testRepository = new Jore4ValidityPeriodTestRepository(jore4DataSource,
                ValidityPeriodTargetTable.SCHEDULED_STOP_POINT
        );
    }

    @Test
    @DisplayName("Should insert one scheduled stop point into the Jore 4 database")
    void shouldInsertOneScheduledStopPointToJore4Database() {
        runSteps(STEPS);

        assertThat(jore4ScheduledStopPointTargetTable).hasNumberOfRows(1);
    }

    @Test
    @DisplayName("Should generate a new id for the exported scheduled stop point")
    void shouldGenerateNewIdForInsertedScheduledStopPoint() {
        runSteps(STEPS);

        assertThat(jore4ScheduledStopPointTargetTable)
                .row()
                .value(JORE4_SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID.getName())
                .isNotNull();
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point with the correct direction")
    void shouldSaveExportedScheduledStopPointWithCorrectDirection() {
        runSteps(STEPS);

        assertThat(jore4ScheduledStopPointTargetTable)
                .row()
                .value(JORE4_SCHEDULED_STOP_POINT.DIRECTION.getName())
                .isEqualTo(DIRECTION_ON_INFRALINK.getValue());
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point with the correct infrastructure link id")
    void shouldSaveExportedScheduledStopPointWithCorrectInfrastructureLinkId() {
        runSteps(STEPS);

        assertThat(jore4ScheduledStopPointTargetTable)
                .row()
                .value(JORE4_SCHEDULED_STOP_POINT.LOCATED_ON_INFRASTRUCTURE_LINK_ID.getName())
                .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ID);
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point with the correct label")
    void shouldSaveExportedScheduledStopPointWithCorrectLabel() {
        runSteps(STEPS);

        assertThat(jore4ScheduledStopPointTargetTable)
                .row()
                .value(JORE4_SCHEDULED_STOP_POINT.LABEL.getName())
                .isEqualTo(LABEL);
    }

    @Test
    @DisplayName("Should save a new scheduled stop point with the correct measured location")
    void shouldSaveNewScheduledStopPointWithCorrectMeasuredLocation(SoftAssertions softAssertions) {
        runSteps(STEPS);

        //I used JDCBTemplate because there was no easy way to write
        //assertions for custom data types (such as geography) with AssertJ-DB.
        final ScheduledStopPointTestLocation measuredLocation = jdbcTemplate.query(
                ScheduledStopPointTestLocation.SQL_QUERY_GET_MEASURED_LOCATION,
                (resultSet, i) -> new ScheduledStopPointTestLocation(
                        resultSet.getDouble(ScheduledStopPointTestLocation.SQL_ALIAS_X_COORDINATE),
                        resultSet.getDouble(ScheduledStopPointTestLocation.SQL_ALIAS_Y_COORDINATE)
                )
        ).get(0);

        softAssertions.assertThat(measuredLocation.getX())
                .as("x")
                .isEqualTo(X_COORDINATE);
        softAssertions.assertThat(measuredLocation.getY())
                .as("y")
                .isEqualTo(Y_COORDINATE);
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point with the correct priority")
    void shouldSaveExportedScheduledStopPointWithCorrectPriority() {
        runSteps(STEPS);

        assertThat(jore4ScheduledStopPointTargetTable)
                .row()
                .value(JORE4_SCHEDULED_STOP_POINT.PRIORITY.getName())
                .isEqualTo(EXPECTED_PRIORITY);
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point with the correct validity period start time")
    void shouldSaveExportedScheduledStopPointWithCorrectValidityPeriodStartTime() {
        runSteps(STEPS);

        final LocalDate validityStart = testRepository.findValidityPeriodStartDate();
        Assertions.assertThat(validityStart)
                .as(JORE4_SCHEDULED_STOP_POINT.VALIDITY_START.getName())
                .isEqualTo(EXPECTED_VALIDITY_PERIOD_START);
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point with the correct validity period end time")
    void shouldSaveExportedScheduledStopPointWithCorrectValidityPeriodEndTime() {
        runSteps(STEPS);

        final LocalDate validityEnd = testRepository.findValidityPeriodEndDate();
        Assertions.assertThat(validityEnd)
                .as(JORE4_SCHEDULED_STOP_POINT.VALIDITY_END.getName())
                .isEqualTo(EXPECTED_VALIDITY_PERIOD_END);
    }

    @Test
    @DisplayName("Should update the Jore 4 id of the scheduled stop point found from the importer's database")
    void shouldUpdateJore4IdOfScheduledStopPointFoundFromImportersDatabase() {
        runSteps(STEPS);

        assertThat(importerTargetTable)
                .row()
                .value(IMPORTER_SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_JORE4_ID.getName())
                .isNotNull();
    }

    @Test
    @DisplayName("Should insert a new scheduled stop point vehicle mode into the Jore 4 database")
    void shouldInsertNewScheduledStopPointVehicleModeIntoJore4Database() {
        runSteps(STEPS);

        assertThat(jore4VehicleModeTargetTable).hasNumberOfRows(1);
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point vehicle mode with the generated scheduled stop point id")
    void shouldSaveExportedScheduledStopPointVehicleModeWithGeneratedScheduledStopPointId() {
        runSteps(STEPS);

        assertThat(jore4VehicleModeTargetTable)
                .row()
                .value(VEHICLE_MODE_ON_SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID.getName())
                .isNotNull();
    }

    @Test
    @DisplayName("Should save exported scheduled stop point vehicle with the correct vehicle mode")
    void shouldSaveExportedScheduledStopPointVehicleModeWithCorrectVehicleMode() {
        runSteps(STEPS);

        assertThat(jore4VehicleModeTargetTable)
                .row()
                .value(VEHICLE_MODE_ON_SCHEDULED_STOP_POINT.VEHICLE_MODE.getName())
                .isEqualTo(EXPECTED_SCHEDULED_STOP_POINT_VEHICLE_MODE);
    }
}
