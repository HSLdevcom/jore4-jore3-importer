package fi.hsl.jore.importer.config.jooq.converter.date_range;

import fi.hsl.jore.importer.config.jooq.converter.util.RangeUtil;

import java.time.LocalDate;
import java.util.Optional;

public final class DateRangeUtil {

    public static final DateRange EMPTY = DateRange.empty();

    public static final DateRange UNBOUNDED = DateRange.unbounded();

    public static DateRange asDateRange(final Optional<LocalDate> maybeFrom,
                                        final Optional<LocalDate> maybeTo,
                                        final boolean startInclusive,
                                        final boolean endInclusive) {
        return DateRange.of(RangeUtil.asRange(maybeFrom, maybeTo, startInclusive, endInclusive));
    }

    private DateRangeUtil() {
    }
}
