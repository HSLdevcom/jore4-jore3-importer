package fi.hsl.jore.importer.config.jooq.converter.time_range;

import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimeRangeConverterTest {

    private static final TimeRangeConverter CONVERTER = TimeRangeConverter.INSTANCE;

    // timestamp with timezone from postgresql as string
    private static final String PG_STRING_START = "2004-10-19 10:23:54+02";

    private static final Instant TIMESTAMP_START = Instant.parse("2004-10-19T08:23:54.00Z");

    // timestamp with timezone from postgresql as string
    private static final String PG_STRING_END = "2005-10-19 10:23:54+02";
    // .. and the corresponding java instant
    private static final Instant TIMESTAMP_END = Instant.parse("2005-10-19T08:23:54.00Z");

    @Test
    public void givenNull_thenReturnNull() {
        assertThat(CONVERTER.from(null),
                   is(nullValue()));
    }

    @Test
    public void givenEmpty_thenReturnEmpty() {
        final TimeRange range = CONVERTER.from("empty");
        assertThat(range,
                   is(TimeRange.empty()));
        assertThat(range.range().isEmpty(),
                   is(true));
    }

    @Test
    public void givenUnbounded_thenReturnUnbounded() {
        assertThat(CONVERTER.from("(,)"),
                   is(TimeRange.unbounded()));
        assertThat(CONVERTER.from("[,]"),
                   is(TimeRange.unbounded()));
    }

    @Test
    public void givenBounded_thenReturnBounded() {
        assertThat(CONVERTER.from(String.format("[%s,%s]",
                                                PG_STRING_START,
                                                PG_STRING_END)),
                   is(TimeRange.of(Range.closed(TIMESTAMP_START,
                                                TIMESTAMP_END))));
        assertThat(CONVERTER.from(String.format("(%s,%s)",
                                                PG_STRING_START,
                                                PG_STRING_END)),
                   is(TimeRange.of(Range.open(TIMESTAMP_START,
                                              TIMESTAMP_END))));
    }

    @Test
    public void givenOnlyTo_thenReturnBounded() {
        assertThat(CONVERTER.from(String.format("[,%s]",
                                                PG_STRING_END)),
                   is(TimeRange.of(Range.atMost(TIMESTAMP_END))));
        assertThat(CONVERTER.from(String.format("(,%s)",
                                                PG_STRING_END)),
                   is(TimeRange.of(Range.lessThan(TIMESTAMP_END))));
        assertThat(CONVERTER.from(String.format("[,%s)",
                                                PG_STRING_END)),
                   is(TimeRange.of(Range.lessThan(TIMESTAMP_END))));
        assertThat(CONVERTER.from(String.format("(,%s]",
                                                PG_STRING_END)),
                   is(TimeRange.of(Range.atMost(TIMESTAMP_END))));
    }

    @Test
    public void givenOnlyFrom_thenReturnBounded() {
        assertThat(CONVERTER.from(String.format("[%s,]",
                                                PG_STRING_START)),
                   is(TimeRange.of(Range.atLeast(TIMESTAMP_START))));
        assertThat(CONVERTER.from(String.format("(%s,)",
                                                PG_STRING_START)),
                   is(TimeRange.of(Range.greaterThan(TIMESTAMP_START))));
        assertThat(CONVERTER.from(String.format("[%s,)",
                                                PG_STRING_START)),
                   is(TimeRange.of(Range.atLeast(TIMESTAMP_START))));
        assertThat(CONVERTER.from(String.format("(%s,]",
                                                PG_STRING_START)),
                   is(TimeRange.of(Range.greaterThan(TIMESTAMP_START))));
    }

    @Test
    public void givenStringWithQuotes_thenReturnParsed() {
        assertThat("start and end quotes are ignored",
                   CONVERTER.from(String.format("\"[%s,)\"",
                                                PG_STRING_START)),
                   is(TimeRange.of(Range.atLeast(TIMESTAMP_START))));
        assertThat("unbalanced starting quote is ignored",
                   CONVERTER.from(String.format("\"[%s,)",
                                                PG_STRING_START)),
                   is(TimeRange.of(Range.atLeast(TIMESTAMP_START))));
        assertThat("unbalanced ending quote is ignored",
                   CONVERTER.from(String.format("[%s,)\"",
                                                PG_STRING_START)),
                   is(TimeRange.of(Range.atLeast(TIMESTAMP_START))));
    }

    @Test
    public void givenMissingTimezone_thenThrows() {
        assertThrows(DateTimeParseException.class,
                     () -> CONVERTER.from(String.format("[%s,)",
                                                        "2004-10-19 10:23:54")));
    }

    @Test
    public void givenMissingTime_thenThrows() {
        assertThrows(DateTimeParseException.class,
                     () -> CONVERTER.from(String.format("[%s,)",
                                                        "2004-10-19")));
    }

    @Test
    public void givenMissingDate_thenThrows() {
        assertThrows(DateTimeParseException.class,
                     () -> CONVERTER.from(String.format("[%s,)",
                                                        "10:23:54+02")));
    }
}
