package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import fi.hsl.jore.importer.feature.mapmatching.dto.request.RoutePointLocationRequestDTO;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

class RoutePointLocationRequestDTOTest {

    @Nested
    @DisplayName("Get type")
    class GetType {

        private static final String EXPECTED_TYPE = "Point";

        @Test
        @DisplayName("Should return the expected type")
        void shouldReturnExpectedType() {
            final RoutePointLocationRequestDTO location = new RoutePointLocationRequestDTO();
            assertThat(location.getType()).isEqualTo(EXPECTED_TYPE);
        }
    }

    @Nested
    @DisplayName("Set coordinates")
    @ExtendWith(SoftAssertionsExtension.class)
    class SetCoordinates {

        private static final double X_COORDINATE = 5.7;
        private static final double Y_COORDINATE = 10.1;

        @Test
        @DisplayName("Should set the correct X and Y coordinate")
        void shouldSetCorrectXAndYCoordinate(final SoftAssertions softAssertions) {
            final RoutePointLocationRequestDTO location = new RoutePointLocationRequestDTO();
            location.setCoordinates(X_COORDINATE, Y_COORDINATE);

            final double[] coordinates = location.getCoordinates();

            softAssertions.assertThat(coordinates[0])
                    .as("coordinateX")
                    .isEqualTo(X_COORDINATE);
            softAssertions.assertThat(coordinates[1])
                    .as("coordinateY")
                    .isEqualTo(Y_COORDINATE);
        }
    }
}
