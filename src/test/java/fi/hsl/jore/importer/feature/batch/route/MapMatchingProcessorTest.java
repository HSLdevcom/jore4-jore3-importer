package fi.hsl.jore.importer.feature.batch.route;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static fi.hsl.jore.importer.feature.mapmatching.service.RouteGeometryTestFactory.createRouteGeometry;
import static fi.hsl.jore.importer.feature.mapmatching.service.RouteGeometryTestFactory.createRoutePoints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteGeometry;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteInfrastructureLink;
import fi.hsl.jore.importer.feature.mapmatching.service.IMapMatchingService;
import fi.hsl.jore.importer.feature.mapmatching.service.MapMatchingService;
import fi.hsl.jore.importer.feature.mapmatching.service.RouteGeometryTestFactory;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.repository.IRoutePointExportRepository;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;

class MapMatchingProcessorTest {

    private static final String MAP_MATCHING_API_BASE_URL_TEMPLATE = "http://localhost:%d";

    private WireMockServer wireMockServer;

    private MapMatchingProcessor processor;
    private IRoutePointExportRepository repository;

    private ImporterRouteGeometry routeGeometryInput;

    @BeforeEach
    void configureSystemUnderTest() {
        repository = mock(IRoutePointExportRepository.class);

        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        configureFor(wireMockServer.port());

        final IMapMatchingService mapMatchingService = new MapMatchingService(
                String.format(MAP_MATCHING_API_BASE_URL_TEMPLATE, wireMockServer.port()),
                new ObjectMapper(),
                new RestTemplate());

        processor = new MapMatchingProcessor(mapMatchingService, repository);

        routeGeometryInput = createRouteGeometry();
    }

    @AfterEach
    void stopWireMockServer() {
        wireMockServer.stop();
    }

    @Nested
    @DisplayName("When no route points are found")
    class WhenNoRoutePointsAreFound {

        @BeforeEach
        void returnEmptyList() {
            given(repository.findImporterRoutePointsByRouteDirectionId(RouteGeometryTestFactory.ROUTE_DIRECTION_ID))
                    .willReturn(Collections.emptyList());
        }

        @Test
        @DisplayName("Should return null")
        void shouldReturnNull() throws Exception {
            final Jore4RouteGeometry routeGeometry = processor.process(routeGeometryInput);
            assertThat(routeGeometry).isNull();
        }
    }

    @Nested
    @DisplayName("When one route point is found")
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenOneRoutePointIsFound {
        private static final String MAP_MATCHING_RESPONSE =
                """
                {
                  "code": "Ok",
                  "routes": [
                    {
                      "geometry": {
                        "type": "LineString",
                        "coordinates": [
                          [24.92746831478124, 60.16364653019382],
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

        private static final String EXPECTED_FIRST_INFRASTRUCTURE_LINK_SOURCE = "digiroad_r";
        private static final String EXPECTED_FIRST_INFRASTRUCTURE_LINK_EXT_ID = "445117";
        private static final int EXPECTED_FIRST_INFRASTRUCTURE_LINK_SEQUENCE = 0;
        private static final boolean EXPECTED_FIRST_INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS = true;

        private static final String EXPECTED_SECOND_INFRASTRUCTURE_LINK_SOURCE = "digiroad_r";
        private static final String EXPECTED_SECOND_INFRASTRUCTURE_LINK_EXT_ID = "442423";
        private static final int EXPECTED_SECOND_INFRASTRUCTURE_LINK_SEQUENCE = 1;
        private static final boolean EXPECTED_SECOND_INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS = true;

        @BeforeEach
        void configureSystemUnderTest() {
            returnOneRoutePoint();
            returnMapMatchingResponse();
        }

        void returnOneRoutePoint() {
            final List<ImporterRoutePoint> routePoints = createRoutePoints();
            given(repository.findImporterRoutePointsByRouteDirectionId(RouteGeometryTestFactory.ROUTE_DIRECTION_ID))
                    .willReturn(routePoints);
        }

        void returnMapMatchingResponse() {
            givenThat(post(urlEqualTo("/api/match/public-transport-route/v1/bus.json"))
                    .willReturn(okJson(MAP_MATCHING_RESPONSE)));
        }

        @Test
        @DisplayName("Should return a route geometry with the correct route id")
        void shouldReturnRouteGeometryWithCorrectRouteId() throws Exception {
            final Jore4RouteGeometry routeGeometry = processor.process(routeGeometryInput);
            assertThat(routeGeometry.routeId()).isEqualTo(RouteGeometryTestFactory.ROUTE_JORE4_ID);
        }

        @Test
        @DisplayName("Should return a route geometry with two infrastructure links")
        void shouldReturnRouteGeometryWithTwoInfrastructureLinks() throws Exception {
            final Jore4RouteGeometry routeGeometry = processor.process(routeGeometryInput);
            assertThat(routeGeometry.infrastructureLinks()).hasSize(2);
        }

        @Test
        @DisplayName("Should return route geometry with the correct first infrastructure link")
        void shouldReturnRouteGeometryWithCorrectFirstInfrastructureLink(final SoftAssertions softAssertions)
                throws Exception {
            final Jore4RouteInfrastructureLink infrastructureLink =
                    processor.process(routeGeometryInput).infrastructureLinks().get(0);

            softAssertions
                    .assertThat(infrastructureLink.infrastructureLinkSource())
                    .as("infrastructureLinkSource")
                    .isEqualTo(EXPECTED_FIRST_INFRASTRUCTURE_LINK_SOURCE);
            softAssertions
                    .assertThat(infrastructureLink.infrastructureLinkExtId())
                    .as("infrastructureLinkExtId")
                    .isEqualTo(EXPECTED_FIRST_INFRASTRUCTURE_LINK_EXT_ID);
            softAssertions
                    .assertThat(infrastructureLink.infrastructureLinkSequence())
                    .as("infrastructureLinkSequence")
                    .isEqualTo(EXPECTED_FIRST_INFRASTRUCTURE_LINK_SEQUENCE);
            softAssertions
                    .assertThat(infrastructureLink.isTraversalForwards())
                    .as("isTraversalForwards")
                    .isEqualTo(EXPECTED_FIRST_INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS);
        }

        @Test
        @DisplayName("Should return route geometry with the correct second infrastructure link")
        void shouldReturnRouteGeometryWithCorrectSecondInfrastructureLink(final SoftAssertions softAssertions)
                throws Exception {
            final Jore4RouteInfrastructureLink infrastructureLink =
                    processor.process(routeGeometryInput).infrastructureLinks().get(1);

            softAssertions
                    .assertThat(infrastructureLink.infrastructureLinkSource())
                    .as("infrastructureLinkSource")
                    .isEqualTo(EXPECTED_SECOND_INFRASTRUCTURE_LINK_SOURCE);
            softAssertions
                    .assertThat(infrastructureLink.infrastructureLinkExtId())
                    .as("infrastructureLinkExtId")
                    .isEqualTo(EXPECTED_SECOND_INFRASTRUCTURE_LINK_EXT_ID);
            softAssertions
                    .assertThat(infrastructureLink.infrastructureLinkSequence())
                    .as("infrastructureLinkSequence")
                    .isEqualTo(EXPECTED_SECOND_INFRASTRUCTURE_LINK_SEQUENCE);
            softAssertions
                    .assertThat(infrastructureLink.isTraversalForwards())
                    .as("isTraversalForwards")
                    .isEqualTo(EXPECTED_SECOND_INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS);
        }
    }
}
