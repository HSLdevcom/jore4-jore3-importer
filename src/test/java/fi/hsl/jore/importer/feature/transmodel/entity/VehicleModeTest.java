package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleModeTest {

    @Nested
    @DisplayName("of")
    class Of {

        @Nested
        @DisplayName("When the network type is metro track")
        class WhenNetworkTypeIsMetroTrack {

            @Test
            @DisplayName("Should return the vehicle mode: metro")
            void shouldReturnVehicleModeMetro() {
                final VehicleMode vehicleMode = VehicleMode.of(NetworkType.METRO_TRACK);
                assertThat(vehicleMode).isEqualTo(VehicleMode.METRO);
            }
        }

        @Nested
        @DisplayName("When the network type is railway")
        class WhenNetworkTypeIsRailway {

            @Test
            @DisplayName("Should return the vehicle mode: train")
            void shouldReturnVehicleModeTrain() {
                final VehicleMode vehicleMode = VehicleMode.of(NetworkType.RAILWAY);
                assertThat(vehicleMode).isEqualTo(VehicleMode.TRAIN);
            }
        }

        @Nested
        @DisplayName("When the network type is road")
        class WhenNetworkTypeIsRoad {

            @Test
            @DisplayName("Should return the vehicle mode: bus")
            void shouldReturnVehicleModeBus() {
                final VehicleMode vehicleMode = VehicleMode.of(NetworkType.ROAD);
                assertThat(vehicleMode).isEqualTo(VehicleMode.BUS);
            }
        }

        @Nested
        @DisplayName("When the network type is tram track")
        class WhenNetworkTypeIsTramTrack {

            @Test
            @DisplayName("Should return the vehicle mode: tram")
            void shouldReturnVehicleModeTram() {
                final VehicleMode vehicleMode = VehicleMode.of(NetworkType.TRAM_TRACK);
                assertThat(vehicleMode).isEqualTo(VehicleMode.TRAM);
            }
        }

        @Nested
        @DisplayName("When the network type is waterway")
        class WhenNetworkTypeIsWaterWay {

            @Test
            @DisplayName("Should return the vehicle mode: ferry")
            void shouldReturnVehicleModeFerry() {
                final VehicleMode vehicleMode = VehicleMode.of(NetworkType.WATERWAY);
                assertThat(vehicleMode).isEqualTo(VehicleMode.FERRY);
            }
        }

        @Nested
        @DisplayName("When the network type is unknown")
        class WhenNetworkTypeIsUnknown {

            @Test
            @DisplayName("Should return the vehicle mode: unknown")
            void shouldReturnVehicleModeUnknown() {
                final VehicleMode vehicleMode = VehicleMode.of(NetworkType.UNKNOWN);
                assertThat(vehicleMode).isEqualTo(VehicleMode.UNKNOWN);
            }
        }
    }
}
