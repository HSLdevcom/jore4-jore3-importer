package fi.hsl.jore.importer.feature.jore4.entity;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import java.util.Optional;

/** Contains the vehicle modes found from the Jore 4 database. */
public enum VehicleMode {
    BUS("bus"),
    TRAM("tram"),
    TRAIN("train"),
    METRO("metro"),
    FERRY("ferry"),
    UNKNOWN("unknown");

    private final String value;

    VehicleMode(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Returns the Jore 4 vehicle submode whose infrastructure links are used for scheduled stop points of this vehicle
     * mode. Only bus and tram are supported; other modes return an empty optional and are treated as unresolvable.
     */
    public Optional<String> vehicleSubmode() {
        return switch (this) {
            case BUS -> Optional.of("generic_bus");
            case TRAM -> Optional.of("generic_tram");
            case TRAIN -> Optional.of("generic_train");
            case METRO -> Optional.of("generic_metro");
            case FERRY -> Optional.of("generic_ferry");
            default -> Optional.empty();
        };
    }

    public static VehicleMode of(final NetworkType networkType) {
        switch (networkType) {
            case METRO_TRACK:
                return METRO;
            case RAILWAY:
                return TRAIN;
            case ROAD:
                return BUS;
            case TRAM_TRACK:
                return TRAM;
            case WATERWAY:
                return FERRY;
            default:
                return UNKNOWN;
        }
    }
}
