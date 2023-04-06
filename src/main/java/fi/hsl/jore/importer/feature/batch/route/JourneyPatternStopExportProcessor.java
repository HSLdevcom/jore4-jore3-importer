package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPatternStop;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPatternStop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Transforms the information of a journey pattern stop point into a format that
 * can be inserted to the Jore 4 database.
 */
@Component
public class JourneyPatternStopExportProcessor implements ItemProcessor<ImporterJourneyPatternStop, Jore4JourneyPatternStop> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JourneyPatternStopExportProcessor.class);

    @Override
    public Jore4JourneyPatternStop process(final ImporterJourneyPatternStop input) throws Exception {
        LOGGER.debug("Processing journey pattern stop: {}", input);

        final boolean isUsedAsTimingPoint = isUsedAsTimingPoint(input);

        return Jore4JourneyPatternStop.of(input.journeyPatternJore4Id(),
                                          input.orderNumber(),
                                          input.scheduledStopPointJore4Label(),
                                          isUsedAsTimingPoint,
                                          input.isViaPoint(),
                                          input.viaPointNames()
        );
    }

    private static boolean isUsedAsTimingPoint(final ImporterJourneyPatternStop stop) {
        // There is a constraint in the Jore4 database that requires that when a stop point is
        // marked as a timing point, it must also have a Hastus place ID. If this constraint is not
        // met, the stop point (as part of journey pattern) is rejected.

        if (stop.isUsedAsTimingPoint()) {
            if (stop.timingPlaceId().isPresent()) {
                return true;
            } else {
                LOGGER.warn(
                        "Jore3 route direction {}: route point {}: stop point marked as a timing point but no Hastus "
                                + "place ID present => setting isUsedAsTimingPoint flag to false",
                        stop.routeDirectionJore3Id(),
                        stop.orderNumber());
            }
        }

        return false;
    }
}
