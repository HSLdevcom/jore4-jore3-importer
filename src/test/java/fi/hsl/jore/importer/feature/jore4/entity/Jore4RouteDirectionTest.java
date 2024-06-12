package fi.hsl.jore.importer.feature.jore4.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class Jore4RouteDirectionTest {

    @Nested
    @DisplayName("Of")
    class Of {

        @Nested
        @DisplayName("When the direction type is known")
        class WhenDirectionTypeIsKnown {

            @DisplayName("Create a new Jore 4 route direction from direction type")
            @ParameterizedTest(name = "When the direction is: {0}, the Jore 4 route direction should be: {1}")
            @ArgumentsSource(DirectionTypeArgumentsProvider.class)
            void shouldReturnCorrectJore4RouteDirection(final DirectionType input, final Jore4RouteDirection expected) {
                final Jore4RouteDirection actual = Jore4RouteDirection.of(input);
                assertThat(actual).isEqualTo(expected);
            }
        }

        @Nested
        @DisplayName("When the direction type is unknown")
        class WhenDirectionTypeIsUnknown {

            @Test
            @DisplayName("Should throw an exception")
            void shouldThrowException() {
                assertThatThrownBy(() -> Jore4RouteDirection.of(DirectionType.UNKNOWN))
                        .isExactlyInstanceOf(NoSuchElementException.class);
            }
        }
    }

    private static class DirectionTypeArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(DirectionType.ANTICLOCKWISE, Jore4RouteDirection.ANTICLOCKWISE),
                    Arguments.of(DirectionType.CLOCKWISE, Jore4RouteDirection.CLOCKWISE),
                    Arguments.of(DirectionType.INBOUND, Jore4RouteDirection.INBOUND),
                    Arguments.of(DirectionType.OUTBOUND, Jore4RouteDirection.OUTBOUND));
        }
    }
}
