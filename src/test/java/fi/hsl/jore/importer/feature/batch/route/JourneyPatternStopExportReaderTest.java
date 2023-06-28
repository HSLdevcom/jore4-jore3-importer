package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.jore3.enumerated.RegulatedTimingPointStatus;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPatternStop;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntTest
class JourneyPatternStopExportReaderTest {

    private final JdbcCursorItemReader<ImporterJourneyPatternStop> reader;

    @Autowired
    JourneyPatternStopExportReaderTest(final JourneyPatternStopExportReader reader) {
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
    class WhenSourceTablesAreEmpty {

        @Test
        @DisplayName("The first invocation of the read() method must return null")
        void firstInvocationOfReadMethodMustReturnNull() throws Exception {
            final ImporterJourneyPatternStop found = reader.read();
            assertThat(found).isNull();
        }
    }

    @Nested
    @DisplayName("When the source table has one route with two stops")
    @Sql(scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_infrastructure_nodes.sql",
            "/sql/importer/populate_lines_with_jore4_ids.sql",
            "/sql/importer/populate_routes.sql",
            "/sql/importer/populate_route_directions_with_journey_pattern_jore4_ids.sql",
            "/sql/importer/populate_route_points_for_jore4_export.sql",
            "/sql/importer/populate_route_stop_points_for_jore4_export.sql",
            "/sql/importer/populate_places.sql",
            "/sql/importer/populate_scheduled_stop_points_for_jore4_export.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableHasOneRouteWithTwoStops {

        private final UUID JOURNEY_PATTERN_JORE4_ID = UUID.fromString("ec564137-f30c-4689-9322-4ef650768af3");
        private final String ROUTE_DIRECTION_JORE3_ID = "1001-2-20211004";

        private static final String FIRST_JOURNEY_PATTERN_STOP_JORE4_LABEL = "H1234";
        private static final int FIRST_JOURNEY_PATTERN_STOP_ORDER_NUMBER = 1;
        private static final boolean FIRST_JOURNEY_PATTERN_STOP_IS_USED_AS_TIMING_POINT = false;
        private static final String FIRST_JOURNEY_PATTERN_STOP_TIMING_PLACE_LABEL = "1ELIEL";
        private final RegulatedTimingPointStatus FIRST_JOURNEY_PATTERN_STOP_REGULATED_TIMING_POINT_STATUS = RegulatedTimingPointStatus.NO;
        private static final boolean FIRST_JOURNEY_PATTERN_STOP_IS_VIA_POINT = false;

        private static final String SECOND_JOURNEY_PATTERN_STOP_JORE4_LABEL = "H4321";
        private static final int SECOND_JOURNEY_PATTERN_STOP_ORDER_NUMBER = 2;
        private static final boolean SECOND_JOURNEY_PATTERN_STOP_IS_USED_AS_TIMING_POINT = true;
        private static final boolean SECOND_JOURNEY_PATTERN_STOP_IS_VIA_POINT = true;
        private static final String SECOND_JOURNEY_PATTERN_STOP_TIMING_PLACE_LABEL = "1KALA";
        private final RegulatedTimingPointStatus SECOND_JOURNEY_PATTERN_STOP_REGULATED_TIMING_POINT_STATUS = RegulatedTimingPointStatus.YES_LOAD_TIME;
        private static final String SECOND_JOURNEY_PATTERN_STOP_VIA_NAME_FINNISH = "ViaSuomi";
        private static final String SECOND_JOURNEY_PATTERN_STOP_VIA_NAME_SWEDISH = "ViaSverige";

        @Test
        @DisplayName("The first invocation of the read() method must return the information of the second stop")
        void firstInvocationOfReadMethodMustReturnInformationOfFirstStop(final SoftAssertions softAssertions) throws Exception {
            final ImporterJourneyPatternStop first = reader.read();

            softAssertions.assertThat(first.journeyPatternJore4Id())
                    .as("journeyPatternJore4Id")
                    .isEqualTo(JOURNEY_PATTERN_JORE4_ID);
            softAssertions.assertThat(first.routeDirectionJore3Id())
                    .as("routeDirectionJore3Id")
                    .isEqualTo(ROUTE_DIRECTION_JORE3_ID);
            softAssertions.assertThat(first.orderNumber())
                    .as("orderNumber")
                    .isEqualTo(FIRST_JOURNEY_PATTERN_STOP_ORDER_NUMBER);
            softAssertions.assertThat(first.scheduledStopPointJore4Label())
                    .as("scheduledStopPointJore4Label")
                    .isEqualTo(FIRST_JOURNEY_PATTERN_STOP_JORE4_LABEL);
            softAssertions.assertThat(first.isUsedAsTimingPoint())
                    .as("isUsedAsTimingPoint")
                    .isEqualTo(FIRST_JOURNEY_PATTERN_STOP_IS_USED_AS_TIMING_POINT);
            softAssertions.assertThat(first.timingPlaceLabel())
                    .as("timingPlaceLabel")
                    .contains(FIRST_JOURNEY_PATTERN_STOP_TIMING_PLACE_LABEL);
            softAssertions.assertThat(first.regulatedTimingPointStatus())
                    .as("regulatedTimingPointStatus")
                    .isEqualTo(FIRST_JOURNEY_PATTERN_STOP_REGULATED_TIMING_POINT_STATUS);
            softAssertions.assertThat(first.isViaPoint())
                    .as("isViaPoint")
                    .isEqualTo(FIRST_JOURNEY_PATTERN_STOP_IS_VIA_POINT);
            softAssertions.assertThat(first.viaPointNames().isEmpty())
                    .as("viaPointNames")
                    .isTrue();
        }

        @Test
        @DisplayName("The second invocation of the read() method must return the information of the second stop")
        void secondInvocationOfReadMethodMustReturnInformationOfSecondStop(SoftAssertions softAssertions) throws Exception {
            //The first invocation returns the journey pattern stop found from the database.
            final ImporterJourneyPatternStop first = reader.read();
            assertThat(first).isNotNull();

            final ImporterJourneyPatternStop second = reader.read();
            softAssertions.assertThat(second.journeyPatternJore4Id())
                    .as("journeyPatternJore4Id")
                    .isEqualTo(JOURNEY_PATTERN_JORE4_ID);
            softAssertions.assertThat(second.routeDirectionJore3Id())
                    .as("routeDirectionJore3Id")
                    .isEqualTo(ROUTE_DIRECTION_JORE3_ID);
            softAssertions.assertThat(second.orderNumber())
                    .as("orderNumber")
                    .isEqualTo(SECOND_JOURNEY_PATTERN_STOP_ORDER_NUMBER);
            softAssertions.assertThat(second.scheduledStopPointJore4Label())
                    .as("scheduledStopPointJore4Label")
                    .isEqualTo(SECOND_JOURNEY_PATTERN_STOP_JORE4_LABEL);
            softAssertions.assertThat(second.isUsedAsTimingPoint())
                    .as("isUsedAsTimingPoint")
                    .isEqualTo(SECOND_JOURNEY_PATTERN_STOP_IS_USED_AS_TIMING_POINT);
            softAssertions.assertThat(second.timingPlaceLabel())
                    .as("timingPlaceLabel")
                    .contains(SECOND_JOURNEY_PATTERN_STOP_TIMING_PLACE_LABEL);
            softAssertions.assertThat(second.regulatedTimingPointStatus())
                    .as("regulatedTimingPointStatus")
                    .isEqualTo(SECOND_JOURNEY_PATTERN_STOP_REGULATED_TIMING_POINT_STATUS);
            softAssertions.assertThat(second.isViaPoint())
                    .as("isViaPoint")
                    .isEqualTo(SECOND_JOURNEY_PATTERN_STOP_IS_VIA_POINT);
            softAssertions.assertThat(JoreLocaleUtil.getI18nString(second.viaPointNames().get(), JoreLocaleUtil.FINNISH))
                    .as("viaPointFinnishName")
                    .isEqualTo(SECOND_JOURNEY_PATTERN_STOP_VIA_NAME_FINNISH);
            softAssertions.assertThat(JoreLocaleUtil.getI18nString(second.viaPointNames().get(), JoreLocaleUtil.SWEDISH))
                    .as("viaPointSwedishName")
                    .isEqualTo(SECOND_JOURNEY_PATTERN_STOP_VIA_NAME_SWEDISH);
        }

        @Test
        @DisplayName("The third invocation of the read method must return null")
        void thirdInvocationOfReaderMethodMustReturnNull() throws Exception {
            //The first invocation returns the journey pattern stop found from the database.
            final ImporterJourneyPatternStop first = reader.read();
            assertThat(first).isNotNull();

            //The second invocation returns the journey pattern stop found from the database.
            final ImporterJourneyPatternStop second = reader.read();
            assertThat(second).isNotNull();

            //Because there are no more journey pattern stops, this invocation must return null.
            final ImporterJourneyPatternStop third = reader.read();
            assertThat(third).isNull();
        }
    }
}
