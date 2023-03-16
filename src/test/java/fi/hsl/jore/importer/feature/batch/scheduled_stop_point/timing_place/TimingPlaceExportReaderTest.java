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
    @DisplayName("When scheduled_stop_points source table is empty")
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
    @DisplayName("When scheduled_stop_points source table has one scheduled stop point")
    @Sql(scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_infrastructure_nodes.sql",
            "/sql/importer/populate_scheduled_stop_points.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableHasOneScheduledStopPoint {

        private static final String EXPECTED_HASTUS_PLACE_ID = "1KALA";

        @Test
        @DisplayName("Should return the Hastus place ID of the only existing scheduled stop point")
        void shouldReturnCorrectHastusPlaceId(final SoftAssertions softAssertions) throws Exception {
            final ImporterTimingPlace found = reader.read();
            assertThat(found).isNotNull();

            softAssertions.assertThat(found.hastusPlaceId())
                          .as("hastusPlaceId")
                          .contains(EXPECTED_HASTUS_PLACE_ID);
        }
    }

    @Nested
    @DisplayName("When scheduled_stop_points source table has two scheduled stop points")
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableHasTwoScheduledStopPoints {

        @Nested
        @DisplayName("...with distinct Hastus places")
        @Sql(scripts = {
                "/sql/importer/drop_tables.sql",
                "/sql/importer/populate_infrastructure_nodes.sql",
                "/sql/importer/populate_scheduled_stop_points_with_same_short_id.sql"
        })
        class WithDistinctHastusPlaces {

            private static final String EXPECTED_HASTUS_PLACE_ID_1 = "1ELIEL";
            private static final String EXPECTED_HASTUS_PLACE_ID_2 = "1KALA";

            @Test
            @DisplayName("The first invocation of the read() method must return the first Hastus place ID in alphabetical order")
            void firstInvocationOfReadMethodMustReturnFirstHastusPlaceIdInAlphabeticalOrder(
                    final SoftAssertions softAssertions) throws Exception {

                final ImporterTimingPlace found = reader.read();
                assertThat(found).isNotNull();

                softAssertions.assertThat(found.hastusPlaceId())
                              .as("hastusPlaceId")
                              .contains(EXPECTED_HASTUS_PLACE_ID_1);
            }

            @Test
            @DisplayName("The second invocation of the read() method must return the second Hastus place ID in alphabetical order")
            void secondInvocationOfReadMethodMustReturnSecondHastusPlaceIdInAlphabeticalOrder(
                    final SoftAssertions softAssertions) throws Exception {

                final ImporterTimingPlace first = reader.read();
                assertThat(first).isNotNull();

                final ImporterTimingPlace second = reader.read();
                assertThat(second).isNotNull();

                softAssertions.assertThat(second.hastusPlaceId())
                              .as("hastusPlaceId")
                              .contains(EXPECTED_HASTUS_PLACE_ID_2);
            }
        }

        @Nested
        @DisplayName("...with common shared Hastus place")
        @Sql(scripts = {
                "/sql/importer/drop_tables.sql",
                "/sql/importer/populate_infrastructure_nodes.sql",
                "/sql/importer/populate_scheduled_stop_points_with_same_hastus_place_id.sql"
        })
        class WithCommonSharedHastusPlace {

            private static final String EXPECTED_HASTUS_PLACE_ID = "1ELIEL";

            @Test
            @DisplayName("The first invocation of the read() method must return the only existing Hastus place ID")
            void firstInvocationOfReadMethodMustReturnOnlyExistingHastusPlaceId(final SoftAssertions softAssertions)
                    throws Exception {

                final ImporterTimingPlace found = reader.read();
                assertThat(found).isNotNull();

                softAssertions.assertThat(found.hastusPlaceId())
                              .as("hastusPlaceId")
                              .contains(EXPECTED_HASTUS_PLACE_ID);
            }

            @Test
            @DisplayName("The second invocation of the read() method must return null")
            void secondInvocationOfReadMethodMustReturnNull() throws Exception {
                final ImporterTimingPlace first = reader.read();
                assertThat(first).isNotNull();

                final ImporterTimingPlace second = reader.read();
                assertThat(second).isNull();
            }
        }
    }
}
