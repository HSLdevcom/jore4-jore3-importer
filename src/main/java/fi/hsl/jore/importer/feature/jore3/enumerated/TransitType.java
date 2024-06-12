package fi.hsl.jore.importer.feature.jore3.enumerated;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreEnumeration;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JoreEnumeration(name = "Verkko")
public enum TransitType {
    BUS(1, "Linja-auto verkko"),
    SUBWAY(2, "Metroverkko"),
    TRAM(3, "Raitiovaunuverkko"),
    TRAIN(4, "Junaverkko"),
    FERRY(7, "Vesitieverkko"),

    UNKNOWN(-1, "Unknown"),
    ;

    private static final Logger LOG = LoggerFactory.getLogger(TransitType.class);

    private final int value;
    private final String description;

    TransitType(final int value, final String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int value() {
        return value;
    }

    public static Optional<TransitType> of(final int i) {
        return Arrays.stream(values()).filter(transitType -> transitType.value == i).findFirst();
    }

    public static Optional<TransitType> of(final String s) {
        try {
            return of(Integer.parseInt(s));
        } catch (final NumberFormatException ignored) {
            LOG.warn("Could not parse TransitType from '{}'", s);
            return Optional.empty();
        }
    }
}
