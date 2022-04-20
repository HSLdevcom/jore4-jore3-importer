package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRouteGeometry;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntTest
public class RouteGeometryExportReaderTest {

    private final JdbcCursorItemReader<ExportableRouteGeometry> reader;

    @Autowired
    RouteGeometryExportReaderTest(final RouteGeometryExportReader reader) {
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
            final ExportableRouteGeometry first = reader.read();
            assertThat(first).isNull();
        }
    }

    @Nested
    @DisplayName("When the source tables have one route")
    @Sql(scripts = {
            "/sql/destination/drop_tables.sql",
            "/sql/destination/populate_infrastructure_nodes.sql",
            "/sql/destination/populate_lines_with_transmodel_ids.sql",
            "/sql/destination/populate_routes.sql",
            "/sql/destination/populate_route_directions_with_transmodel_ids.sql",
            "/sql/destination/populate_route_points_for_jore4_export.sql",
            "/sql/destination/populate_route_stop_points_for_jore4_export.sql",
            "/sql/destination/populate_scheduled_stop_points_for_jore4_export.sql",
            "/sql/destination/populate_infrastructure_links.sql",
            "/sql/destination/populate_route_links.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTablesHaveOneRoute {

        private final double EXPECTED_FIRST_ROUTE_COORDINATE_LNG = 24.457948;
        private final double EXPECTED_FIRST_ROUTE_COORDINATE_LAT = 60.088725;

        private final double EXPECTED_SECOND_ROUTE_COORDINATE_LNG = 24.468175;
        private final double EXPECTED_SECOND_ROUTE_COORDINATE_LAT = 60.15286;

        private final UUID EXPECTED_ROUTE_DIRECTION_ID = UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");
        private final String EXPECTED_ROUTE_DIRECTION_EXT_ID = "1001-2-20211004";
        private final UUID EXPECTED_ROUTE_TRANSMODEL_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");

        @Test
        @DisplayName("The first invocation of the read() method must return the found route geometry")
        void firstInvocationOfReadMethodMustReturnFoundRouteGeometry(final SoftAssertions softAssertions) throws Exception {
            final ExportableRouteGeometry first = reader.read();

            final Coordinate[] routeGeometryCoordinates = first.geometry().getCoordinates();
            softAssertions.assertThat(routeGeometryCoordinates)
                    .as("routeGeometryCoordinateSize")
                    .hasSize(2);

            final Coordinate firstRouteCoordinate = routeGeometryCoordinates[0];
            softAssertions.assertThat(firstRouteCoordinate.getX())
                    .as("firstRouteCoordinateX")
                    .isEqualTo(EXPECTED_FIRST_ROUTE_COORDINATE_LNG);
            softAssertions.assertThat(firstRouteCoordinate.getY())
                    .as("firstRouteCoordinateY")
                    .isEqualTo(EXPECTED_FIRST_ROUTE_COORDINATE_LAT);

            final Coordinate secondRouteCoordinate = routeGeometryCoordinates[1];
            softAssertions.assertThat(secondRouteCoordinate.getX())
                    .as("secondRouteCoordinateX")
                    .isEqualTo(EXPECTED_SECOND_ROUTE_COORDINATE_LNG);
            softAssertions.assertThat(secondRouteCoordinate.getY())
                    .as("secondRouteCoordinateY")
                    .isEqualTo(EXPECTED_SECOND_ROUTE_COORDINATE_LAT);

            softAssertions.assertThat(first.routeDirectionId())
                    .as("routeDirectionId")
                    .isEqualTo(EXPECTED_ROUTE_DIRECTION_ID);

            softAssertions.assertThat(first.routeDirectionExtId())
                    .as("routeDirectionExtId")
                    .isEqualTo(EXPECTED_ROUTE_DIRECTION_EXT_ID);

            softAssertions.assertThat(first.routeTransmodelId())
                    .as("routeTransmodelId")
                    .isEqualTo(EXPECTED_ROUTE_TRANSMODEL_ID);
        }

        @Test
        @DisplayName("The second invocation of the read() method must return null")
        void secondInvocationOfReadMethodMustReturnNull() throws Exception {
            //The first invocation returns the route geometry found from the database.
            final ExportableRouteGeometry first = reader.read();
            assertThat(first).isNotNull();

            //Because there are no more route geometries, this invocation must return null.
            final ExportableRouteGeometry second = reader.read();
            assertThat(second).isNull();
        }
    }
}