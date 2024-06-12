package fi.hsl.jore.importer.config.jooq.converter.time_range;

import fi.hsl.jore.importer.config.jooq.converter.util.RangeUtil;
import java.time.Instant;
import java.util.Optional;

public final class TimeRangeUtil {

    public static final TimeRange EMPTY = TimeRange.empty();

    public static final TimeRange UNBOUNDED = TimeRange.unbounded();

    public static TimeRange asTimeRange(
            final Optional<Instant> maybeFrom,
            final Optional<Instant> maybeTo,
            final boolean startInclusive,
            final boolean endInclusive) {
        return TimeRange.of(RangeUtil.asRange(maybeFrom, maybeTo, startInclusive, endInclusive));
    }

    private TimeRangeUtil() {}
}
