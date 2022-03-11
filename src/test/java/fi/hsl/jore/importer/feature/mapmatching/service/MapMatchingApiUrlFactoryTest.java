package fi.hsl.jore.importer.feature.mapmatching.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MapMatchingApiUrlFactoryTest {

    @Nested
    @DisplayName("Build the URL of the map matching API")
    class BuildMapMatchingApiUrl {

        private static final String EXPECTED_API_URL = "https://localhost:3005/api/match/public-transport-route/v1/bus.json";

        @Nested
        @DisplayName("When the base URL doesn't end with slash")
        class WhenBaseUrlDoesNotEndWithSlash {

            private static final String API_BASE_URL = "https://localhost:3005";

            @Test
            @DisplayName("Should return the full URL of the map matching API")
            void shouldReturnFullUrlOfMapMatchingAPI() {
                final String actualUrl = MapMatchingApiUrlFactory.buildMapMatchingApiUrl(API_BASE_URL);
                assertThat(actualUrl).isEqualTo(EXPECTED_API_URL);
            }
        }

        @Nested
        @DisplayName("When the base URL ends with slash")
        class WhenBaseUrlEndsWithSlash {

            private static final String API_BASE_URL = "https://localhost:3005/";

            @Test
            @DisplayName("Should return the full URL of the map matching API")
            void shouldReturnFullUrlOfMapMatchingAPI() {
                final String actualUrl = MapMatchingApiUrlFactory.buildMapMatchingApiUrl(API_BASE_URL);
                assertThat(actualUrl).isEqualTo(EXPECTED_API_URL);
            }
        }
    }
}
