package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStopDirection;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ExportableScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Combines the stop information imported from Jore 3 and Digiroad, and creates
 * a new {@link TransmodelScheduledStopPoint} object which can be inserted into
 * the Jore 4 database.
 */
public class ScheduledStopPointExportProcessor implements ItemProcessor<ExportableScheduledStopPoint, TransmodelScheduledStopPoint> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledStopPointExportProcessor.class);

    private static final String LANGUAGE_CODE_FINNISH = "fi_FI";

    private final DigiroadStopService digiroadStopService;

    @Autowired
    public ScheduledStopPointExportProcessor(DigiroadStopService digiroadStopService) {
        this.digiroadStopService = digiroadStopService;
    }

    @Override
    public TransmodelScheduledStopPoint process(ExportableScheduledStopPoint jore3Stop) throws Exception {
        LOGGER.debug("Processing Jore 3 stop: {}", jore3Stop);

        //At the moment we export stops which don't have an ely number.
        //Thus, we must return null if the stop has no ely number
        //because this ensures that the stop isn't processed any further.
        if (jore3Stop.elyNumber().isEmpty()) {
            LOGGER.error("Jore 3 stop with id: {} isn't processed any further because it has no ely number",
                    jore3Stop.externalId().value()
            );
            return null;
        }

        String elyNumber = jore3Stop.elyNumber().get();
        Optional<DigiroadStop> digiroadStopContainer = digiroadStopService.findByElyNumber(elyNumber);

        //If we cannot find the corresponding Digiroad stop, we cannot
        //determine the coordinates of the stop. Thus, we must return null
        //because this ensures that the stop isn't processed any further.
        if (digiroadStopContainer.isEmpty()) {
            LOGGER.error("Jore 3 stop with id: {} isn't processed any further no digiroad stop was found with ely number: {}",
                    jore3Stop.externalId().value(),
                    elyNumber
            );
            return null;
        }

        DigiroadStop digiroadStop = digiroadStopContainer.get();
        LOGGER.debug("Found Digiroad stop: {}", digiroadStop);

        TransmodelScheduledStopPoint transmodelStop = TransmodelScheduledStopPoint.of(
                jore3Stop.externalId().value(),
                digiroadStop.externalLinkId(),
                isDirectionForwardOnInfraLink(digiroadStop.directionOnInfraLink()),
                constructLabel(jore3Stop.name()),
                jore3Stop.location()
        );

        LOGGER.debug("Created scheduled stop point: {}", transmodelStop);
        return transmodelStop;
    }

    private boolean isDirectionForwardOnInfraLink(DigiroadStopDirection stopDirection) {
        return stopDirection == DigiroadStopDirection.FORWARD;
    }

    private String constructLabel(MultilingualString name) {
        return name.values().get(LANGUAGE_CODE_FINNISH).get();
    }
}
