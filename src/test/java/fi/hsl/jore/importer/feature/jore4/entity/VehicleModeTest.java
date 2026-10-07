package fi.hsl.jore.importer.feature.jore4.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.support.ParameterDeclarations;

class VehicleModeTest {

    @ParameterizedTest
    @CsvSource({
        "1, TRAM",
        "2, BUS",
        "3, BUS",
        "4, BUS",
        "5, BUS",
        "6, BUS",
        "7, UNKNOWN",
        "8, BUS",
        "9, BUS",
        "10, BUS",
        "11, BUS"
    })
    void shouldMapIntegerAndStringStopTypes(final int stopType, final VehicleMode expected) {
        assertThat(VehicleMode.ofStopType(stopType)).isEqualTo(expected);
        assertThat(VehicleMode.ofStopType(Integer.toString(stopType))).isEqualTo(expected);
        assertThat(VehicleMode.ofStopType(String.format("%02d", stopType))).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 12, 99, Integer.MAX_VALUE})
    void shouldRejectUnsupportedStopTypes(final int stopType) {
        assertThatThrownBy(() -> VehicleMode.ofStopType(stopType)).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> VehicleMode.ofStopType(Integer.toString(stopType)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"abc", "1.0", "[1]", " ", "2147483648"})
    void shouldRejectMalformedStopTypes(final String stopType) {
        assertThatThrownBy(() -> VehicleMode.ofStopType(stopType)).isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    @DisplayName("of")
    class Of {

        @DisplayName(("Create a new vehicle mode from the network type"))
        @ParameterizedTest(name = "When the network type is: {0}, the vehicle mode should be: {1}")
        @ArgumentsSource(NetworkVehicleModeArgumentsProvider.class)
        void shouldReturnCorrectVehicleMode(
                final NetworkType sourceNetworkType, final VehicleMode expectedVehicleMode) {
            final VehicleMode vehicleMode = VehicleMode.of(sourceNetworkType);
            assertThat(vehicleMode).isEqualTo(expectedVehicleMode);
        }
    }

    private static class NetworkVehicleModeArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
                throws Exception {
            return Stream.of(
                    Arguments.of(NetworkType.METRO_TRACK, VehicleMode.METRO),
                    Arguments.of(NetworkType.RAILWAY, VehicleMode.TRAIN),
                    Arguments.of(NetworkType.ROAD, VehicleMode.BUS),
                    Arguments.of(NetworkType.TRAM_TRACK, VehicleMode.TRAM),
                    Arguments.of(NetworkType.WATERWAY, VehicleMode.FERRY),
                    Arguments.of(NetworkType.UNKNOWN, VehicleMode.UNKNOWN));
        }
    }
}
