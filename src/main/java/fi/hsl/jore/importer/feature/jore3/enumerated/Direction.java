package fi.hsl.jore.importer.feature.jore3.enumerated;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreEnumeration;
import java.util.Arrays;
import java.util.Optional;

@JoreEnumeration(name = "Suunta")
public enum Direction {
    DIRECTION_1(1, "Suunta 1"),
    DIRECTION_2(2, "Suunta 2"),

    UNKNOWN(-9999, "Unknown direction"),
    ;

    private final int value;
    private final String description;

    Direction(final int value, final String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int value() {
        return value;
    }

    public static Optional<Direction> of(final int value) {
        return Arrays.stream(values()).filter(direction -> direction.value == value).findFirst();
    }
}
