package fi.hsl.jore.importer.config.jooq.converter.time_range;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.quicktheories.QuickTheory.qt;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.quicktheories.core.Gen;
import org.quicktheories.generators.Generate;

public class TimeRangeConverterGenerativeTest {

    private static final TimeRangeConverter CONVERTER = TimeRangeConverter.INSTANCE;

    public static final class Generators {

        private Generators() {}

        public static Gen<Instant> timestamp(final Instant start, final Duration range) {
            final long now = start.toEpochMilli();
            final long diff = range.toMillis();
            return Generate.longRange(now - diff, now).map(Instant::ofEpochMilli);
        }

        public static Gen<Optional<Instant>> maybeTimestamp(final Instant start, final Duration range) {
            return Generate.oneOf(
                    Generate.constant(Optional.empty()), timestamp(start, range).map(Optional::of));
        }

        public static Gen<TimeRange> instantTimeRange() {
            final Instant start = Instant.now().minus(365, ChronoUnit.DAYS);
            final Instant end = start.plus(200, ChronoUnit.DAYS);
            final Duration range = Duration.ofDays(100);
            return maybeTimestamp(start, range)
                    .zip(
                            maybeTimestamp(end, range),
                            Generate.booleans(),
                            Generate.booleans(),
                            TimeRangeUtil::asTimeRange);
        }

        public static Gen<TimeRange> emptyTimeRange() {
            return Generate.constant(TimeRange.empty());
        }

        public static Gen<TimeRange> unboundedTimeRange() {
            return Generate.constant(TimeRange.unbounded());
        }

        public static Gen<TimeRange> randomTimeRange() {
            return Generate.oneOf(emptyTimeRange(), unboundedTimeRange(), instantTimeRange());
        }
    }

    @Test
    public void testTimeRangeRoundTrip() {
        qt().withExamples(1000).forAll(Generators.randomTimeRange()).checkAssert(timeRange -> {
            final Object converted = CONVERTER.to(timeRange);
            final TimeRange roundTrip = CONVERTER.from(converted);

            assertThat(String.format("'%s' should match '%s'", timeRange, roundTrip), timeRange, is(roundTrip));
        });
    }
}
