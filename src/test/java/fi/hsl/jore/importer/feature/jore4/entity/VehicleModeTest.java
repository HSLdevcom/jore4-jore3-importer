package fi.hsl.jore.importer.feature.jore4.entity;

import static org.assertj.core.api.Assertions.assertThat;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class VehicleModeTest {

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
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
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
