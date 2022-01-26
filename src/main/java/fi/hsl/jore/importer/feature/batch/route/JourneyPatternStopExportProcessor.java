package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.network.route.dto.ExportableJourneyPatternStop;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPatternStop;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Transforms the information of a journey pattern stop into a format
 * that can be inserted into to the Jore 4 database.
 */
@Component
public class JourneyPatternStopExportProcessor implements ItemProcessor<ExportableJourneyPatternStop, TransmodelJourneyPatternStop> {

    @Override
    public TransmodelJourneyPatternStop process(final ExportableJourneyPatternStop input) throws Exception {
        return TransmodelJourneyPatternStop.of(
                input.isHastusPoint(),
                input.isViaPoint(),
                input.journeyPatternTransmodelId(),
                input.scheduledStopPointTransmodelId(),
                input.orderNumber()
        );
    }
}
