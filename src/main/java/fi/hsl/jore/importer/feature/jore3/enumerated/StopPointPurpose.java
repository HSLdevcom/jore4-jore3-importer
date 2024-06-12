package fi.hsl.jore.importer.feature.jore3.enumerated;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreEnumeration;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JoreEnumeration(name = "Pysäkin käyttö")
public enum StopPointPurpose {
    NOT_IN_USE(0, "Ei"),
    FLEXIBLE(1, "Kutsu"),
    ALIGHTING(2, "Jättö"),
    BOARDING(3, "Otto"),

    UNKNOWN(-9999, "Unknown"),
    ;

    private static final Logger LOG = LoggerFactory.getLogger(StopPointPurpose.class);

    private final int value;
    private final String description;

    StopPointPurpose(final int value, final String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<StopPointPurpose> of(final int i) {
        return Arrays.stream(values())
                .filter(transportType -> transportType.value == i)
                .findFirst();
    }

    public static Optional<StopPointPurpose> of(final String s) {
        try {
            return of(Integer.parseInt(s));
        } catch (final NumberFormatException ignored) {
            LOG.warn("Could not parse StopPointPurpose from '{}'", s);
            return Optional.empty();
        }
    }
}
