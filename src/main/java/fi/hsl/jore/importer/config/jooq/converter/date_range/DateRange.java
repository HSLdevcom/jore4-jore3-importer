package fi.hsl.jore.importer.config.jooq.converter.date_range;

import com.google.common.collect.Range;
import java.time.LocalDate;

public record DateRange(Range<LocalDate> range) {
    @Override
    public String toString() {
        return range().toString();
    }

    public static DateRange empty() {
        return new DateRange(Range.closedOpen(LocalDate.EPOCH, LocalDate.EPOCH));
        /*
        return ImmutableDateRange.builder()
                                 .range(Range.closedOpen(LocalDate.EPOCH, LocalDate.EPOCH))
                                 .build();
                                 */
    }

    public static DateRange unbounded() {
        return new DateRange(Range.all());
        /*
        return ImmutableDateRange.builder()
                                 .range(Range.all())
                                 .build();

         */
    }

    public static DateRange of(final Range<LocalDate> range) {
        return new DateRange(range);
        /*
        return ImmutableDateRange.builder()
                                 .range(range)
                                 .build();

         */
    }

    public static DateRange between(final LocalDate start, final LocalDate end) {
        return new DateRange(Range.closedOpen(start, end.plusDays(1)));
        /*
        return ImmutableDateRange.builder()
                                 // For discrete range types (e.g. daterange), postgresql will always transform them into the closed-open form
                                 // See section 8.17.7:
                                 // > The built-in range types int4range, int8range, and daterange all use a canonical form that includes the
                                 // > lower bound and excludes the upper bound; that is, [).
                                 // To guarantee equality both before & after persisting, we must manually do the same
                                 //.range(Range.closedOpen(start, end.plusDays(1)))
                                 .build();

         */
    }
}
