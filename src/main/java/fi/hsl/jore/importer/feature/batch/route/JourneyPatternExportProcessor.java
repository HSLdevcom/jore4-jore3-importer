package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPattern;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Transforms the information of a journey pattern into a format
 * which can be inserted into the Jore 4 database.
 */
@Component
public class JourneyPatternExportProcessor implements ItemProcessor<ImporterJourneyPattern, Jore4JourneyPattern> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JourneyPatternExportProcessor.class);

    @Override
    public Jore4JourneyPattern process(final ImporterJourneyPattern input) throws Exception {
        LOGGER.debug("Processing journey pattern: {}", input);

        return Jore4JourneyPattern.of(
                UUID.randomUUID(),
                input.routeDirectionId(),
                input.routeJore4Id()
        );
    }
}
