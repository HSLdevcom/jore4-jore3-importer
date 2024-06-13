package fi.hsl.jore.importer.feature.mapmatching.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static fi.hsl.jore.importer.feature.mapmatching.service.RouteGeometryTestFactory.createRouteGeometry;
import static fi.hsl.jore.importer.feature.mapmatching.service.RouteGeometryTestFactory.createRoutePoints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.InfrastructureLinkDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.MapMatchingSuccessResponseDTO;
import fi.hsl.jore.importer.feature.mapmatching.dto.response.RouteDTO;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

class MapMatchingServiceTest {

    private static final String EXPECTED_MAP_MATCHING_API_PATH = "/api/match/public-transport-route/v1/bus.json";
    private static final String MAP_MATCHING_API_BASE_URL_TEMPLATE = "http://localhost:%d";

    private MapMatchingService mapMatchingService;
    private WireMockServer wireMockServer;

    private ImporterRouteGeometry routeGeometryInput;
    private java.util.List<ImporterRoutePoint> routePointsInput;

    @BeforeEach
    void configureSystemUnderTest() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        configureFor(wireMockServer.port());

        mapMatchingService = new MapMatchingService(
                String.format(MAP_MATCHING_API_BASE_URL_TEMPLATE, wireMockServer.port()),
                new ObjectMapper(),
                new RestTemplate());

