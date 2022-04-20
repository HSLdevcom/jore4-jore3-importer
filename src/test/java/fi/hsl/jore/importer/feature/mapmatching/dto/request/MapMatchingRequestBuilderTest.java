package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ExportableRoutePoint;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.collection.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.geojson.LngLatAlt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MapMatchingRequestBuilderTest {

    @Nested
    @DisplayName("Create map matching request")
    @ExtendWith(SoftAssertionsExtension.class)
    class CreateMapMatchingRequest {

        private final UUID ROUTE_DIRECTION_ID = UUID.randomUUID();
        private final String ROUTE_DIRECTION_EXT_ID = "1001-2-20211004";
        private final UUID ROUTE_TRANSMODEL_ID = UUID.randomUUID();

        private static final double ROUTE_GEOMETRY_START_LNG = 5;
        private static final double ROUTE_GEOMETRY_START_LATITUDE = 6;
        private static final double ROUTE_GEOMETRY_END_LNG = 10;
        private static final double ROUTE_GEOMETRY_END_LAT = 11;

        private static final double ROUTE_POINT_LNG = 4.5533;
        private static final double ROUTE_POINT_LAT = 10.3343;

        private ExportableRouteGeometry routeGeometryInput;

        @BeforeEach
        void createRouteGeometry() {
            final CoordinateSequence coordinates = new CoordinateArraySequence(new Coordinate[]{
                    new Coordinate(ROUTE_GEOMETRY_START_LNG, ROUTE_GEOMETRY_START_LATITUDE),
                    new Coordinate(ROUTE_GEOMETRY_END_LNG, ROUTE_GEOMETRY_END_LAT)
            });
            final LineString routeLineString = new LineString(coordinates,
                    GeometryUtil.factoryForSrid(GeometryUtil.SRID_WGS84)
            );

            routeGeometryInput = ExportableRouteGeometry.from(routeLineString,
                    ROUTE_DIRECTION_ID,
                    ROUTE_DIRECTION_EXT_ID,
                    ROUTE_TRANSMODEL_ID
            );
        }

        @Test
        @DisplayName("Should return a map matching request with the correct route id")
        void shouldReturnMapMatchingRequestWithCorrectRouteId() {
            final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                    List.empty()
            );

            assertThat(request.getRouteId()).isEqualTo(ROUTE_DIRECTION_EXT_ID);
        }

        @Test
        @DisplayName("Should return a map matching request with the correct route geometry")
        void shouldReturnMapMatchingRequestWithCorrectRouteGeometry(final SoftAssertions softAssertions) {
            final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                    List.empty()
            );
            final org.geojson.LineString routeGeometry = request.getRouteGeometry();

            softAssertions.assertThat(routeGeometry.getCoordinates()).hasSize(2);

            final LngLatAlt startPoint = routeGeometry.getCoordinates().get(0);
            softAssertions.assertThat(startPoint.getLongitude())
                    .as("startPointLongitude")
                    .isEqualTo(ROUTE_GEOMETRY_START_LNG);
            softAssertions.assertThat(startPoint.getLatitude())
                    .as("startPointLatitude")
                    .isEqualTo(ROUTE_GEOMETRY_START_LATITUDE);

            final LngLatAlt endPoint = routeGeometry.getCoordinates().get(1);
            softAssertions.assertThat(endPoint.getLongitude())
                    .as("endPointLongitude")
                    .isEqualTo(ROUTE_GEOMETRY_END_LNG);
            softAssertions.assertThat(endPoint.getLatitude())
                    .as("endPointLatitude")
                    .isEqualTo(ROUTE_GEOMETRY_END_LAT);
        }

        @Nested
        @DisplayName("When the input route point is a crossroad")
        class WhenInputRoutePointIsCrossroad {

            ExportableRoutePoint routePoint;

            @BeforeEach
            void createRoutePoint() {
                final CoordinateSequence locationCoordinates = new CoordinateArraySequence(new Coordinate[]{
                        new Coordinate(ROUTE_POINT_LNG, ROUTE_POINT_LAT)
                });
                final Point location = new Point(
                        locationCoordinates,
                        GeometryUtil.factoryForSrid(GeometryUtil.SRID_WGS84)
                );

                routePoint = ExportableRoutePoint.from(
                        location,
                        1,
                        Optional.empty(),
                        NodeType.CROSSROADS,
                        Optional.empty(),
                        Optional.empty()
                );
            }

            @Test
            @DisplayName("Should return a map matching request that has one route point")
            void shouldReturnMapMatchingRequestThatHasOneRoutePoint() {
                final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                        List.of(routePoint)
                );

                assertThat(request.getRoutePoints()).hasSize(1);
            }

            @Test
            @DisplayName("Should return a map matching request with the correct route point")
            void shouldReturnMapMatchingRequestWithCorrectRoutePoint(SoftAssertions softAssertions) {
                final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                        List.of(routePoint)
                );
                final RoutePointRequestDTO routePoint = request.getRoutePoints().get(0);

                softAssertions.assertThat(routePoint.getType())
                        .as("type")
                        .isEqualTo(RoutePointType.ROAD_JUNCTION);

                final org.geojson.Point routePointLocation = routePoint.getLocation();
                final LngLatAlt routePointCoordinates = routePointLocation.getCoordinates();
                softAssertions.assertThat(routePointCoordinates.getLongitude())
                        .as("routePointLongitude")
                        .isEqualTo(ROUTE_POINT_LNG);
                softAssertions.assertThat(routePointCoordinates.getLatitude())
                        .as("routePointLatitude")
                        .isEqualTo(ROUTE_POINT_LAT);

                //Because this route point is road junction, the following
                //field values must be null.
                softAssertions.assertThat(routePoint.getNationalId())
                        .as("nationalId")
                        .isNull();
                softAssertions.assertThat(routePoint.getPassengerId())
                        .as("passengerId")
                        .isNull();
                softAssertions.assertThat(routePoint.getProjectedLocation())
                        .as("projectedLocation")
                        .isNull();
            }
        }

        @Nested
        @DisplayName("When the input route point is a scheduled stop point")
        class WhenInputRoutePointIsScheduledStopPoint {

            private static final String SHORT_ID = "H1234";

            private static final double ROUTE_PROJECTED_POINT_LNG = 5.604;
            private static final double ROUTE_PROJECTED_POINT_LAT = 12.053;

            Point location;
            Point projectedLocation;
            ExportableRoutePoint routePoint;

            @BeforeEach
            void createLocations() {
                final CoordinateSequence locationCoordinates = new CoordinateArraySequence(new Coordinate[]{
                        new Coordinate(ROUTE_POINT_LNG, ROUTE_POINT_LAT)
                });
                location = new Point(
                        locationCoordinates,
                        GeometryUtil.factoryForSrid(GeometryUtil.SRID_WGS84)
                );

                final CoordinateSequence projectedLocationCoordinates = new CoordinateArraySequence(new Coordinate[]{
                        new Coordinate(ROUTE_PROJECTED_POINT_LNG, ROUTE_PROJECTED_POINT_LAT)
                });
                projectedLocation = new Point(
                        projectedLocationCoordinates,
                        GeometryUtil.factoryForSrid(GeometryUtil.SRID_WGS84)
                );
            }

            @Nested
            @DisplayName("When ely number is an integer")
            class WhenElyNumberIsInteger {

                private static final int ELY_NUMBER_IN_REQUEST = 123567;
                private static final String ELY_NUMBER_INPUT = "123567";

                @BeforeEach
                void createRoutePoint() {
                    routePoint = ExportableRoutePoint.from(
                            location,
                            1,
                            Optional.of(projectedLocation),
                            NodeType.STOP,
                            Optional.of(ELY_NUMBER_INPUT),
                            Optional.of(SHORT_ID)
                    );
                }

                @Test
                @DisplayName("Should return a map matching request that has one route point")
                void shouldReturnMapMatchingRequestThatHasOneRoutePoint() {
                    final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                            List.of(routePoint)
                    );

                    assertThat(request.getRoutePoints()).hasSize(1);
                }

                @Test
                @DisplayName("Should return a map matching request with the correct route point")
                void shouldReturnMapMatchingRequestWithCorrectRoutePoint(final SoftAssertions softAssertions) {
                    final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                            List.of(routePoint)
                    );
                    final RoutePointRequestDTO routePoint = request.getRoutePoints().get(0);

                    softAssertions.assertThat(routePoint.getType())
                            .as("type")
                            .isEqualTo(RoutePointType.PUBLIC_TRANSPORT_STOP);

                    softAssertions.assertThat(routePoint.getNationalId())
                            .as("nationalId")
                            .isEqualByComparingTo(ELY_NUMBER_IN_REQUEST);
                    softAssertions.assertThat(routePoint.getPassengerId())
                            .as("passengerId")
                            .contains(SHORT_ID);

                    final org.geojson.Point routePointLocation = routePoint.getLocation();
                    final LngLatAlt routePointCoordinates = routePointLocation.getCoordinates();
                    softAssertions.assertThat(routePointCoordinates.getLongitude())
                            .as("routePointLongitude")
                            .isEqualTo(ROUTE_POINT_LNG);
                    softAssertions.assertThat(routePointCoordinates.getLatitude())
                            .as("routePointLatitude")
                            .isEqualTo(ROUTE_POINT_LAT);

                    final org.geojson.Point routePointProjectedLocation = routePoint.getProjectedLocation();
                    final LngLatAlt routePointProjectedCoordinates = routePointProjectedLocation.getCoordinates();
                    softAssertions.assertThat(routePointProjectedCoordinates.getLongitude())
                            .as("routePointProjectedLongitude")
                            .isEqualTo(ROUTE_PROJECTED_POINT_LNG);
                    softAssertions.assertThat(routePointProjectedCoordinates.getLatitude())
                            .as("routePointProjectedLatitude")
                            .isEqualTo(ROUTE_PROJECTED_POINT_LAT);
                }
            }

            @Nested
            @DisplayName("When ely number is a string")
            class WhenElyNumberIsString {

                private static final String ELY_NUMBER_INPUT = "elyNumber";

                @BeforeEach
                void createRoutePoint() {
                    routePoint = ExportableRoutePoint.from(
                            location,
                            1,
                            Optional.of(projectedLocation),
                            NodeType.STOP,
                            Optional.of(ELY_NUMBER_INPUT),
                            Optional.of(SHORT_ID)
                    );
                }

                @Test
                @DisplayName("Should return a map matching request that has one route point")
                void shouldReturnMapMatchingRequestThatHasOneRoutePoint() {
                    final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                            List.of(routePoint)
                    );

                    assertThat(request.getRoutePoints()).hasSize(1);
                }

                @Test
                @DisplayName("Should return a map matching request with the correct route point")
                void shouldReturnMapMatchingRequestWithCorrectRoutePoint(final SoftAssertions softAssertions) {
                    final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                            List.of(routePoint)
                    );
                    final RoutePointRequestDTO routePoint = request.getRoutePoints().get(0);

                    softAssertions.assertThat(routePoint.getType())
                            .as("type")
                            .isEqualTo(RoutePointType.PUBLIC_TRANSPORT_STOP);

                    softAssertions.assertThat(routePoint.getNationalId())
                            .as("nationalId")
                            .isNull();
                    softAssertions.assertThat(routePoint.getPassengerId())
                            .as("passengerId")
                            .contains(SHORT_ID);

                    final org.geojson.Point routePointLocation = routePoint.getLocation();
                    final LngLatAlt routePointCoordinates = routePointLocation.getCoordinates();
                    softAssertions.assertThat(routePointCoordinates.getLongitude())
                            .as("routePointLongitude")
                            .isEqualTo(ROUTE_POINT_LNG);
                    softAssertions.assertThat(routePointCoordinates.getLatitude())
                            .as("routePointLatitude")
                            .isEqualTo(ROUTE_POINT_LAT);

                    final org.geojson.Point routePointProjectedLocation = routePoint.getProjectedLocation();
                    final LngLatAlt routePointProjectedCoordinates = routePointProjectedLocation.getCoordinates();
                    softAssertions.assertThat(routePointProjectedCoordinates.getLongitude())
                            .as("routePointProjectedLongitude")
                            .isEqualTo(ROUTE_PROJECTED_POINT_LNG);
                    softAssertions.assertThat(routePointProjectedCoordinates.getLatitude())
                            .as("routePointProjectedLatitude")
                            .isEqualTo(ROUTE_PROJECTED_POINT_LAT);
                }
            }

            @Nested
            @DisplayName("When ely number is empty optional")
            class WhenElyNumberIsEmptyOptional {

                @BeforeEach
                void createRoutePoint() {
                    routePoint = ExportableRoutePoint.from(
                            location,
                            1,
                            Optional.of(projectedLocation),
                            NodeType.STOP,
                            Optional.empty(),
                            Optional.of(SHORT_ID)
                    );
                }

                @Test
                @DisplayName("Should return a map matching request that has one route point")
                void shouldReturnMapMatchingRequestThatHasOneRoutePoint() {
                    final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                            List.of(routePoint)
                    );

                    assertThat(request.getRoutePoints()).hasSize(1);
                }

                @Test
                @DisplayName("Should return a map matching request with the correct route point")
                void shouldReturnMapMatchingRequestWithCorrectRoutePoint(final SoftAssertions softAssertions) {
                    final MapMatchingRequestDTO request = MapMatchingRequestBuilder.createMapMatchingRequest(routeGeometryInput,
                            List.of(routePoint)
                    );
                    final RoutePointRequestDTO routePoint = request.getRoutePoints().get(0);

                    softAssertions.assertThat(routePoint.getType())
                            .as("type")
                            .isEqualTo(RoutePointType.PUBLIC_TRANSPORT_STOP);

                    softAssertions.assertThat(routePoint.getNationalId())
                            .as("nationalId")
                            .isNull();
                    softAssertions.assertThat(routePoint.getPassengerId())
                            .as("passengerId")
                            .contains(SHORT_ID);

                    final org.geojson.Point routePointLocation = routePoint.getLocation();
                    final LngLatAlt routePointCoordinates = routePointLocation.getCoordinates();
                    softAssertions.assertThat(routePointCoordinates.getLongitude())
                            .as("routePointLongitude")
                            .isEqualTo(ROUTE_POINT_LNG);
                    softAssertions.assertThat(routePointCoordinates.getLatitude())
                            .as("routePointLatitude")
                            .isEqualTo(ROUTE_POINT_LAT);

                    final org.geojson.Point routePointProjectedLocation = routePoint.getProjectedLocation();
                    final LngLatAlt routePointProjectedCoordinates = routePointProjectedLocation.getCoordinates();
                    softAssertions.assertThat(routePointProjectedCoordinates.getLongitude())
                            .as("routePointProjectedLongitude")
                            .isEqualTo(ROUTE_PROJECTED_POINT_LNG);
                    softAssertions.assertThat(routePointProjectedCoordinates.getLatitude())
                            .as("routePointProjectedLatitude")
                            .isEqualTo(ROUTE_PROJECTED_POINT_LAT);
                }
            }
        }
    }
}