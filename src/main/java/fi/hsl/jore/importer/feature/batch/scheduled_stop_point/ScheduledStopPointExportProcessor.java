package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ExportableScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.ExportConstants;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPointDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Combines the stop information imported from Jore 3 and Digiroad, and creates
 * a new {@link TransmodelScheduledStopPoint} object which can be inserted into
 * the Jore 4 database.
 */
@Component
public class ScheduledStopPointExportProcessor implements ItemProcessor<ExportableScheduledStopPoint, TransmodelScheduledStopPoint> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledStopPointExportProcessor.class);

    private static final int DEFAULT_PRIORITY = 10;

    //The validity period is hard coded here because it was requested by
    //the customer. The idea was that these weird dates would make it easier to
    //find scheduled stop points which are imported from Jore 3 and are valid until
    //further notice.
    private static final LocalDateTime DEFAULT_VALIDITY_START = LocalDateTime.of(
            LocalDate.of(1990, 1, 1),
            ExportConstants.OPERATING_DAY_START_TIME
    );
    private static final LocalDateTime DEFAULT_VALIDITY_END = LocalDateTime.of(
            LocalDate.of(2051, 1, 1),
            ExportConstants.OPERATING_DAY_END_TIME
    );

    private final DigiroadStopService digiroadStopService;

    @Autowired
    public ScheduledStopPointExportProcessor(final DigiroadStopService digiroadStopService) {
        this.digiroadStopService = digiroadStopService;
    }

    @Override
    public TransmodelScheduledStopPoint process(final ExportableScheduledStopPoint jore3Stop) throws Exception {
        LOGGER.debug("Processing Jore 3 stop: {}", jore3Stop);

        final String elyNumber = jore3Stop.elyNumber().filter(str -> !str.isEmpty()).orElse(null);
        if (elyNumber == null) {
            LOGGER.debug("Jore 3 stop with id: {} isn't processed any further because it has no ely number",
                    jore3Stop.externalId().value()
            );
            return null;
        }

        final int nationalId = Integer.parseInt(elyNumber);
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
                UUID.randomUUID().toString(),
                jore3Stop.externalId().value(),
                digiroadStop.digiroadLinkId(),
                TransmodelScheduledStopPointDirection.valueOf(digiroadStop.directionOnInfraLink().name()),
                jore3Stop.shortId().get(),
                jore3Stop.location(),
                DEFAULT_PRIORITY,
                Optional.of(DEFAULT_VALIDITY_START),
                Optional.of(DEFAULT_VALIDITY_END)
        );

        LOGGER.debug("Created scheduled stop point: {}", transmodelStop);
        return transmodelStop;
    }
}
