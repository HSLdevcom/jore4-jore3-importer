package fi.hsl.jore.importer.feature.jore.field;

import java.util.Arrays;
import java.util.Optional;

public enum NodeType {
    BUS_STOP('P'),
    CROSSROADS('X'),
    MUNICIPAL_BORDER('-'),
    UNKNOWN('?');

    private final char jrValue;

    NodeType(final char val) {
        jrValue = val;
    }

    public static Optional<NodeType> of(final char i) {
        return Arrays.stream(values())
                     .filter(transitType -> transitType.jrValue == i)
                     .findFirst();
    }

    public static Optional<NodeType> of(final String s) {
        try {
            return of(s.charAt(0));
        } catch (final StringIndexOutOfBoundsException ignored) {
            return Optional.empty();
        }
    }
}
