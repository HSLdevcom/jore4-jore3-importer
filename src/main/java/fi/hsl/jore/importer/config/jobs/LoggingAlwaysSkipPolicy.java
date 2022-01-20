package fi.hsl.jore.importer.config.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

/**
 * Skips every error and writes a message to the log.
 */
public class LoggingAlwaysSkipPolicy implements SkipPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAlwaysSkipPolicy.class);

    @Override
    public boolean shouldSkip(final Throwable throwable, final int skipCount) throws SkipLimitExceededException {
        LOGGER.error("Could not process the item because an error occurred: {}", throwable.getMessage());
        return true;
    }
}
