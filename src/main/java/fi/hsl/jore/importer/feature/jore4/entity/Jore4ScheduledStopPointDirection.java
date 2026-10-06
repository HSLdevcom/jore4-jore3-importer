package fi.hsl.jore.importer.feature.jore4.entity;

import java.util.Arrays;
import java.util.Optional;

/** Specifies the direction of the scheduled stop point on an infrastructure link. */
public enum Jore4ScheduledStopPointDirection {
    BACKWARD("backward"),
    FORWARD("forward");

    private final String value;

    Jore4ScheduledStopPointDirection(final String value) {
        this.value = value;
    }

    /** @return The value which is inserted into the Jore 4 database. */
    public String getValue() {
        return value;
    }

    /**
     * @return The direction whose database value equals the given value, or an empty optional if no such direction
     *     exists.
     */
    public static Optional<Jore4ScheduledStopPointDirection> fromValue(final String value) {
        return Arrays.stream(values())
                .filter(direction -> direction.value.equals(value))
                .findFirst();
    }
}
