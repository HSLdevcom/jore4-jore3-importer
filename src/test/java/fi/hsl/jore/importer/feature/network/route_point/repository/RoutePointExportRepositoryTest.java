package fi.hsl.jore.importer.feature.network.route_point.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import io.vavr.collection.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntTest
class RoutePointExportRepositoryTest {

    private static final UUID ROUTE_DIRECTION_ID = UUID.fromString("6f93fa6b-8a19-4b98-bd84-b8409e670c70");

    private final RoutePointExportRepository repository;

    @Autowired
    RoutePointExportRepositoryTest(RoutePointExportRepository repository) {
        this.repository = repository;
    }

    @Nested
    @DisplayName("Find importer route points by route direction id")
    class FindImporterRoutePointsByRouteDirectionId {

        @Nested
        @DisplayName("When the source tables are empty")
        @Sql(scripts = "/sql/destination/drop_tables.sql")
        class WhenSourceTablesAreEmpty {

            @Test
            @DisplayName("Should return an empty list")
            void shouldReturnEmptyList() {
                final List<ImporterRoutePoint> routePoints = repository.findImporterRoutePointsByRouteDirectionId(ROUTE_DIRECTION_ID);
                assertThat(routePoints).isEmpty();
            }
        }

        @Nested
        @DisplayName("When the source tables have one route with three route points")
        @Sql(scripts = {
                "/sql/destination/drop_tables.sql",
                "/sql/destination/populate_infrastructure_nodes.sql",
                "/sql/destination/populate_lines_with_transmodel_ids.sql",
                "/sql/destination/populate_routes.sql",
                "/sql/destination/populate_route_directions_with_journey_pattern_transmodel_ids.sql",
                "/sql/destination/populate_route_points_for_jore4_export.sql",
                "/sql/destination/populate_route_stop_points_for_jore4_export.sql",
                "/sql/destination/populate_scheduled_stop_points_for_jore4_export.sql"
        })
        @ExtendWith(SoftAssertionsExtension.class)
        class WhenSourceTablesHaveOneRouteWithThreeRoutePoints {

            private static final double FIRST_ROUTE_POINT_LOCATION_X = 6;
            private static final double FIRST_ROUTE_POINT_LOCATION_Y = 5;
            private static final int FIRST_ROUTE_POINT_ORDER_NUMBER = 1;
            private static final double FIRST_ROUTE_POINT_PROJECTED_LOCATION_X = 13;
            private static final double FIRST_ROUTE_POINT_PROJECTED_LOCATION_Y = 12;
            private static final long FIRST_ROUTE_POINT_STOP_POINT_ELY_NUMBER = 1234567890L;
            private static final String FIRST_ROUTE_POINT_STOP_POINT_SHORT_ID = "H1234";
            private final NodeType FIRST_ROUTE_POINT_TYPE = NodeType.STOP;

            private static final double SECOND_ROUTE_POINT_LOCATION_X = 7;
            private static final double SECOND_ROUTE_POINT_LOCATION_Y = 6;
            private static final int SECOND_ROUTE_POINT_ORDER_NUMBER = 2;
            private final NodeType SECOND_ROUTE_POINT_TYPE = NodeType.CROSSROADS;

            private static final double THIRD_ROUTE_POINT_LOCATION_X = 24.468175;
            private static final double THIRD_ROUTE_POINT_LOCATION_Y = 60.15286;
            private static final int THIRD_ROUTE_POINT_ORDER_NUMBER = 3;
            private static final double THIRD_ROUTE_POINT_PROJECTED_LOCATION_X = 24.468122;
            private static final double THIRD_ROUTE_POINT_PROJECTED_LOCATION_Y = 60.152911;
            private static final long THIRD_ROUTE_POINT_STOP_POINT_ELY_NUMBER = 987654321L;
            private static final String THIRD_ROUTE_POINT_STOP_POINT_SHORT_ID = "H4321";
            private final NodeType THIRD_ROUTE_POINT_TYPE = NodeType.STOP;

            @Test
            @DisplayName("Should return a list that has three importer route points")
            void shouldReturnListThatHasThreeImporterRoutePoints() {
                final List<ImporterRoutePoint> routePoints = repository.findImporterRoutePointsByRouteDirectionId(ROUTE_DIRECTION_ID);
                assertThat(routePoints).hasSize(3);
            }

