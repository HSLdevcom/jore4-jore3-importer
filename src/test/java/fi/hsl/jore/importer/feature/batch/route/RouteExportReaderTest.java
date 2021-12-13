package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.ValidityPeriodAssertions;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.network.route.dto.ExportableRoute;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntTest
class RouteExportReaderTest {

    private final JdbcCursorItemReader<ExportableRoute> reader;

   @Autowired
   RouteExportReaderTest(RouteExportReader reader) {
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
    @DisplayName("When the source tables are empty")
    @Sql(scripts = "/sql/destination/drop_tables.sql")
    class WhenSourceTablesAreEmpty {

        @Test
        @DisplayName("The first invocation of the read() method must return null")
        void firstInvocationOfReadMethodMustReturnNull() throws Exception {
            final ExportableRoute found = reader.read();
            assertThat(found).isNull();
        }
    }

    @Nested
    @DisplayName("When the source table has one route")
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
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTableHasOneRoute {

       private final DirectionType EXPECTED_DIRECTION_TYPE = DirectionType.INBOUND;
       private final String EXPECTED_EXTERNAL_ID = "1001";
       private final UUID EXPECTED_LINE_TRANSMODEL_ID = UUID.fromString("5aa7d9fc-2cf9-466d-8ac0-f442d60c261f");
       private static final String EXPECTED_ROUTE_NUMBER = "1";
       private static final String EXPECTED_FINNISH_NAME = "Keskustori - Etelä-Hervanta vanha";
       private static final String EXPECTED_SWEDISH_NAME = "Central torget - Södra Hervanta gamla";

       private final UUID EXPECTED_START_SCHEDULED_STOP_POINT_TRANSMODEL_ID = UUID.fromString("45e83727-41fb-4e75-ad71-7e54d58f23ac");
       private final UUID EXPECTED_END_SCHEDULED_STOP_POINT_TRANSMODEL_ID = UUID.fromString("48a88a16-7b8c-4a97-ac2b-c9bf2ac3a08d");

       private final LocalDate EXPECTED_VALID_DATE_RANGE_START = LocalDate.of(2021, 10, 4);
       private final LocalDate EXPECTED_VALID_DATE_RANGE_END  = LocalDate.of(2022, 1, 1);

       @Test
       @DisplayName("The first invocation of the read()  method must return the found route")
       void firstInvocationOfReadMethodMustReturnFoundRoute(final SoftAssertions softAssertions) throws Exception {
           final ExportableRoute route = reader.read();

           softAssertions.assertThat(route.directionType())
                   .as("directionType")
                   .isEqualTo(EXPECTED_DIRECTION_TYPE);
           softAssertions.assertThat(route.externalId().value())
                   .as("externalID")
                   .isEqualTo(EXPECTED_EXTERNAL_ID);
           softAssertions.assertThat(route.lineTransmodelId())
                   .as("lineTransmodelId")
                   .isEqualTo(EXPECTED_LINE_TRANSMODEL_ID);
           softAssertions.assertThat(route.routeNumber())
                   .as("routeNumber")
                   .isEqualTo(EXPECTED_ROUTE_NUMBER);

           final String finnishName = JoreLocaleUtil.getI18nString(route.name(), JoreLocaleUtil.FINNISH);
           softAssertions.assertThat(finnishName)
                   .as("finnishName")
                   .isEqualTo(EXPECTED_FINNISH_NAME);

           final String swedishName = JoreLocaleUtil.getI18nString(route.name(), JoreLocaleUtil.SWEDISH);
           softAssertions.assertThat(swedishName)
                   .as("swedishName")
                   .isEqualTo(EXPECTED_SWEDISH_NAME);

           softAssertions.assertThat(route.startScheduledStopPointTransmodelId())
                   .as("startScheduledStopPointTransmodelId")
                   .isEqualTo(EXPECTED_START_SCHEDULED_STOP_POINT_TRANSMODEL_ID);
           softAssertions.assertThat(route.endScheduledStopPointTransmodelId())
                   .as("endScheduledStopPointTransmodelId")
                   .isEqualTo(EXPECTED_END_SCHEDULED_STOP_POINT_TRANSMODEL_ID);

           final ValidityPeriodAssertions validityPeriodAssertions = new ValidityPeriodAssertions(
                   softAssertions,
                   route.validDateRange()
           );
           validityPeriodAssertions.assertThatValidityPeriodStartsAt(EXPECTED_VALID_DATE_RANGE_START)
                           .assertThatValidityPeriodEndsAt(EXPECTED_VALID_DATE_RANGE_END);
       }

       @Test
       @DisplayName("The second invocation of the read() method must return null")
       void secondInvocationOfReadMethodMustReturnNull() throws Exception {
           //The first invocation returns the route found from the database.
           final ExportableRoute first = reader.read();
           assertThat(first).isNotNull();

           //Because there are no more routes, this invocation must return null.
           final ExportableRoute second = reader.read();
           assertThat(second).isNull();
       }
    }
}
