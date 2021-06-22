package fi.hsl.jore.importer.feature.batch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class PeriodicWriteStatisticsLogger {

    private static final Logger LOG = LoggerFactory.getLogger(PeriodicWriteStatisticsLogger.class);

    private static final Duration INTERVAL = Duration.ofSeconds(10);

    private final String name;

    private int counterNow = Integer.MIN_VALUE;
    private int counterPrevious = Integer.MIN_VALUE;
    private Instant lastWrite = Instant.now();

    public PeriodicWriteStatisticsLogger(final Class<?> repositoryClazz) {
        name = repositoryClazz.getSimpleName()
                              .replace("$$EnhancerBySpringCGLIB", "")
                              .replaceFirst("\\$\\$.*", "");
    }

    public void reset() {
        counterNow = 0;
        counterPrevious = 0;
        lastWrite = Instant.now();
    }

    public void updateCounterAndLog(final int counter) {
        final Instant now = Instant.now();
        counterPrevious = counterNow;
        counterNow = counter;

        if (lastWrite.plus(INTERVAL).isBefore(now)) {
            final int counterDiff = counterNow - counterPrevious;
            final double timeDiffSeconds = (now.toEpochMilli() - lastWrite.toEpochMilli()) / 1000.0;
            final long itemsPerSecond = Math.round(counterDiff / timeDiffSeconds);

            LOG.info("Written {} items in total, {} per second [{}]",
                     counterNow, itemsPerSecond, name);

            lastWrite = now;
        }
    }
}