        routeGeometryInput = createRouteGeometry();
        routePointsInput = createRoutePoints();
    }

    @AfterEach
    void stopWireMockServer() {
        wireMockServer.stop();
    }

    @Nested
    @DisplayName("When the map matching API doesn't return the HTTP status code ok (200)")
    class WhenMapMatchingApiDoesNotReturnHttpStatusCodeOk {

        @BeforeEach
        void returnHttpStatusCodeBadRequest() {
            givenThat(post(urlEqualTo(EXPECTED_MAP_MATCHING_API_PATH))
                    .willReturn(aResponse().withStatus(HttpStatus.BAD_REQUEST.value())));
        }

        @Test
        @DisplayName("Should throw an exception")
        void shouldThrowException() {
            assertThatThrownBy(() -> mapMatchingService.sendMapMatchingRequest(routeGeometryInput, routePointsInput))
                    .isExactlyInstanceOf(MapMatchingException.class);
        }
    }

    @Nested
    @DisplayName("When the map matching API returns the HTTP status code ok (200)")
    class WhenMapMatchingApiReturnsHttpStatusCodeOk {
        private static final String EXPECTED_MAP_MATCHING_REQUEST_BODY =
                """
                {
                  "routeId": "1001-2-20211004",
                  "routeGeometry": {
                    "type": "LineString",
                    "coordinates": [
                      [5.0, 6.0],
                      [10.0, 11.0]
                    ]
                  },
                  "routePoints": [
                    {
                      "location": {
                        "type": "Point",
                        "coordinates": [4.5533, 10.3343]
                      },
                      "nationalId": 123567,
                      "passengerId": "H1234",
                      "projectedLocation": {
                        "type": "Point",
                        "coordinates": [5.604, 12.053]
                      },
                      "type": "PUBLIC_TRANSPORT_STOP"
                    },
                    {
                      "location": {
                        "type": "Point",
                        "coordinates": [3.212, 1.2334]
                      },
                      "type": "ROAD_JUNCTION"
                    }
                  ]
                }
                """;

        private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
        private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

        private static final String MAP_MATCHING_RESPONSE = "{\"code\": \"Ok\"}";

        @BeforeEach
        void returnMapMatchingResponse() {
            givenThat(post(urlEqualTo(EXPECTED_MAP_MATCHING_API_PATH)).willReturn(okJson(MAP_MATCHING_RESPONSE)));
        }

        @Test
        @DisplayName("Should send the request body to the map matching API as Json")
        void shouldSendRequestBodyToMapMatchingApiAsJson() {
            mapMatchingService.sendMapMatchingRequest(routeGeometryInput, routePointsInput);

            verify(postRequestedFor(urlEqualTo(EXPECTED_MAP_MATCHING_API_PATH))
                    .withHeader(HTTP_HEADER_CONTENT_TYPE, equalTo(CONTENT_TYPE_APPLICATION_JSON)));
        }

        @Test
        @DisplayName("Should send the correct request body to the map matching API")
        void shouldSendCorrectRequestBodyToMapMatchingAPI() {
            mapMatchingService.sendMapMatchingRequest(routeGeometryInput, routePointsInput);

            verify(postRequestedFor(urlEqualTo(EXPECTED_MAP_MATCHING_API_PATH))
                    .withRequestBody(equalToJson(EXPECTED_MAP_MATCHING_REQUEST_BODY)));
        }

        @Nested
        @DisplayName("When the response code isn't Ok")
        class WhenResponseCodeIsNotOk {

            private static final String ERROR_RESPONSE = "{\"code\": \"Error\"}";

            @BeforeEach
            void returnErrorResponse() {
                givenThat(post(urlEqualTo(EXPECTED_MAP_MATCHING_API_PATH)).willReturn(okJson(ERROR_RESPONSE)));
            }

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(
                                () -> mapMatchingService.sendMapMatchingRequest(routeGeometryInput, routePointsInput))
                        .isExactlyInstanceOf(MapMatchingException.class);
            }
        }

        @Nested
        @DisplayName("When the response code is Ok")
        class WhenResponseCodeIsOk {

            @Nested
            @DisplayName("When the map matching request failed")
            class WhenMapMatchingRequestFailed {

                private static final String MAP_MATCHING_RESPONSE = "{ \"code\": \"Ok\", \"message\": \"Error\" }";

                @BeforeEach
                void returnMapMatchingResponse() {
                    givenThat(
                            post(urlEqualTo(EXPECTED_MAP_MATCHING_API_PATH)).willReturn(okJson(MAP_MATCHING_RESPONSE)));
                }

                @Test
                @DisplayName("Should throw an exception")
                void shouldThrowException() {
                    assertThatThrownBy(() ->
                                    mapMatchingService.sendMapMatchingRequest(routeGeometryInput, routePointsInput))
                            .isExactlyInstanceOf(MapMatchingException.class);
                }
            }

            @Nested
            @DisplayName("When a match was found")
            @ExtendWith(SoftAssertionsExtension.class)
            class WhenMatchWasFound {
                private static final String MAP_MATCHING_RESPONSE =
                        """
                        {
                          "code": "Ok",
                          "routes": [
                            {
                              "geometry": {
                                "type": "LineString",
                                "coordinates": [
                                  [24.92746831478124, 60.16364653019382],\s
                                  [24.92919590585085, 60.164250754589546],
                                  [24.931694330826097, 60.16512656170321]
                                ]
                              },
                              "weight": 286.7201809450182,
                              "distance": 286.7201809449182,
                              "paths": [
                                {
                                  "mapMatchingInfrastructureLinkId": 209830,
                                  "externalLinkRef": {
                                    "infrastructureSource": "digiroad_r",
                                    "externalLinkId": "445117"
                                  },
                                  "isTraversalForwards": true,
                                  "geometry": {
                                    "type": "LineString",
                                    "coordinates": [
                                      [24.92743115932746, 60.16363353459729],
                                      [24.92919590585085, 60.164250754589546]
                                    ]
                                  },
                                  "weight": 119.68001616364820,
                                  "distance": 119.68001616364803,
                                  "infrastructureLinkName": {
                                    "fi": "Kalevankatu 1",
                                    "sv": "Kalevagatan 1"
                                  }
                                },
                                {
                                  "mapMatchingInfrastructureLinkId": 232425,
                                  "externalLinkRef": {
                                    "infrastructureSource": "digiroad_r",
                                    "externalLinkId": "442423"
                                  },
                                  "isTraversalForwards": true,
                                  "geometry": {
                                    "type": "LineString",
                                    "coordinates": [
                                      [24.92919590585085, 60.164250754589546],
                                      [24.932010800782077, 60.16523749159016]
                                    ]
                                  },
                                  "weight": 191.0508963733580,
                                  "distance": 191.0508963733563,
                                  "infrastructureLinkName": {
                                    "fi": "Kalevankatu 2",
                                    "sv": "Kalevagatan 2"
                                  }
                                }
                              ]
                            }
                          ]
                        }
                        """;

                private static final String LANGUAGE_CODE_FINNISH = "fi";
                private static final String LANGUAGE_CODE_SWEDISH = "sv";

                private static final String EXPECTED_MAP_MATCHING_RESPONSE_CODE = "Ok";

                private static final double EXPECTED_ROUTE_GEOMETRY_COORDINATE_ONE_LNG = 24.92746831478124;
                private static final double EXPECTED_ROUTE_GEOMETRY_COORDINATE_ONE_LAT = 60.16364653019382;
                private static final double EXPECTED_ROUTE_GEOMETRY_COORDINATE_TWO_LNG = 24.92919590585085;
                private static final double EXPECTED_ROUTE_GEOMETRY_COORDINATE_TWO_LAT = 60.164250754589546;
                private static final double EXPECTED_ROUTE_GEOMETRY_COORDINATE_THREE_LNG = 24.931694330826097;
                private static final double EXPECTED_ROUTE_GEOMETRY_COORDINATE_THREE_LAT = 60.16512656170321;

                private static final double EXPECTED_ROUTE_DISTANCE = 286.7201809449182;
                private static final double EXPECTED_ROUTE_WEIGHT = 286.7201809450182;

                private static final long EXPECTED_INFRASTRUCTURE_LINK_ONE_ID = 209830;
                private static final String EXPECTED_INFRASTRUCTURE_LINK_ONE_SOURCE = "digiroad_r";
                private static final String EXPECTED_INFRASTRUCTURE_LINK_ONE_EXT_ID = "445117";
                private static final boolean EXPECTED_INFRASTRUCTURE_LINK_ONE_TRAVERSAL_FORWARDS = true;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_ONE_COORDINATE_ONE_LNG = 24.92743115932746;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_ONE_COORDINATE_ONE_LAT = 60.16363353459729;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_ONE_COORDINATE_TWO_LNG = 24.92919590585085;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_ONE_COORDINATE_TWO_LAT = 60.164250754589546;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_ONE_WEIGHT = 119.68001616364820;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_ONE_DISTANCE = 119.68001616364803;
                private static final String EXPECTED_INFRASTRUCTURE_LINK_ONE_FINNISH_NAME = "Kalevankatu 1";
                private static final String EXPECTED_INFRASTRUCTURE_LINK_ONE_SWEDISH_NAME = "Kalevagatan 1";

                private static final long EXPECTED_INFRASTRUCTURE_LINK_TWO_ID = 232425;
                private static final String EXPECTED_INFRASTRUCTURE_LINK_TWO_SOURCE = "digiroad_r";
                private static final String EXPECTED_INFRASTRUCTURE_LINK_TWO_EXT_ID = "442423";
                private static final boolean EXPECTED_INFRASTRUCTURE_LINK_TWO_TRAVERSAL_FORWARDS = true;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_TWO_COORDINATE_ONE_LNG = 24.92919590585085;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_TWO_COORDINATE_ONE_LAT = 60.164250754589546;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_TWO_COORDINATE_TWO_LNG = 24.932010800782077;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_TWO_COORDINATE_TWO_LAT = 60.16523749159016;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_TWO_WEIGHT = 191.0508963733580;
                private static final double EXPECTED_INFRASTRUCTURE_LINK_TWO_DISTANCE = 191.0508963733563;
                private static final String EXPECTED_INFRASTRUCTURE_LINK_TWO_FINNISH_NAME = "Kalevankatu 2";
                private static final String EXPECTED_INFRASTRUCTURE_LINK_TWO_SWEDISH_NAME = "Kalevagatan 2";

                @BeforeEach
                void returnMapMatchingResponse() {
                    givenThat(
                            post(urlEqualTo(EXPECTED_MAP_MATCHING_API_PATH)).willReturn(okJson(MAP_MATCHING_RESPONSE)));
                }

                @Test
                @DisplayName("Should return a map matching response with the correct code")
                void shouldReturnMapMatchingResponseWithCorrectCode() {
                    final MapMatchingSuccessResponseDTO response =
                            mapMatchingService.sendMapMatchingRequest(routeGeometryInput, routePointsInput);
                    assertThat(response.getCode()).isEqualTo(EXPECTED_MAP_MATCHING_RESPONSE_CODE);
                }

                @Test
                @DisplayName("Should return a map matching response with one route")
                void shouldReturnMapMatchingResponseWithOneRoute() {
                    final MapMatchingSuccessResponseDTO response =
                            mapMatchingService.sendMapMatchingRequest(routeGeometryInput, routePointsInput);
                    assertThat(response.getRoutes()).hasSize(1);
                }

                @Test
                @DisplayName("Should return a route with correct route geometry")
                void shouldReturnRouteWithCorrectRouteGeometry(final SoftAssertions softAssertions) {
                    final LineString routeGeometry = mapMatchingService
                            .sendMapMatchingRequest(routeGeometryInput, routePointsInput)
                            .getRoutes()
                            .get(0)
                            .getGeometry();

                    final List<LngLatAlt> coordinates = routeGeometry.getCoordinates();
                    assertThat(coordinates).as("coordinateCount").hasSize(3);

                    final LngLatAlt coordinateOne = coordinates.get(0);
                    softAssertions
                            .assertThat(coordinateOne.getLongitude())
                            .as("coordinateOneLongitude")
                            .isEqualTo(EXPECTED_ROUTE_GEOMETRY_COORDINATE_ONE_LNG);
                    softAssertions
                            .assertThat(coordinateOne.getLatitude())
                            .as("coordinateOneLatitude")
                            .isEqualTo(EXPECTED_ROUTE_GEOMETRY_COORDINATE_ONE_LAT);

                    final LngLatAlt coordinateTwo = coordinates.get(1);
                    softAssertions
                            .assertThat(coordinateTwo.getLongitude())
                            .as("coordinateTwoLongitude")
                            .isEqualTo(EXPECTED_ROUTE_GEOMETRY_COORDINATE_TWO_LNG);
                    softAssertions
                            .assertThat(coordinateTwo.getLatitude())
                            .as("coordinateTwoLatitude")
                            .isEqualTo(EXPECTED_ROUTE_GEOMETRY_COORDINATE_TWO_LAT);

                    final LngLatAlt coordinateThree = coordinates.get(2);
                    softAssertions
                            .assertThat(coordinateThree.getLongitude())
                            .as("coordinateThreeLongitude")
                            .isEqualTo(EXPECTED_ROUTE_GEOMETRY_COORDINATE_THREE_LNG);
                    softAssertions
                            .assertThat(coordinateThree.getLatitude())
                            .as("coordinateThreeLatitude")
                            .isEqualTo(EXPECTED_ROUTE_GEOMETRY_COORDINATE_THREE_LAT);
                }

                @Test
                @DisplayName("Should return a route with the correct distance")
                void shouldReturnRouteWithCorrectDistance() {
                    final RouteDTO route = mapMatchingService
                            .sendMapMatchingRequest(routeGeometryInput, routePointsInput)
                            .getRoutes()
                            .get(0);
                    assertThat(route.getDistance()).isEqualTo(EXPECTED_ROUTE_DISTANCE);
                }

                @Test
                @DisplayName("Should return a route with the correct weight")
                void shouldReturnRouteWithCorrectWeight() {
                    final RouteDTO route = mapMatchingService
                            .sendMapMatchingRequest(routeGeometryInput, routePointsInput)
                            .getRoutes()
                            .get(0);
                    assertThat(route.getWeight()).isEqualTo(EXPECTED_ROUTE_WEIGHT);
                }

                @Test
                @DisplayName("Should return a route that has two infrastructure links")
                void shouldReturnRouteWithTwoInfrastructureLinks() {
                    final List<InfrastructureLinkDTO> infrastructureLinks = mapMatchingService
                            .sendMapMatchingRequest(routeGeometryInput, routePointsInput)
                            .getRoutes()
                            .get(0)
                            .getPaths();
                    assertThat(infrastructureLinks).hasSize(2);
                }

                @Test
                @DisplayName("Should return a route that has correct infrastructure link one")
                void shouldReturnRouteThatHasCorrectInfrastructureLinkOne(final SoftAssertions softAssertions) {
                    final InfrastructureLinkDTO infrastructureLink = mapMatchingService
                            .sendMapMatchingRequest(routeGeometryInput, routePointsInput)
                            .getRoutes()
                            .get(0)
                            .getPaths()
                            .get(0);

                    softAssertions
                            .assertThat(infrastructureLink.getMapMatchingInfrastructureLinkId())
                            .as("id")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_ID);
                    softAssertions
                            .assertThat(infrastructureLink.getExternalLinkRef().getInfrastructureSource())
                            .as("source")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_SOURCE);
                    softAssertions
                            .assertThat(infrastructureLink.getExternalLinkRef().getExternalLinkId())
                            .as("externalId")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_EXT_ID);
                    softAssertions
                            .assertThat(infrastructureLink.isTraversalForwards())
                            .as("traversalForwards")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_TRAVERSAL_FORWARDS);

                    final LineString geometry = infrastructureLink.getGeometry();

                    final List<LngLatAlt> coordinates = geometry.getCoordinates();
                    softAssertions.assertThat(coordinates).as("coordinateCount").hasSize(2);

                    final LngLatAlt coordinateOne = coordinates.get(0);
                    softAssertions
                            .assertThat(coordinateOne.getLongitude())
                            .as("coordinateOneLongitude")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_COORDINATE_ONE_LNG);
                    softAssertions
                            .assertThat(coordinateOne.getLatitude())
                            .as("coordinateOneLatitude")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_COORDINATE_ONE_LAT);

                    final LngLatAlt coordinateTwo = coordinates.get(1);
                    softAssertions
                            .assertThat(coordinateTwo.getLongitude())
                            .as("coordinateTwoLongitude")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_COORDINATE_TWO_LNG);
                    softAssertions
                            .assertThat(coordinateTwo.getLatitude())
                            .as("coordinateTwoLatitude")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_COORDINATE_TWO_LAT);

                    softAssertions
                            .assertThat(infrastructureLink.getDistance())
                            .as("distance")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_DISTANCE);
                    softAssertions
                            .assertThat(infrastructureLink.getWeight())
                            .as("weight")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_WEIGHT);

                    final Map<String, String> names = infrastructureLink.getInfrastructureLinkName();
                    assertThat(names.get(LANGUAGE_CODE_FINNISH))
                            .as("finnishName")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_FINNISH_NAME);
                    assertThat(names.get(LANGUAGE_CODE_SWEDISH))
                            .as("swedishName")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ONE_SWEDISH_NAME);
                }

                @Test
                @DisplayName("Should return a route that has correct infrastructure link two")
                void shouldReturnRouteThatHasCorrectInfrastructureLinkTwo(final SoftAssertions softAssertions) {
                    final InfrastructureLinkDTO infrastructureLink = mapMatchingService
                            .sendMapMatchingRequest(routeGeometryInput, routePointsInput)
                            .getRoutes()
                            .get(0)
                            .getPaths()
                            .get(1);

                    softAssertions
                            .assertThat(infrastructureLink.getMapMatchingInfrastructureLinkId())
                            .as("id")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_ID);
                    softAssertions
                            .assertThat(infrastructureLink.getExternalLinkRef().getInfrastructureSource())
                            .as("source")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_SOURCE);
                    softAssertions
                            .assertThat(infrastructureLink.getExternalLinkRef().getExternalLinkId())
                            .as("externalId")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_EXT_ID);
                    softAssertions
                            .assertThat(infrastructureLink.isTraversalForwards())
                            .as("traversalForwards")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_TRAVERSAL_FORWARDS);

                    final LineString geometry = infrastructureLink.getGeometry();

                    final List<LngLatAlt> coordinates = geometry.getCoordinates();
                    softAssertions.assertThat(coordinates).as("coordinateCount").hasSize(2);

                    final LngLatAlt coordinateOne = coordinates.get(0);
                    softAssertions
                            .assertThat(coordinateOne.getLongitude())
                            .as("coordinateOneLongitude")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_COORDINATE_ONE_LNG);
                    softAssertions
                            .assertThat(coordinateOne.getLatitude())
                            .as("coordinateOneLatitude")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_COORDINATE_ONE_LAT);

                    final LngLatAlt coordinateTwo = coordinates.get(1);
                    softAssertions
                            .assertThat(coordinateTwo.getLongitude())
                            .as("coordinateTwoLongitude")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_COORDINATE_TWO_LNG);
                    softAssertions
                            .assertThat(coordinateTwo.getLatitude())
                            .as("coordinateTwoLatitude")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_COORDINATE_TWO_LAT);

                    softAssertions
                            .assertThat(infrastructureLink.getDistance())
                            .as("distance")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_DISTANCE);
                    softAssertions
                            .assertThat(infrastructureLink.getWeight())
                            .as("weight")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_WEIGHT);

                    final Map<String, String> names = infrastructureLink.getInfrastructureLinkName();
                    assertThat(names.get(LANGUAGE_CODE_FINNISH))
                            .as("finnishName")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_FINNISH_NAME);
                    assertThat(names.get(LANGUAGE_CODE_SWEDISH))
                            .as("swedishName")
                            .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_TWO_SWEDISH_NAME);
                }
            }
        }
    }
}
