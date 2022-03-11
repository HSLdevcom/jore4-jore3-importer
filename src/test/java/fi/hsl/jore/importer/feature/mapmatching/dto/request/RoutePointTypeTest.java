package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RoutePointTypeTest {

    @Nested
    @DisplayName("Transform node type into route point type")
    class FromNodeType {

        @DisplayName("Should return the correct route point type")
        @ParameterizedTest(name = "When the node type is: {0}, should return {1}")
        @ArgumentsSource(NodeTypeRoutePointTypeArgumentsProvider.class)
        void shouldReturnCorrectRoutePointType(final NodeType input, final RoutePointType expectedOutput) {
            final RoutePointType actual = RoutePointType.fromNodeType(input);
            assertThat(actual).isEqualTo(expectedOutput);
        }
    }

    private static class NodeTypeRoutePointTypeArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NodeType.BORDER, RoutePointType.OTHER),
                    Arguments.of(NodeType.CROSSROADS, RoutePointType.ROAD_JUNCTION),
                    Arguments.of(NodeType.STOP, RoutePointType.PUBLIC_TRANSPORT_STOP),
                    Arguments.of(NodeType.UNKNOWN, RoutePointType.OTHER)
            );
        }
    }
}
