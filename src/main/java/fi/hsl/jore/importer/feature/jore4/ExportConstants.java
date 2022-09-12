package fi.hsl.jore.importer.feature.jore4;

import java.time.LocalTime;

/**
 * Contains constants which are used when we export
 * data to the Jore 4 database.
 */
public final class ExportConstants {

    public static final String LOCAL_TIME_ZONE = "Europe/Helsinki";
    public static final LocalTime OPERATING_DAY_START_TIME = LocalTime.of(4, 30);
    public static final LocalTime OPERATING_DAY_END_TIME = LocalTime.of(4, 29, 59);

    /**
     * Prevents instantiation.
     */
    private ExportConstants() {}
}
