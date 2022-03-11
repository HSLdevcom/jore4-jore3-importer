package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.mapmatching.dto.request.RoutePointType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoutePointTypeTest {

    @Nested
    @DisplayName("Transform node type into route point type")
    class FromNodeType {

        @Nested
        @DisplayName("When the node type isn't supported")
        class WhenNodeTypeIsNotSupported {

            @DisplayName("Should throw an exception")
            @ParameterizedTest(name = "When the node type is: {0}, should throw IllegalArgumentException")
            @EnumSource(value = NodeType.class, names = {"BORDER", "UNKNOWN"})
            void shouldThrowException(final NodeType input) {
                assertThatThrownBy(() -> RoutePointType.fromNodeType(input))
                        .isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("When the node type is supported")
        class WhenNodeTypeIsSupported {

            @DisplayName("Should return the correct route point type")
            @ParameterizedTest(name = "When the node type is: {0}, should return {1}")
            @ArgumentsSource(NodeTypeRoutePointTypeArgumentsProvider.class)
            void shouldReturnCorrectRoutePointType(final NodeType input, final RoutePointType expectedOutput) {
                final RoutePointType actual = RoutePointType.fromNodeType(input);
                assertThat(actual).isEqualTo(expectedOutput);
            }
        }
    }

    private static class NodeTypeRoutePointTypeArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(NodeType.CROSSROADS, RoutePointType.ROAD_JUNCTION),
                    Arguments.of(NodeType.STOP, RoutePointType.PUBLIC_TRANSPORT_STOP)
            );
        }
    }
}
