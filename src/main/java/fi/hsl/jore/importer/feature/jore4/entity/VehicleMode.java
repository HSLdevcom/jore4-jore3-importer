package fi.hsl.jore.importer.feature.jore4.entity;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;

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
