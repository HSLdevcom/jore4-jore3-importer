package fi.hsl.jore.importer.config.jooq.converter.date_range;

import com.google.common.collect.Range;
import org.immutables.value.Value;

import java.time.LocalDate;


@Value.Immutable
public abstract class DateRange {
    public abstract Range<LocalDate> range();

    @Override
    public String toString() {
        return range().toString();
    }

    public static DateRange empty() {
        return ImmutableDateRange.builder()
                                 .range(Range.closedOpen(LocalDate.EPOCH, LocalDate.EPOCH))
                                 .build();
    }

    public static DateRange unbounded() {
        return ImmutableDateRange.builder()
                                 .range(Range.all())
                                 .build();
    }

    public static DateRange of(final Range<LocalDate> range) {
        return ImmutableDateRange.builder()
                                 .range(range)
                                 .build();
    }

    public static DateRange between(final LocalDate start,
                                    final LocalDate end) {
        return ImmutableDateRange.builder()
                                 // For discrete range types (e.g. daterange), postgresql will always transform them into the closed-open form
                                 // See section 8.17.7:
                                 // > The built-in range types int4range, int8range, and daterange all use a canonical form that includes the
                                 // > lower bound and excludes the upper bound; that is, [).
                                 // To guarantee equality both before & after persisting, we must manually do the same
                                 .range(Range.closedOpen(start, end.plusDays(1)))
                                 .build();
    }
}
