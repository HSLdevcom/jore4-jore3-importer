package fi.hsl.jore.importer.feature.infrastructure.node.dto;

import java.util.Arrays;

public enum NodeType {
    STOP("S"),
    CROSSROADS("X"),
    BORDER("B"),
    UNKNOWN("U");
    private final String value;

    NodeType(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static NodeType of(final String val) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(val))
                .findFirst()
                .orElseThrow();
    }
}
