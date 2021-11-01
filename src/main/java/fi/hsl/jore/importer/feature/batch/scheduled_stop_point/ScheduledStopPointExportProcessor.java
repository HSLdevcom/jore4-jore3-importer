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
    public ScheduledStopPointExportProcessor(final DigiroadStopService digiroadStopService) {
        this.digiroadStopService = digiroadStopService;
    }

    @Override
    public TransmodelScheduledStopPoint process(final ExportableScheduledStopPoint jore3Stop) throws Exception {
        LOGGER.debug("Processing Jore 3 stop: {}", jore3Stop);

        if (jore3Stop.elyNumber().isEmpty()) {
            LOGGER.debug("Jore 3 stop with id: {} isn't processed any further because it has no ely number",
                    jore3Stop.externalId().value()
            );
            return null;
        }

        final int nationalId = Integer.parseInt(jore3Stop.elyNumber().get());
        final Optional<DigiroadStop> digiroadStopContainer = digiroadStopService.findByNationalId(nationalId);

        if (digiroadStopContainer.isEmpty()) {
            LOGGER.error("Jore 3 stop with id: {} isn't processed any further no digiroad stop was found with national id: {}",
                    jore3Stop.externalId().value(),
                    nationalId
            );
            return null;
        }

        final DigiroadStop digiroadStop = digiroadStopContainer.get();
        LOGGER.debug("Found Digiroad stop: {}", digiroadStop);

        final TransmodelScheduledStopPoint transmodelStop = TransmodelScheduledStopPoint.of(
                jore3Stop.externalId().value(),
                digiroadStop.digiroadLinkId(),
                isDirectionForwardOnInfraLink(digiroadStop.directionOnInfraLink()),
                constructLabel(jore3Stop.name()),
                jore3Stop.location()
        );

        LOGGER.debug("Created scheduled stop point: {}", transmodelStop);
        return transmodelStop;
    }

    private static boolean isDirectionForwardOnInfraLink(final DigiroadStopDirection stopDirection) {
        return stopDirection == DigiroadStopDirection.FORWARD;
    }

    private static String constructLabel(final MultilingualString name) {
        return name.values().get(LANGUAGE_CODE_FINNISH).get();
    }
}
