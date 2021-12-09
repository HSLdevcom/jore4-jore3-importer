package fi.hsl.jore.importer;

import java.time.LocalTime;
import java.time.ZoneId;

/**
 *  Contains constants which are used by our
 *  automated tests.
 */
public final class TestConstants {

    public static final ZoneId LOCAL_TIME_ZONE = ZoneId.of("Europe/Helsinki");
    public static final LocalTime OPERATING_DAY_START_TIME = LocalTime.of(4, 30);
    public static final LocalTime OPERATING_DAY_END_TIME = LocalTime.of(4, 29, 59);

    /**
     * Prevents instantiation.
     */
    private TestConstants() {}
}
