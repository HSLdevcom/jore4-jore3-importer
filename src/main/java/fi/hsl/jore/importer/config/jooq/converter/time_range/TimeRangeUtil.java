package fi.hsl.jore.importer.config.jooq.converter.time_range;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

import java.time.Instant;
import java.util.Optional;

public final class TimeRangeUtil {

    public static final TimeRange EMPTY = TimeRange.empty();

    public static final TimeRange UNBOUNDED = TimeRange.unbounded();

    private static TimeRange between(final Instant from,
                                     final Instant to,
                                     final boolean startInclusive,
                                     final boolean endInclusive) {
        return TimeRange.of(Range.range(from,
                                        startInclusive ? BoundType.CLOSED : BoundType.OPEN,
                                        to,
                                        endInclusive ? BoundType.CLOSED : BoundType.OPEN));
    }

    private static TimeRange beginningFrom(final Instant from,
                                           final boolean startInclusive) {
        return TimeRange.of(startInclusive ? Range.atLeast(from) : Range.greaterThan(from));
    }

    private static TimeRange endingTo(final Instant to,
                                      final boolean endInclusive) {
        return TimeRange.of(endInclusive ? Range.atMost(to) : Range.lessThan(to));
    }

    public static TimeRange asTimeRange(final Optional<Instant> maybeFrom,
                                        final Optional<Instant> maybeTo,
                                        final boolean startInclusive,
                                        final boolean endInclusive) {
        return maybeFrom
                .map(from -> maybeTo.map(to -> between(from, to, startInclusive, endInclusive))
                                    .orElse(beginningFrom(from, startInclusive)))
                .orElse(maybeTo.map(to -> endingTo(to, endInclusive))
                               .orElse(UNBOUNDED));
    }

    private TimeRangeUtil() {
    }
}
