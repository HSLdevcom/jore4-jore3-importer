package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.network.route.dto.ExportableJourneyPattern;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPattern;
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
public class JourneyPatternExportProcessor implements ItemProcessor<ExportableJourneyPattern, TransmodelJourneyPattern> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JourneyPatternExportProcessor.class);

    @Override
    public TransmodelJourneyPattern process(final ExportableJourneyPattern input) throws Exception {
        LOGGER.debug("Processing journey pattern: {}", input);

        return TransmodelJourneyPattern.of(
                UUID.randomUUID(),
                input.routeDirectionId(),
                input.routeTransmodelId()
        );
    }
}
