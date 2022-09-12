package fi.hsl.jore.importer.feature.jore4.entity;

import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;

import java.util.Arrays;

/**
 * Specifies the direction of a route.
 */
public enum Jore4RouteDirection {

    INBOUND("inbound"),
    OUTBOUND("outbound"),
    CLOCKWISE("clockwise"),
    ANTICLOCKWISE("anticlockwise");

    private final String value;

    Jore4RouteDirection(final String value) {
        this.value = value;
    }

    /**
     * Returns the value that's inserted into the Jore 4 database.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns a new enum value which matches with the direction type
     * given as a method parameter.
     * @param       directionType Specifies the requested {@link Jore4RouteDirection}.
     * @throws      NoSuchElementException  if no enum value is found with the given direction type.
     */
    public static Jore4RouteDirection of(final DirectionType directionType) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(directionType.label()))
                .findFirst()
                .orElseThrow();
    }
}
