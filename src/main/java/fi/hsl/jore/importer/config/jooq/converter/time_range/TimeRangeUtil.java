package fi.hsl.jore.importer.config.jooq.converter.time_range;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

import java.time.Instant;
import java.util.Optional;

public final class TimeRangeUtil {

    public static final TimeRange EMPTY = TimeRange.empty();

    public static final TimeRange UNBOUNDED = TimeRange.unbounded();

    public static TimeRange from(final Optional<Instant> from,
                                 final Optional<Instant> to,
                                 final boolean startInclusive,
                                 final boolean endInclusive) {
        if (from.isPresent()) {
            if (to.isPresent()) {
                return TimeRange.of(Range.range(from.orElseThrow(),
                                                startInclusive ? BoundType.CLOSED : BoundType.OPEN,
                                                to.orElseThrow(),
                                                endInclusive ? BoundType.CLOSED : BoundType.OPEN));
            }
            return TimeRange.of(startInclusive ? Range.atLeast(from.orElseThrow()) : Range.greaterThan(from.orElseThrow()));
        }
        if (to.isPresent()) {
            return TimeRange.of(endInclusive ? Range.atMost(to.orElseThrow()) : Range.lessThan(to.orElseThrow()));
        }
        return UNBOUNDED;
    }

    private TimeRangeUtil() {
    }
}
