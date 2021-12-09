package fi.hsl.jore.importer.feature.transmodel.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class TimestampFactory {

    private static final String LOCAL_TIME_ZONE = "Europe/Helsinki";

    /**
     * Prevents instantiation.
     */
    private TimestampFactory() {}

    /**
     * Creates an {@code OffsetDateTime} object by using the local timestamp and
     * time zone (Europe/Helsinki).
     * @param input The local timestamp.
     * @return  The created timestamp with offset.
     */
    public static OffsetDateTime offsetDateTimeFromLocalDateTime(final LocalDateTime input) {
        final ZoneId localTimeZone = ZoneId.of(LOCAL_TIME_ZONE);
        return OffsetDateTime.of(
                input,
                localTimeZone.getRules().getOffset(input)
        );
    }
}
