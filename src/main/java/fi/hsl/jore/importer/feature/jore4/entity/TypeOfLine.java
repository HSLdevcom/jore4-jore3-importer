package fi.hsl.jore.importer.feature.jore4.entity;

import java.util.Arrays;

/** Contains the line types found from the Jore 4 database. */
public enum TypeOfLine {
    REGIONAL_RAIL_SERVICE("regional_rail_service"),
    SUBURBAN_RAILWAY("suburban_railway"),
    METRO_SERVICE("metro_service"),
    REGIONAL_BUS_SERVICE("regional_bus_service"),
    EXPRESS_BUS_SERVICE("express_bus_service"),
    STOPPING_BUS_SERVICE("stopping_bus_service"),
    SPECIAL_NEEDS_BUS("special_needs_bus"),
    DEMAND_AND_RESPONSE_BUS_SERVICE("demand_and_response_bus_service"),
    CITY_TRAM_SERVICE("city_tram_service"),
    REGIONAL_TRAM_SERVICE("regional_tram_service"),
    FERRY_SERVICE("ferry_service");

    private final String value;

    TypeOfLine(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TypeOfLine of(final String val) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(val))
                .findFirst()
                .orElseThrow();
    }
}
