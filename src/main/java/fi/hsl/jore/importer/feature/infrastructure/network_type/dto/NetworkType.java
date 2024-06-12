package fi.hsl.jore.importer.feature.infrastructure.network_type.dto;

import java.util.Arrays;

public enum NetworkType {
    ROAD("road"),
    RAILWAY("railway"),
    TRAM_TRACK("tram_track"),
    METRO_TRACK("metro_track"),
    WATERWAY("waterway"),
    UNKNOWN("unknown"),
    ;

    private final String label;

    NetworkType(final String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static NetworkType of(final String val) {
        return Arrays.stream(values())
                .filter(type -> type.label.equalsIgnoreCase(val))
                .findFirst()
                .orElseThrow();
    }
}
