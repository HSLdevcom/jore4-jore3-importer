package fi.hsl.jore.importer.config.jooq.converter.date_range;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.quicktheories.QuickTheory.qt;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.quicktheories.core.Gen;
import org.quicktheories.generators.Generate;

public class DateRangeConverterGenerativeTest {

    private static final DateRangeConverter CONVERTER = DateRangeConverter.INSTANCE;

    public static final class Generators {

        private Generators() {}

        public static Gen<LocalDate> timestamp(final LocalDate end, final Duration range) {
            final long now = end.toEpochDay();
            final long diff = range.toDaysPart();
            return Generate.longRange(now - diff, now).map(LocalDate::ofEpochDay);
        }

        public static Gen<Optional<LocalDate>> maybeTimestamp(final LocalDate end, final Duration range) {
            return Generate.oneOf(
                    Generate.constant(Optional.empty()), timestamp(end, range).map(Optional::of));
        }

        public static Gen<DateRange> dateRange() {
            final LocalDate start = LocalDate.now().minus(365, ChronoUnit.DAYS);
            final LocalDate end = start.plus(200, ChronoUnit.DAYS);
            final Duration range = Duration.ofDays(100);
            return maybeTimestamp(start, range)
                    .zip(
                            maybeTimestamp(end, range),
                            Generate.booleans(),
                            Generate.booleans(),
                            DateRangeUtil::asDateRange);
        }

        public static Gen<DateRange> emptyDateRange() {
            return Generate.constant(DateRange.empty());
        }

        public static Gen<DateRange> unboundedDateRange() {
            return Generate.constant(DateRange.unbounded());
        }

        public static Gen<DateRange> randomDateRange() {
            return Generate.oneOf(emptyDateRange(), unboundedDateRange(), dateRange());
        }
    }

    @Test
    public void testDateRangeRoundTrip() {
        qt().withExamples(1000).forAll(Generators.randomDateRange()).checkAssert(dateRange -> {
            final Object converted = CONVERTER.to(dateRange);
            final DateRange roundTrip = CONVERTER.from(converted);

            assertThat(String.format("'%s' should match '%s'", dateRange, roundTrip), dateRange, is(roundTrip));
        });
    }
}
