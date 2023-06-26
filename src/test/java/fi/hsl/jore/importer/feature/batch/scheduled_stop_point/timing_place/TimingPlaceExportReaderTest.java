package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.timing_place.ImporterTimingPlace;
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
public class TimingPlaceExportReaderTest {

    private final JdbcCursorItemReader<ImporterTimingPlace> reader;

    @Autowired
    TimingPlaceExportReaderTest(final TimingPlaceExportReader reader) {
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
    @DisplayName("When network_places source table is empty")
    @Sql(scripts = "/sql/importer/drop_tables.sql")
    class WhenSourceTableIsEmpty {

        @Test
        @DisplayName("Should return null because no scheduled stop points exist")
        void shouldReturnNull() throws Exception {
            final ImporterTimingPlace found = reader.read();
            assertThat(found).isNull();
        }
    }

    @Nested
    @DisplayName("When network_places source table is not empty")
    @Sql(scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_places.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableIsNotEmpty {

        private static final String EXPECTED_TIMING_PLACE_LABEL_1 = "1ELIEL";
        private static final String EXPECTED_TIMING_PLACE_LABEL_2 = "1KALA";

        @Test
        @DisplayName("The first invocation of the read() method must return the first timing place label in alphabetical order")
        void firstInvocationOfReadMethodMustReturnFirstTimingPlaceLabelInAlphabeticalOrder(
                final SoftAssertions softAssertions) throws Exception {

            final ImporterTimingPlace found = reader.read();
            assertThat(found).isNotNull();

            softAssertions.assertThat(found.timingPlaceLabel())
                    .as("timingPlaceLabel")
                    .contains(EXPECTED_TIMING_PLACE_LABEL_1);
        }

        @Test
        @DisplayName("The second invocation of the read() method must return the second timing place label in alphabetical order")
        void secondInvocationOfReadMethodMustReturnSecondTimingPlaceLabelInAlphabeticalOrder(
                final SoftAssertions softAssertions) throws Exception {

            final ImporterTimingPlace first = reader.read();
            assertThat(first).isNotNull();

            final ImporterTimingPlace second = reader.read();
            assertThat(second).isNotNull();

            softAssertions.assertThat(second.timingPlaceLabel())
                    .as("timingPlaceLabel")
                    .contains(EXPECTED_TIMING_PLACE_LABEL_2);
        }
    }
}
