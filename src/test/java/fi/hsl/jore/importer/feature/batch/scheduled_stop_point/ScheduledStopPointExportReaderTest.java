package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ExportableScheduledStopPoint;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntTest
class ScheduledStopPointExportReaderTest {

    private final JdbcCursorItemReader<ExportableScheduledStopPoint> reader;

    @Autowired
    ScheduledStopPointExportReaderTest(ScheduledStopPointExportReader reader) {
        this.reader = reader.build();
    }

    @BeforeEach
    void openReader() {
        this.reader.open(new ExecutionContext());
    }

    @AfterEach
    void closeReader() {
        this.reader.close();
    }

    @Nested
    @DisplayName("When the source table is empty")
    @Sql(scripts = "/sql/destination/drop_tables.sql")
    class WhenSourceTableIsEmpty {

        @Test
        @DisplayName("The first invocation of the read() method must return null")
        void firstInvocationOfReadMethodMustReturnNull() throws Exception {
            final ExportableScheduledStopPoint found = reader.read();
            assertThat(found).isNull();
        }
    }

    @Nested
    @DisplayName("When the source table has one scheduled stop point")
    @Sql(scripts = {
            "/sql/destination/drop_tables.sql",
            "/sql/destination/populate_infrastructure_nodes.sql",
            "/sql/destination/populate_scheduled_stop_points.sql",
            "/sql/destination/populate_scheduled_stop_points_staging.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableHasOneScheduledStopPoint {

        private static final String EXPECTED_ELY_NUMBER = "1234567890";
        private static final String EXPECTED_EXTERNAL_ID = "c";
        private static final double EXPECTED_X_COORDINATE = 6;
        private static final double EXPECTED_Y_COORDINATE = 5;
        private static final String EXPECTED_FINNISH_NAME = "Yliopisto vanha";
        private static final String EXPECTED_SWEDISH_NAME = "Universitetet gamla";

        private static final String LOCALE_FINNISH = "fi_FI";
        private static final String LOCALE_SWEDISH = "sv_SE";

        @Test
        @DisplayName("The first invocation of the read() method must return the found scheduled stop point")
        void firstInvocationOfReadMethodMustReturnFoundScheduledStopPoint(final SoftAssertions softAssertions) throws Exception {
            final ExportableScheduledStopPoint found = reader.read();

            softAssertions.assertThat(found.externalId().value())
                    .as("externalId")
                    .isEqualTo(EXPECTED_EXTERNAL_ID);

            softAssertions.assertThat(found.elyNumber().get())
                    .as("elyNumber")
                    .isEqualTo(EXPECTED_ELY_NUMBER);

            final double XCoordinate = found.location().getX();
            softAssertions.assertThat(XCoordinate)
                    .as("X coordinate")
                    .isEqualTo(EXPECTED_X_COORDINATE);

            final double YCoordinate = found.location().getY();
            softAssertions.assertThat(YCoordinate)
                    .as("Y coordinate")
                    .isEqualTo(EXPECTED_Y_COORDINATE);

            final String finnishName = found.name().values().getOrElse(LOCALE_FINNISH, "null");
            softAssertions.assertThat(finnishName)
                    .as("finnishName")
                    .isEqualTo(EXPECTED_FINNISH_NAME);

            final String swedishName = found.name().values().getOrElse(LOCALE_SWEDISH, "null");
            softAssertions.assertThat(swedishName)
                    .as("swedishName")
                    .isEqualTo(EXPECTED_SWEDISH_NAME);
        }

        @Test
        @DisplayName("The second invocation of the read() method must return null")
        void secondInvocationOfReadMethodMustReturnNull() throws Exception {
            //The first invocation returns the scheduled stop found from the database.
            final ExportableScheduledStopPoint first = reader.read();
            assertThat(first).isNotNull();

            //Because there are no more scheduled stop points, this invocation must return null.
            final ExportableScheduledStopPoint second = reader.read();
            assertThat(second).isNull();
        }
    }
}
