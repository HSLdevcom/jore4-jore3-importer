package fi.hsl.jore.importer.config.jooq.converter.date_range;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import fi.hsl.jore.importer.config.jooq.converter.util.RangeUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.jooq.Converter;

public class DateRangeConverter implements Converter<Object, DateRange> {

    public static final DateRangeConverter INSTANCE = new DateRangeConverter();

    private static final String DATE_OR_EMPTY = "[\"]?[0-9-:\\.+\\s]*[\"]?";
    private static final String START_TOKEN = "[\\[(]";
    private static final String END_TOKEN = "[\\])]";
    private static final Pattern PATTERN =
            Pattern.compile(
                    String.format(
                            "(%s)(%s),(%s)(%s)",
                            START_TOKEN, DATE_OR_EMPTY, DATE_OR_EMPTY, END_TOKEN));

    private static final DateTimeFormatter PG_TIME_FORMATTER = ISO_LOCAL_DATE;

    private static Optional<LocalDate> fromString(@Nullable final String val) {
        return Optional.ofNullable(val)
                .filter(str -> !str.isEmpty())
                .map(str -> PG_TIME_FORMATTER.parse(str, LocalDate::from));
    }

    private static String toString(final LocalDate val) {
        return val.format(PG_TIME_FORMATTER);
    }

    @Override
    @Nullable
    public DateRange from(@Nullable final Object t) {
        if (t == null) {
            return null;
        }
        if ("empty".equals(t)) {
            return DateRangeUtil.EMPTY;
        }
        final Matcher m = PATTERN.matcher(t.toString().replaceAll("\"", ""));
        if (m.find()) {
            final String startToken = m.group(1);
            final Optional<LocalDate> from = fromString(m.group(2));
            final Optional<LocalDate> to = fromString(m.group(3));
            final String endToken = m.group(4);

            final boolean startInclusive = "[".equals(startToken);
            final boolean endInclusive = "]".equals(endToken);

            return DateRangeUtil.asDateRange(from, to, startInclusive, endInclusive);
        } else {
            throw new IllegalArgumentException("Unsupported range : " + t);
        }
    }

    @Override
    @Nullable
    public Object to(@Nullable final DateRange u) {
        if (u == null) {
            return null;
        }
        if (DateRangeUtil.EMPTY.equals(u)) {
            return "empty";
        }
        if (DateRangeUtil.UNBOUNDED.equals(u)) {
            return "(,)";
        }
        return RangeUtil.render(u.range(), DateRangeConverter::toString);
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<DateRange> toType() {
        return DateRange.class;
    }
}
