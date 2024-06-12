package fi.hsl.jore.importer.feature.jore3.enumerated;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreEnumeration;
import java.util.Arrays;
import java.util.Optional;

@JoreEnumeration(name = "Solmutyyppi (P/E)")
public enum NodeType {
    BUS_STOP('P'),
    BUS_STOP_NOT_IN_USE('E'), // Appears only in jr_reitinlinkki
    CROSSROADS('X'),
    MUNICIPAL_BORDER('-'),
    UNKNOWN('?');

    private final char jrValue;

    NodeType(final char val) {
        jrValue = val;
    }

    public static Optional<NodeType> of(final char i) {
        return Arrays.stream(values()).filter(transitType -> transitType.jrValue == i).findFirst();
    }

    public static Optional<NodeType> of(final String s) {
        try {
            return of(s.charAt(0));
        } catch (final StringIndexOutOfBoundsException ignored) {
            return Optional.empty();
        }
    }
}
