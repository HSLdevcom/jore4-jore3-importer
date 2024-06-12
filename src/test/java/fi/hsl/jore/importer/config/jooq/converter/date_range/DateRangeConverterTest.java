package fi.hsl.jore.importer.config.jooq.converter.date_range;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.google.common.collect.Range;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class DateRangeConverterTest {

    private static final DateRangeConverter CONVERTER = DateRangeConverter.INSTANCE;

    @Test
    public void givenNull_thenReturnNull() {
        assertThat(CONVERTER.from(null), is(nullValue()));
    }

    @Test
    public void givenEmpty_thenReturnEmpty() {
        final DateRange range = CONVERTER.from("empty");
        assertThat(range, is(DateRange.empty()));
        assertThat(range.range().isEmpty(), is(true));
    }

    @Test
    public void givenUnbounded_thenReturnUnbounded() {
        final DateRange range = CONVERTER.from("(,)");
        assertThat(range, is(DateRange.unbounded()));
        final DateRange range2 = CONVERTER.from("[,]");
        assertThat(range2, is(DateRange.unbounded()));
    }

    @Test
    public void givenBounded_thenReturnBounded() {
        assertThat(
                CONVERTER.from("[2004-10-19,2005-10-20]"),
                is(
                        DateRange.of(
                                Range.closed(
                                        LocalDate.parse("2004-10-19"),
                                        LocalDate.parse("2005-10-20")))));
        assertThat(
                CONVERTER.from("(2004-10-19,2005-10-20)"),
                is(
                        DateRange.of(
                                Range.open(
                                        LocalDate.parse("2004-10-19"),
                                        LocalDate.parse("2005-10-20")))));
        assertThat(
                CONVERTER.from("(\"2004-10-19\",\"2005-10-20\")"),
                is(
                        DateRange.of(
                                Range.open(
                                        LocalDate.parse("2004-10-19"),
                                        LocalDate.parse("2005-10-20")))));
    }

    @Test
    public void givenOnlyTo_thenReturnBounded() {
        assertThat(
                CONVERTER.from("[,2005-10-19]"),
                is(DateRange.of(Range.atMost(LocalDate.parse("2005-10-19")))));
        assertThat(
                CONVERTER.from("(,2005-10-19)"),
                is(DateRange.of(Range.lessThan(LocalDate.parse("2005-10-19")))));
        assertThat(
                CONVERTER.from("[,2005-10-19)"),
                is(DateRange.of(Range.lessThan(LocalDate.parse("2005-10-19")))));
        assertThat(
                CONVERTER.from("(,2005-10-19]"),
                is(DateRange.of(Range.atMost(LocalDate.parse("2005-10-19")))));
        assertThat(
                CONVERTER.from("(,\"2005-10-19\"]"),
                is(DateRange.of(Range.atMost(LocalDate.parse("2005-10-19")))));
    }

    @Test
    public void givenOnlyFrom_thenReturnBounded() {
        assertThat(
                CONVERTER.from("[2004-10-19,]"),
                is(DateRange.of(Range.atLeast(LocalDate.parse("2004-10-19")))));
        assertThat(
                CONVERTER.from("(2004-10-19,)"),
                is(DateRange.of(Range.greaterThan(LocalDate.parse("2004-10-19")))));
        assertThat(
                CONVERTER.from("[2004-10-19,)"),
                is(DateRange.of(Range.atLeast(LocalDate.parse("2004-10-19")))));
        assertThat(
                CONVERTER.from("(2004-10-19,]"),
                is(DateRange.of(Range.greaterThan(LocalDate.parse("2004-10-19")))));

        assertThat(
                CONVERTER.from("[\"2021-01-28\",)"),
                is(DateRange.of(Range.atLeast(LocalDate.parse("2021-01-28")))));
    }
}
