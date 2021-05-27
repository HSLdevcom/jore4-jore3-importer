package fi.hsl.jore.importer.config.jooq.converter.time_range;

import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class TimeRangeConverterTest {

    private static final TimeRangeConverter CONVERTER = TimeRangeConverter.INSTANCE;

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
        final TimeRange range = CONVERTER.from("(,)");
        assertThat(range,
                   is(TimeRange.unbounded()));
        final TimeRange range2 = CONVERTER.from("[,]");
        assertThat(range2,
                   is(TimeRange.unbounded()));
    }

    @Test
    public void givenBounded_thenReturnBounded() {
        assertThat(CONVERTER.from("[2004-10-19 10:23:54+02,2005-10-19 10:23:54+02]"),
                   is(TimeRange.of(Range.closed(Instant.parse("2004-10-19T08:23:54.00Z"),
                                                Instant.parse("2005-10-19T08:23:54.00Z")))));
        assertThat(CONVERTER.from("(2004-10-19 10:23:54+02,2005-10-19 10:23:54+02)"),
                   is(TimeRange.of(Range.open(Instant.parse("2004-10-19T08:23:54.00Z"),
                                              Instant.parse("2005-10-19T08:23:54.00Z")))));
    }

    @Test
    public void givenOnlyTo_thenReturnBounded() {
        assertThat(CONVERTER.from("[,2005-10-19 10:23:54+02]"),
                   is(TimeRange.of(Range.atMost(Instant.parse("2005-10-19T08:23:54.00Z")))));
        assertThat(CONVERTER.from("(,2005-10-19 10:23:54+02)"),
                   is(TimeRange.of(Range.lessThan(Instant.parse("2005-10-19T08:23:54.00Z")))));
        assertThat(CONVERTER.from("[,2005-10-19 10:23:54+02)"),
                   is(TimeRange.of(Range.lessThan(Instant.parse("2005-10-19T08:23:54.00Z")))));
        assertThat(CONVERTER.from("(,2005-10-19 10:23:54+02]"),
                   is(TimeRange.of(Range.atMost(Instant.parse("2005-10-19T08:23:54.00Z")))));
    }

    @Test
    public void givenOnlyFrom_thenReturnBounded() {
        assertThat(CONVERTER.from("[2004-10-19 10:23:54+02,]"),
                   is(TimeRange.of(Range.atLeast(Instant.parse("2004-10-19T08:23:54.00Z")))));
        assertThat(CONVERTER.from("(2004-10-19 10:23:54+02,)"),
                   is(TimeRange.of(Range.greaterThan(Instant.parse("2004-10-19T08:23:54.00Z")))));
        assertThat(CONVERTER.from("[2004-10-19 10:23:54+02,)"),
                   is(TimeRange.of(Range.atLeast(Instant.parse("2004-10-19T08:23:54.00Z")))));
        assertThat(CONVERTER.from("(2004-10-19 10:23:54+02,]"),
                   is(TimeRange.of(Range.greaterThan(Instant.parse("2004-10-19T08:23:54.00Z")))));

        assertThat(CONVERTER.from("[\"2021-01-28 13:40:04.392588+02\",)"),
                   is(TimeRange.of(Range.atLeast(Instant.parse("2021-01-28T11:40:04.392588Z")))));
    }
}
