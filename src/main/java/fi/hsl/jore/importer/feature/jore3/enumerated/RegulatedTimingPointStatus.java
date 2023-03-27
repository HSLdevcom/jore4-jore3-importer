package fi.hsl.jore.importer.feature.jore3.enumerated;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

@JoreEnumeration(name = "Ajantasauspysäkki")
public enum RegulatedTimingPointStatus {

    NO(0, "Ei"),
    YES(1, "Kyllä"),
    YES_LOAD_TIME(2, "Kyllä, load time"),

    UNKNOWN(-9999, "Unknown");

    private static final Logger LOG = LoggerFactory.getLogger(RegulatedTimingPointStatus.class);

    private final int value;
    private final String description;

    RegulatedTimingPointStatus(final int value,
                               final String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRegulatedTimingPoint() {
        return this == YES || this == YES_LOAD_TIME;
    }

    public static Optional<RegulatedTimingPointStatus> of(final int i) {
        return Arrays.stream(values())
                     .filter(transportType -> transportType.value == i)
                     .findFirst();
    }

    public static Optional<RegulatedTimingPointStatus> of(final String s) {
        try {
            return of(Integer.parseInt(s));
        } catch (final NumberFormatException ignored) {
            LOG.warn("Could not parse TimingStopPoint from '{}'", s);
            return Optional.empty();
        }
    }
}
