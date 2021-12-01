package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPointDirection;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static fi.hsl.jore.jore4.jooq.internal_service_pattern.Tables.SCHEDULED_STOP_POINT;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class TransmodelScheduledStopPointRepositoryTest {

    private final String SCHEDULED_STOP_POINT_EXTERNAL_ID = "1234567";
    private final TransmodelScheduledStopPointDirection DIRECTION_ON_INFRALINK = TransmodelScheduledStopPointDirection.FORWARD;
    private final String INFRASTRUCTURE_LINK_EXTERNAL_ID = "133202";
    private final String EXPECTED_INFRASTRUCTURE_LINK_ID = "554c63e6-87b2-4dc8-a032-b6b0e2607696";
    private final String LABEL = "Ullanmäki";
    private final double X_COORDINATE = 25.696376131;
    private final double Y_COORDINATE = 61.207149801;
    private final int PRIORITY = 10;
    private static final LocalDateTime VALIDITY_PERIOD_START_TIME = LocalDateTime.of(
            LocalDate.of(1990, 1, 1),
            OPERATING_DAY_START_TIME
    );
    private static final LocalDateTime VALIDITY_PERIOD_END_TIME = LocalDateTime.of(
            LocalDate.of(2051, 1, 1),
            OPERATING_DAY_END_TIME
    );

    private final JdbcTemplate jdbcTemplate;
    private final TransmodelScheduledStopPointRepository repository;
    private final DataSource targetDataSource;
    private final Table targetTable;

    @Autowired
    TransmodelScheduledStopPointRepositoryTest(final TransmodelScheduledStopPointRepository repository,
                                               @Qualifier("jore4DataSource") final DataSource targetDataSource) {
        this.jdbcTemplate = new JdbcTemplate(targetDataSource);
        this.repository = repository;
        this.targetDataSource = targetDataSource;
        this.targetTable = new Table(targetDataSource, "internal_service_pattern.scheduled_stop_point");
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

        private final TransmodelScheduledStopPoint INPUT = TransmodelScheduledStopPoint.of(
                SCHEDULED_STOP_POINT_EXTERNAL_ID,
                INFRASTRUCTURE_LINK_EXTERNAL_ID,
                DIRECTION_ON_INFRALINK,
                LABEL,
                JoreGeometryUtil.fromDbCoordinates(Y_COORDINATE, X_COORDINATE),
                PRIORITY,
                Optional.of(VALIDITY_PERIOD_START_TIME),
                Optional.of(VALIDITY_PERIOD_END_TIME)
        );

        @Test
        @DisplayName("Should insert a new scheduled stop point into the database")
        void shouldInsertNewScheduledStopPointIntoDatabase() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should generate a new id for the inserted scheduled stop point")
        void shouldGenerateNewIdForInsertedScheduledStopPoint() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID.getName())
                    .isNotNull();
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

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.VALIDITY_START.getName())
                    .isEqualTo(VALIDITY_PERIOD_START_TIME);
        }

        @Test
        @DisplayName("Should save a new scheduled stop point with the correct validity period end time")
        void shouldSaveNewScheduledStopPointWithCorrectValidityPeriodEndTime() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT.VALIDITY_END.getName())
                    .isEqualTo(VALIDITY_PERIOD_END_TIME);
        }
    }
}
