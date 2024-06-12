package fi.hsl.jore.importer.feature.network.direction_type.field;

import java.util.Arrays;

public enum DirectionType {
    INBOUND("inbound"),
    OUTBOUND("outbound"),
    CLOCKWISE("clockwise"),
    ANTICLOCKWISE("anticlockwise"),
    UNKNOWN("unknown"),
    ;

    private final String label;

    DirectionType(final String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static DirectionType of(final String val) {
        return Arrays.stream(values())
                .filter(type -> type.label.equalsIgnoreCase(val))
                .findFirst()
                .orElseThrow();
    }
}
