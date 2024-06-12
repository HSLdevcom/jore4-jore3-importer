package fi.hsl.jore.importer.config.jooq.converter.time_range;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

import fi.hsl.jore.importer.config.jooq.converter.util.RangeUtil;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.jooq.Converter;

/**
 * A converter class for converting between:
 *
 * <ul>
 *   <li>Postgresql {@code tstzrange} values in the ISO 8601 format (e.g. {@code "[2004-10-19 10:23:54+02,2005-10-19
 *       10:23:54+02)")} and
 *   <li>{@link fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange} instances
 * </ul>
 */
public class TimeRangeConverter implements Converter<Object, TimeRange> {

    public static final TimeRangeConverter INSTANCE = new TimeRangeConverter();

    private static final String DATE_OR_EMPTY = "[\"]?[0-9-:\\.+\\s]*[\"]?";
    private static final String START_TOKEN = "[\\[(]";
    private static final String END_TOKEN = "[\\])]";
    private static final Pattern PATTERN =
            Pattern.compile(String.format("(%s)(%s),(%s)(%s)", START_TOKEN, DATE_OR_EMPTY, DATE_OR_EMPTY, END_TOKEN));

    /**
     * We cant use {@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME} directly, as "ISO
     * 8601 specifies the use of uppercase letter T to separate the date and time. PostgreSQL accepts that format on
     * input, but on output it uses a space rather than T."
     */
    private static final DateTimeFormatter PG_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(ISO_LOCAL_DATE)
            .appendLiteral(' ')
            .append(ISO_LOCAL_TIME)
            .appendOffset("+HHmm", "+00")
            .toFormatter();

    private static Optional<Instant> fromString(@Nullable final String val) {
        return Optional.ofNullable(val)
                .filter(str -> !str.isEmpty())
                .map(str -> PG_TIME_FORMATTER.parse(str, OffsetDateTime::from).toInstant());
    }

    private static String toString(final Instant val) {
        return val.atOffset(ZoneOffset.UTC).format(PG_TIME_FORMATTER);
    }

    @Override
    @Nullable
    public TimeRange from(@Nullable final Object t) {
        if (t == null) {
            return null;
        }
        if ("empty".equals(t)) {
            return TimeRangeUtil.EMPTY;
        }
        final Matcher m = PATTERN.matcher(t.toString().replaceAll("\"", ""));
        if (m.find()) {
            final String startToken = m.group(1);
            final Optional<Instant> from = fromString(m.group(2));
            final Optional<Instant> to = fromString(m.group(3));
            final String endToken = m.group(4);

            final boolean startInclusive = "[".equals(startToken);
            final boolean endInclusive = "]".equals(endToken);

            return TimeRangeUtil.asTimeRange(from, to, startInclusive, endInclusive);
        } else {
            throw new IllegalArgumentException("Unsupported range : " + t);
        }
    }

    @Override
    @Nullable
    public Object to(@Nullable final TimeRange u) {
        if (u == null) {
            return null;
        }
        if (TimeRangeUtil.EMPTY.equals(u)) {
            return "empty";
        }
        if (TimeRangeUtil.UNBOUNDED.equals(u)) {
            return "(,)";
        }

        return RangeUtil.render(u.range(), TimeRangeConverter::toString);
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<TimeRange> toType() {
        return TimeRange.class;
    }
}
