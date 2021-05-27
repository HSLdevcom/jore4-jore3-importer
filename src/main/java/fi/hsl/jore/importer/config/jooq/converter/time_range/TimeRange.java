package fi.hsl.jore.importer.config.jooq.converter.time_range;

import com.google.common.collect.Range;
import org.immutables.value.Value;

import java.time.Instant;


@Value.Immutable
public abstract class TimeRange {
    public abstract Range<Instant> range();

    @Override
    public String toString() {
        return range().toString();
    }

    public static TimeRange empty() {
        return ImmutableTimeRange.builder()
                                 .range(Range.closedOpen(Instant.EPOCH, Instant.EPOCH))
                                 .build();
    }

    public static TimeRange unbounded() {
        return ImmutableTimeRange.builder()
                                 .range(Range.all())
                                 .build();
    }

    public static TimeRange of(final Range<Instant> range) {
        return ImmutableTimeRange.builder()
                                 .range(range)
                                 .build();
    }
}
