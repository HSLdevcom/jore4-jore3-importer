package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPatternStop;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPatternStop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Transforms the information of a journey pattern stop into a format
 * that can be inserted into to the Jore 4 database.
 */
@Component
public class JourneyPatternStopExportProcessor implements ItemProcessor<ImporterJourneyPatternStop, Jore4JourneyPatternStop> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JourneyPatternStopExportProcessor.class);

    @Override
    public Jore4JourneyPatternStop process(final ImporterJourneyPatternStop input) throws Exception {
        LOGGER.debug("Processing journey pattern stop: {}", input);

        return Jore4JourneyPatternStop.of(
                input.isHastusPoint(),
                input.isViaPoint(),
                input.viaPointNames(),
                input.journeyPatternTransmodelId(),
                input.scheduledStopPointTransmodelLabel(),
                input.orderNumber()
        );
    }
}
