package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPointDirection;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.Disabled;
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
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static fi.hsl.jore.importer.feature.transmodel.util.TimestampFactory.offsetDateTimeFromLocalDateTime;
import static fi.hsl.jore.jore4.jooq.internal_service_pattern.Tables.SCHEDULED_STOP_POINT;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
@Disabled("This test cannot pass because scheduled stop point and its vehicle modes aren't inserted in same transaction")
class TransmodelScheduledStopPointRepositoryTest {

    private static final UUID SCHEDULED_STOP_POINT_ID = UUID.fromString("0259d692-7ee0-4792-b769-1141b248d102");
    private static final String SCHEDULED_STOP_POINT_EXTERNAL_ID = "1234567";
    private static final TransmodelScheduledStopPointDirection DIRECTION_ON_INFRALINK = TransmodelScheduledStopPointDirection.FORWARD;
    private static final String INFRASTRUCTURE_LINK_EXTERNAL_ID = "133202";
    private static final String EXPECTED_INFRASTRUCTURE_LINK_ID = "554c63e6-87b2-4dc8-a032-b6b0e2607696";
    private static final String LABEL = "Ullanmäki";
    private static final double X_COORDINATE = 25.696376131;
    private static final double Y_COORDINATE = 61.207149801;
    private static final int PRIORITY = 10;

    private static final OffsetDateTime VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                    LocalDate.of(1990, 1, 1),
                    OPERATING_DAY_START_TIME
            )
    );
    private static final OffsetDateTime VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                    LocalDate.of(2051, 1, 1),
                    OPERATING_DAY_END_TIME
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final TransmodelScheduledStopPointRepository repository;
    private final Table targetTable;
    private final TransmodelValidityPeriodTestRepository testRepository;

    @Autowired
    TransmodelScheduledStopPointRepositoryTest(final TransmodelScheduledStopPointRepository repository,
                                               @Qualifier("jore4DataSource") final DataSource targetDataSource) {
        this.jdbcTemplate = new JdbcTemplate(targetDataSource);
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "internal_service_pattern.scheduled_stop_point");
        this.testRepository = new TransmodelValidityPeriodTestRepository(targetDataSource,
                ValidityPeriodTargetTable.SCHEDULED_STOP_POINT
        );
    }

    @Nested
    @DisplayName("Insert scheduled stop point into the database")
    @Sql(
            scripts = {
                    "/sql/transmodel/drop_tables.sql",
                    "/sql/transmodel/populate_vehicle_modes.sql",
                    "/sql/transmodel/populate_infrastructure_links.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    @ExtendWith(SoftAssertionsExtension.class)
    class InsertScheduledStopPointIntoDatabase {

        private final TransmodelScheduledStopPoint INPUT = TransmodelScheduledStopPoint.of(
                SCHEDULED_STOP_POINT_ID,
                SCHEDULED_STOP_POINT_EXTERNAL_ID,
                INFRASTRUCTURE_LINK_EXTERNAL_ID,
                DIRECTION_ON_INFRALINK,
                LABEL,
                JoreGeometryUtil.fromDbCoordinates(Y_COORDINATE, X_COORDINATE),
                PRIORITY,
                Optional.of(VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE),
                Optional.of(VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE)
        );

        @Test
        @DisplayName("Should insert a new scheduled stop point into the database")
        void shouldInsertNewScheduledStopPointIntoDatabase() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct id")
        void shouldSaveNewScheduledStopPointWithCorrectId() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID.getName())
                    .isEqualTo(SCHEDULED_STOP_POINT_ID);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct direction")
        void shouldSaveNewScheduledStopPointWithCorrectDirection() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.DIRECTION.getName())
                    .isEqualTo(DIRECTION_ON_INFRALINK.getValue());
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct infrastructure link id")
        void shouldSaveNewScheduledStopPointWithCorrectInfrastructureLinkId() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.LOCATED_ON_INFRASTRUCTURE_LINK_ID.getName())
                    .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ID);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct label")
        void shouldSaveNewScheduledStopPointWithCorrectLabel() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
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

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.PRIORITY.getName())
                    .isEqualTo(PRIORITY);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct validity period start time")
        void shouldSaveNewScheduledStopPointWithCorrectValidityPeriodStartTime() {
            repository.insert(List.of(INPUT));

            final OffsetDateTime validityStart = testRepository.findValidityPeriodStartTimestampAtFinnishTimeZone();
            Assertions.assertThat(validityStart)
                    .as(SCHEDULED_STOP_POINT.VALIDITY_START.getName())
                    .isEqualTo(VALIDITY_PERIOD_START_TIME_AT_FINNISH_TIME_ZONE);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct validity period end time")
        void shouldSaveNewScheduledStopPointWithCorrectValidityPeriodEndTime() {
            repository.insert(List.of(INPUT));

            final OffsetDateTime validityEnd = testRepository.findValidityPeriodEndTimestampAtFinnishTimeZone();
            Assertions.assertThat(validityEnd)
                    .as(SCHEDULED_STOP_POINT.VALIDITY_END.getName())
                    .isEqualTo(VALIDITY_PERIOD_END_TIME_AT_FINNISH_TIME_ZONE);
        }
    }
}
