package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.service_pattern.Tables.SCHEDULED_STOP_POINT;
import static fi.hsl.jore.jore4.jooq.service_pattern.Tables.VEHICLE_MODE_ON_SCHEDULED_STOP_POINT;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class Jore4ScheduledStopPointRepositoryTest {

    private static final UUID SCHEDULED_STOP_POINT_ID = UUID.fromString("0259d692-7ee0-4792-b769-1141b248d102");
    private static final String SCHEDULED_STOP_POINT_EXTERNAL_ID = "1234567";
    private static final Jore4ScheduledStopPointDirection DIRECTION_ON_INFRALINK = Jore4ScheduledStopPointDirection.FORWARD;
    private static final String INFRASTRUCTURE_LINK_EXTERNAL_ID = "133202";
    private static final String EXPECTED_INFRASTRUCTURE_LINK_ID = "554c63e6-87b2-4dc8-a032-b6b0e2607696";
    private static final String LABEL = "Ullanmäki";
    private static final double X_COORDINATE = 25.696376131;
    private static final double Y_COORDINATE = 61.207149801;
    private static final int PRIORITY = 10;

    private static final LocalDate VALIDITY_PERIOD_START = LocalDate.of(1990, 1, 1);
    private static final LocalDate VALIDITY_PERIOD_END = LocalDate.of(2051, 1, 1);

    private static final String SCHEDULED_STOP_POINT_VEHICLE_MODE = VehicleMode.BUS.getValue();

    private final JdbcTemplate jdbcTemplate;
    private final Jore4ScheduledStopPointRepository repository;
    private final Table scheduledStopPointTargetTable;
    private final Jore4ValidityPeriodTestRepository testRepository;
    private final Table vehicleModeTargetTable;

    @Autowired
    Jore4ScheduledStopPointRepositoryTest(final Jore4ScheduledStopPointRepository repository,
                                          @Qualifier("jore4DataSource") final DataSource targetDataSource) {
        this.jdbcTemplate = new JdbcTemplate(targetDataSource);
        this.repository = repository;
        this.scheduledStopPointTargetTable = new Table(targetDataSource, "service_pattern.scheduled_stop_point");
        this.testRepository = new Jore4ValidityPeriodTestRepository(targetDataSource,
                ValidityPeriodTargetTable.SCHEDULED_STOP_POINT
        );
        this.vehicleModeTargetTable = new Table(targetDataSource, "service_pattern.vehicle_mode_on_scheduled_stop_point");
    }

    @Nested
    @DisplayName("Insert scheduled stop point into the database")
    @Sql(
            scripts = {
                    "/sql/transmodel/drop_tables.sql",
                    "/sql/transmodel/populate_infrastructure_links.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    @ExtendWith(SoftAssertionsExtension.class)
    class InsertScheduledStopPointIntoDatabase {

        private final Jore4ScheduledStopPoint INPUT = Jore4ScheduledStopPoint.of(
                SCHEDULED_STOP_POINT_ID,
                SCHEDULED_STOP_POINT_EXTERNAL_ID,
                INFRASTRUCTURE_LINK_EXTERNAL_ID,
                DIRECTION_ON_INFRALINK,
                LABEL,
                JoreGeometryUtil.fromDbCoordinates(Y_COORDINATE, X_COORDINATE),
                PRIORITY,
                Optional.of(VALIDITY_PERIOD_START),
                Optional.of(VALIDITY_PERIOD_END)
        );

        @Test
        @DisplayName("Should insert a new scheduled stop point into the database")
        void shouldInsertNewScheduledStopPointIntoDatabase() {
            repository.insert(List.of(INPUT));

            assertThat(scheduledStopPointTargetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct id")
        void shouldSaveNewScheduledStopPointWithCorrectId() {
            repository.insert(List.of(INPUT));

            assertThat(scheduledStopPointTargetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID.getName())
                    .isEqualTo(SCHEDULED_STOP_POINT_ID);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct direction")
        void shouldSaveNewScheduledStopPointWithCorrectDirection() {
            repository.insert(List.of(INPUT));

            assertThat(scheduledStopPointTargetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.DIRECTION.getName())
                    .isEqualTo(DIRECTION_ON_INFRALINK.getValue());
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct infrastructure link id")
        void shouldSaveNewScheduledStopPointWithCorrectInfrastructureLinkId() {
            repository.insert(List.of(INPUT));

            assertThat(scheduledStopPointTargetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.LOCATED_ON_INFRASTRUCTURE_LINK_ID.getName())
                    .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ID);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct label")
        void shouldSaveNewScheduledStopPointWithCorrectLabel() {
            repository.insert(List.of(INPUT));

            assertThat(scheduledStopPointTargetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.LABEL.getName())
                    .isEqualTo(LABEL);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct measured location")
        void shouldSaveNewScheduledStopPointWithCorrectMeasuredLocation(SoftAssertions softAssertions) {
            repository.insert(List.of(INPUT));

            //I used JDCBTemplate because there was no easy way to write
            //assertions for custom data types (such as geography) with AssertJ-DB.
            ScheduledStopPointTestLocation measuredLocation = jdbcTemplate.query(
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
        @DisplayName("Should save a new scheduled stop point with the correct priority")
        void shouldSaveNewScheduledStopPointWithCorrectPriority() {
            repository.insert(List.of(INPUT));

            assertThat(scheduledStopPointTargetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.PRIORITY.getName())
                    .isEqualTo(PRIORITY);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct validity period start time")
        void shouldSaveNewScheduledStopPointWithCorrectValidityPeriodStartTime() {
            repository.insert(List.of(INPUT));

            final LocalDate validityStart = testRepository.findValidityPeriodStartDate();
            Assertions.assertThat(validityStart)
                    .as(SCHEDULED_STOP_POINT.VALIDITY_START.getName())
                    .isEqualTo(VALIDITY_PERIOD_START);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct validity period end time")
        void shouldSaveNewScheduledStopPointWithCorrectValidityPeriodEndTime() {
            repository.insert(List.of(INPUT));

            final LocalDate validityEnd = testRepository.findValidityPeriodEndDate();
            Assertions.assertThat(validityEnd)
                    .as(SCHEDULED_STOP_POINT.VALIDITY_END.getName())
                    .isEqualTo(VALIDITY_PERIOD_END);
        }

        @Test
        @DisplayName("Should insert a new scheduled stop point vehicle mode into the database")
        void shouldInsertNewScheduledStopPointVehicleModeIntoDatabase() {
            repository.insert(List.of(INPUT));

            assertThat(vehicleModeTargetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point vehicle mode with the correct scheduled stop point id")
        void shouldSaveNewScheduledStopPointVehicleModeWithCorrectScheduledStopPointId() {
            repository.insert(List.of(INPUT));

            assertThat(vehicleModeTargetTable)
                    .row()
                    .value(VEHICLE_MODE_ON_SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID.getName())
                    .isEqualTo(SCHEDULED_STOP_POINT_ID);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point vehicle with the correct vehicle mode")
        void shouldSaveNewScheduledStopPointVehicleModeWithCorrectVehicleMode() {
            repository.insert(List.of(INPUT));

            assertThat(vehicleModeTargetTable)
                    .row()
                    .value(VEHICLE_MODE_ON_SCHEDULED_STOP_POINT.VEHICLE_MODE.getName())
                    .isEqualTo(SCHEDULED_STOP_POINT_VEHICLE_MODE);
        }
    }
}