            @Test
            @DisplayName("Should return the correct information of the first route point")
            void shouldReturnCorrectInformationOfFirstRoutePoint(final SoftAssertions softAssertions) {
                final ImporterRoutePoint firstRoutePoint = repository.findImporterRoutePointsByRouteDirectionId(ROUTE_DIRECTION_ID)
                        .get(0);

                softAssertions.assertThat(firstRoutePoint.location().getX())
                        .as("locationX")
                        .isEqualTo(FIRST_ROUTE_POINT_LOCATION_X);
                softAssertions.assertThat(firstRoutePoint.location().getY())
                        .as("locationY")
                        .isEqualTo(FIRST_ROUTE_POINT_LOCATION_Y);

                softAssertions.assertThat(firstRoutePoint.orderNumber())
                        .as("orderNumber")
                        .isEqualTo(FIRST_ROUTE_POINT_ORDER_NUMBER);

                final Point projectedLocation = firstRoutePoint.projectedLocation().get();
                softAssertions.assertThat(projectedLocation.getX())
                        .as("projectedLocationX")
                        .isEqualTo(FIRST_ROUTE_POINT_PROJECTED_LOCATION_X);
                softAssertions.assertThat(projectedLocation.getY())
                        .as("projectedLocationY")
                        .isEqualTo(FIRST_ROUTE_POINT_PROJECTED_LOCATION_Y);

                softAssertions.assertThat(firstRoutePoint.stopPointElyNumber())
                        .as("stopPointElyNumber")
                        .contains(FIRST_ROUTE_POINT_STOP_POINT_ELY_NUMBER);

                softAssertions.assertThat(firstRoutePoint.stopPointShortId())
                        .as("stopPointShortId")
                        .contains(FIRST_ROUTE_POINT_STOP_POINT_SHORT_ID);

                softAssertions.assertThat(firstRoutePoint.type())
                        .as("type")
                        .isEqualTo(FIRST_ROUTE_POINT_TYPE);
            }

            @Test
            @DisplayName("Should return the correct information of the second route point")
            void shouldReturnCorrectInformationOfSecondRoutePoint(final SoftAssertions softAssertions) {
                final ImporterRoutePoint secondRoutePoint = repository.findImporterRoutePointsByRouteDirectionId(ROUTE_DIRECTION_ID)
                        .get(1);

                softAssertions.assertThat(secondRoutePoint.location().getX())
                        .as("locationX")
                        .isEqualTo(SECOND_ROUTE_POINT_LOCATION_X);
                softAssertions.assertThat(secondRoutePoint.location().getY())
                        .as("locationY")
                        .isEqualTo(SECOND_ROUTE_POINT_LOCATION_Y);

                softAssertions.assertThat(secondRoutePoint.orderNumber())
                        .as("orderNumber")
                        .isEqualTo(SECOND_ROUTE_POINT_ORDER_NUMBER);

                softAssertions.assertThat(secondRoutePoint.projectedLocation())
                        .as("projectedLocation")
                        .isEmpty();

                softAssertions.assertThat(secondRoutePoint.stopPointElyNumber())
                        .as("stopPointElyNumber")
                        .isEmpty();

                softAssertions.assertThat(secondRoutePoint.stopPointShortId())
                        .as("stopPointShortId")
                        .isEmpty();

                softAssertions.assertThat(secondRoutePoint.type())
                        .as("type")
                        .isEqualTo(SECOND_ROUTE_POINT_TYPE);
            }

            @Test
            @DisplayName("Should return the correct information of the third route point")
            void shouldReturnCorrectInformationOfThirdRoutePoint(final SoftAssertions softAssertions) {
                final ImporterRoutePoint thirdRoutePoint = repository.findImporterRoutePointsByRouteDirectionId(ROUTE_DIRECTION_ID)
                        .get(2);

                softAssertions.assertThat(thirdRoutePoint.location().getX())
                        .as("locationX")
                        .isEqualTo(THIRD_ROUTE_POINT_LOCATION_X);
                softAssertions.assertThat(thirdRoutePoint.location().getY())
                        .as("locationY")
                        .isEqualTo(THIRD_ROUTE_POINT_LOCATION_Y);

                softAssertions.assertThat(thirdRoutePoint.orderNumber())
                        .as("orderNumber")
                        .isEqualTo(THIRD_ROUTE_POINT_ORDER_NUMBER);

                final Point projectedLocation = thirdRoutePoint.projectedLocation().get();
                softAssertions.assertThat(projectedLocation.getX())
                        .as("projectedLocationX")
                        .isEqualTo(THIRD_ROUTE_POINT_PROJECTED_LOCATION_X);
                softAssertions.assertThat(projectedLocation.getY())
                        .as("projectedLocationY")
                        .isEqualTo(THIRD_ROUTE_POINT_PROJECTED_LOCATION_Y);

                softAssertions.assertThat(thirdRoutePoint.stopPointElyNumber())
                        .as("stopPointElyNumber")
                        .contains(THIRD_ROUTE_POINT_STOP_POINT_ELY_NUMBER);

                softAssertions.assertThat(thirdRoutePoint.stopPointShortId())
                        .as("stopPointShortId")
                        .contains(THIRD_ROUTE_POINT_STOP_POINT_SHORT_ID);

                softAssertions.assertThat(thirdRoutePoint.type())
                        .as("type")
                        .isEqualTo(THIRD_ROUTE_POINT_TYPE);
            }
        }
    }
}
