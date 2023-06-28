package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImporterScheduledStopPoint;
import io.vavr.collection.List;
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

    private final JdbcCursorItemReader<ImporterScheduledStopPoint> reader;

    @Autowired
    ScheduledStopPointExportReaderTest(final ScheduledStopPointExportReader reader) {
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
    @Sql(scripts = "/sql/importer/drop_tables.sql")
    class WhenSourceTableIsEmpty {

        @Test
        @DisplayName("The first invocation of the read() method must return null")
        void firstInvocationOfReadMethodMustReturnNull() throws Exception {
            final ImporterScheduledStopPoint found = reader.read();
            assertThat(found).isNull();
        }
    }

    @Nested
    @DisplayName("When the source table has one scheduled stop point")
    @Sql(scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_infrastructure_nodes.sql",
            "/sql/importer/populate_places.sql",
            "/sql/importer/populate_scheduled_stop_points.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableHasOneScheduledStopPoint {

        private static final long EXPECTED_ELY_NUMBER = 1234567890L;
        private static final String EXPECTED_EXTERNAL_ID = "c";
        private static final double EXPECTED_X_COORDINATE = 6;
        private static final double EXPECTED_Y_COORDINATE = 5;
        private static final String EXPECTED_FINNISH_NAME = "Yliopisto vanha";
        private static final String EXPECTED_SWEDISH_NAME = "Universitetet gamla";
        private static final String EXPECTED_PLACE_EXTERNAL_ID = "1KALA";

        @Test
        @DisplayName("The first invocation of the read() method must return the found scheduled stop point")
        void firstInvocationOfReadMethodMustReturnFoundScheduledStopPoint(final SoftAssertions softAssertions) throws Exception {
            final ImporterScheduledStopPoint found = reader.read();

            final List<ExternalId> externalIds = found.externalIds();
            softAssertions.assertThat(externalIds)
                    .as("externalIdSize")
                    .hasSize(1);
            softAssertions.assertThat(externalIds)
                    .as("externalIds")
                    .containsExactly(ExternalId.of(EXPECTED_EXTERNAL_ID));

            final List<Long> elyNumbers = found.elyNumbers();
            softAssertions.assertThat(elyNumbers)
                    .as("elyNumberSize")
                    .hasSize(1);
            softAssertions.assertThat(elyNumbers)
                    .as("elyNumbers")
                    .containsExactly(EXPECTED_ELY_NUMBER);

            final double XCoordinate = found.location().getX();
            softAssertions.assertThat(XCoordinate)
                    .as("X coordinate")
                    .isEqualTo(EXPECTED_X_COORDINATE);

            final double YCoordinate = found.location().getY();
            softAssertions.assertThat(YCoordinate)
                    .as("Y coordinate")
                    .isEqualTo(EXPECTED_Y_COORDINATE);

            final String finnishName = JoreLocaleUtil.getI18nString(found.name(), JoreLocaleUtil.FINNISH);
            softAssertions.assertThat(finnishName)
                    .as("finnishName")
                    .isEqualTo(EXPECTED_FINNISH_NAME);

            final String swedishName = JoreLocaleUtil.getI18nString(found.name(), JoreLocaleUtil.SWEDISH);
            softAssertions.assertThat(swedishName)
                    .as("swedishName")
                    .isEqualTo(EXPECTED_SWEDISH_NAME);

            softAssertions.assertThat(found.placeExternalId())
                    .as("placeExternalId")
                    .contains(EXPECTED_PLACE_EXTERNAL_ID);
        }

        @Test
        @DisplayName("The second invocation of the read() method must return null")
        void secondInvocationOfReadMethodMustReturnNull() throws Exception {
            //The first invocation returns the scheduled stop found from the database.
            final ImporterScheduledStopPoint first = reader.read();
            assertThat(first).isNotNull();

            //Because there are no more scheduled stop points, this invocation must return null.
            final ImporterScheduledStopPoint second = reader.read();
            assertThat(second).isNull();
        }
    }

    @Nested
    @DisplayName("When the source table has two scheduled stop points with same short ID")
    @Sql(scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_infrastructure_nodes.sql",
            "/sql/importer/populate_places.sql",
            "/sql/importer/populate_scheduled_stop_points_with_same_short_id.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableHasTwoScheduledStopPointsWithSameShortId {

        private static final long EXPECTED_ELY_NUMBER_ONE = 1234567890L;
        private static final long EXPECTED_ELY_NUMBER_TWO = 9876543211L;
        private static final String EXPECTED_EXTERNAL_ID_ONE = "c";
        private static final String EXPECTED_EXTERNAL_ID_TWO = "d";
        private static final double EXPECTED_X_COORDINATE = 6;
        private static final double EXPECTED_Y_COORDINATE = 5;
        private static final String EXPECTED_FINNISH_NAME = "Yliopisto vanha";
        private static final String EXPECTED_SWEDISH_NAME = "Universitetet gamla";
        private static final String EXPECTED_PLACE_EXTERNAL_ID = "1KALA";

        @Test
        @DisplayName("The first invocation of the read() method must return the found scheduled stop point")
        void firstInvocationOfReadMethodMustReturnFoundScheduledStopPoint(final SoftAssertions softAssertions) throws Exception {
            final ImporterScheduledStopPoint found = reader.read();

            final List<ExternalId> externalIds = found.externalIds();
            softAssertions.assertThat(externalIds)
                    .as("externalIdSize")
                    .hasSize(2);
            softAssertions.assertThat(externalIds)
                    .as("externalIds")
                    .containsExactly(
                            ExternalId.of(EXPECTED_EXTERNAL_ID_TWO),
                            ExternalId.of(EXPECTED_EXTERNAL_ID_ONE)
                    );

            final List<Long> elyNumbers = found.elyNumbers();
            softAssertions.assertThat(elyNumbers)
                    .as("elyNumberSize")
                    .hasSize(2);
            softAssertions.assertThat(elyNumbers)
                    .as("elyNumbers")
                    .containsExactly(EXPECTED_ELY_NUMBER_TWO, EXPECTED_ELY_NUMBER_ONE);

            final double XCoordinate = found.location().getX();
            softAssertions.assertThat(XCoordinate)
                    .as("X coordinate")
                    .isEqualTo(EXPECTED_X_COORDINATE);

            final double YCoordinate = found.location().getY();
            softAssertions.assertThat(YCoordinate)
                    .as("Y coordinate")
                    .isEqualTo(EXPECTED_Y_COORDINATE);

            final String finnishName = JoreLocaleUtil.getI18nString(found.name(), JoreLocaleUtil.FINNISH);
            softAssertions.assertThat(finnishName)
                    .as("finnishName")
                    .isEqualTo(EXPECTED_FINNISH_NAME);

            final String swedishName = JoreLocaleUtil.getI18nString(found.name(), JoreLocaleUtil.SWEDISH);
            softAssertions.assertThat(swedishName)
                    .as("swedishName")
                    .isEqualTo(EXPECTED_SWEDISH_NAME);

            softAssertions.assertThat(found.placeExternalId())
                    .as("placeExternalId")
                    .contains(EXPECTED_PLACE_EXTERNAL_ID);
        }

        @Test
        @DisplayName("The second invocation of the read() method must return null")
        void secondInvocationOfReadMethodMustReturnNull() throws Exception {
            //The first invocation returns the scheduled stop found from the database.
            final ImporterScheduledStopPoint first = reader.read();
            assertThat(first).isNotNull();

            //Because there are no more scheduled stop points, this invocation must return null.
            final ImporterScheduledStopPoint second = reader.read();
            assertThat(second).isNull();
        }
    }

    @Nested
    @DisplayName("When the source table has one scheduled stop point with no place ID")
    @Sql(scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_infrastructure_nodes.sql",
            "/sql/importer/populate_scheduled_stop_point_with_no_place_id.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableHasOneScheduledStopPointWithNoPlaceId {

        private static final long EXPECTED_ELY_NUMBER = 987654321L;
        private static final String EXPECTED_EXTERNAL_ID = "d";
        private static final double EXPECTED_X_COORDINATE = 24.468175;
        private static final double EXPECTED_Y_COORDINATE = 60.15286;
        private static final String EXPECTED_FINNISH_NAME = "Etelä-Hervanta";
        private static final String EXPECTED_SWEDISH_NAME = "Södra Hervanta";

        @Test
        @DisplayName("The first invocation of the read() method must return the found scheduled stop point")
        void firstInvocationOfReadMethodMustReturnFoundScheduledStopPoint(final SoftAssertions softAssertions) throws Exception {
            final ImporterScheduledStopPoint found = reader.read();

            final List<ExternalId> externalIds = found.externalIds();
            softAssertions.assertThat(externalIds)
                    .as("externalIdSize")
                    .hasSize(1);
            softAssertions.assertThat(externalIds)
                    .as("externalIds")
                    .containsExactly(ExternalId.of(EXPECTED_EXTERNAL_ID));

            final List<Long> elyNumbers = found.elyNumbers();
            softAssertions.assertThat(elyNumbers)
                    .as("elyNumberSize")
                    .hasSize(1);
            softAssertions.assertThat(elyNumbers)
                    .as("elyNumbers")
                    .containsExactly(EXPECTED_ELY_NUMBER);

            final double XCoordinate = found.location().getX();
            softAssertions.assertThat(XCoordinate)
                    .as("X coordinate")
                    .isEqualTo(EXPECTED_X_COORDINATE);

            final double YCoordinate = found.location().getY();
            softAssertions.assertThat(YCoordinate)
                    .as("Y coordinate")
                    .isEqualTo(EXPECTED_Y_COORDINATE);

            final String finnishName = JoreLocaleUtil.getI18nString(found.name(), JoreLocaleUtil.FINNISH);
            softAssertions.assertThat(finnishName)
                    .as("finnishName")
                    .isEqualTo(EXPECTED_FINNISH_NAME);

            final String swedishName = JoreLocaleUtil.getI18nString(found.name(), JoreLocaleUtil.SWEDISH);
            softAssertions.assertThat(swedishName)
                    .as("swedishName")
                    .isEqualTo(EXPECTED_SWEDISH_NAME);

            softAssertions.assertThat(found.placeExternalId())
                    .as("placeExternalId")
                    .isEmpty();
        }
    }
}
