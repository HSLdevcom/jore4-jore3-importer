package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import fi.hsl.jore.importer.feature.mapmatching.dto.request.RouteGeometryRequestDTO;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

class RouteGeometryRequestDTOTest {

    @Nested
    @DisplayName("Add coordinate")
    @ExtendWith(SoftAssertionsExtension.class)
    class AddCoordinate {

        private static final double X_COORDINATE = 5.7;
        private static final double Y_COORDINATE = 10.1;

        private RouteGeometryRequestDTO geometry;

        @BeforeEach
        void createGeometry() {
            geometry = new RouteGeometryRequestDTO();
        }

        @Test
        @DisplayName("Should add a new coordinate to route geometry")
        void shouldAddNewCoordinateToRouteGeometry() {
            geometry.addCoordinate(X_COORDINATE, Y_COORDINATE);
            assertThat(geometry.getCoordinates()).hasSize(1);
        }

        @Test
        @DisplayName("Should add a new coordinate with the correct X and Y coordinates")
        void shouldAddNewCoordinateWithCorrectXAndYCoordinates(final SoftAssertions softAssertions) {
            geometry.addCoordinate(X_COORDINATE, Y_COORDINATE);

            final double[] coordinate = geometry.getCoordinates().get(0);

            softAssertions.assertThat(coordinate[0])
                    .as("coordinateX")
                    .isEqualTo(X_COORDINATE);
            softAssertions.assertThat(coordinate[1])
                    .as("coordinateY")
                    .isEqualTo(Y_COORDINATE);
        }
    }

    @Nested
    @DisplayName("Get type")
    class GetType {

        private static final String EXPECTED_TYPE = "LineString";

        @Test
        @DisplayName("Should return the correct type")
        void shouldReturnCorrectType() {
            final RouteGeometryRequestDTO geometry = new RouteGeometryRequestDTO();
            assertThat(geometry.getType()).isEqualTo(EXPECTED_TYPE);
        }
    }
}
