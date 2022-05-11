package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ExportableScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.ExportConstants;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPointDirection;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static fi.hsl.jore.importer.feature.transmodel.util.TimestampFactory.offsetDateTimeFromLocalDateTime;

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

        final List<ExternalId> externalIds = jore3Stop.externalIds();
        final List<Long> elyNumbers = jore3Stop.elyNumbers();

        if (externalIds.size() != elyNumbers.size()) {
            LOGGER.debug(
                    "Error processing the Jore 3 stop with short id: {}. The amount of external IDs {} differs from the amount of ELY numbers {} which blocks processing any further.",
                    jore3Stop.shortId(),
                    externalIds.size(),
                    elyNumbers.size()
            );
            return null;
        }


        for (int index = 0; index < elyNumbers.size(); index++) {
            final ExternalId externalId = externalIds.get(index);
            final Long elyNumber = elyNumbers.get(index);

            final Optional<DigiroadStop> digiroadStopContainer = digiroadStopService.findByNationalId(elyNumber);
            if (digiroadStopContainer.isPresent()) {
                final DigiroadStop digiroadStop = digiroadStopContainer.get();
                LOGGER.debug("Found Digiroad stop: {}", digiroadStop);

                final TransmodelScheduledStopPoint transmodelStop = TransmodelScheduledStopPoint.of(
                        UUID.randomUUID(),
                        externalId.value(),
                        digiroadStop.digiroadLinkId(),
                        TransmodelScheduledStopPointDirection.valueOf(digiroadStop.directionOnInfraLink().name()),
                        jore3Stop.shortId().get(),
                        jore3Stop.location(),
                        DEFAULT_PRIORITY,
                        Optional.of(offsetDateTimeFromLocalDateTime(DEFAULT_VALIDITY_START)),
                        Optional.of(offsetDateTimeFromLocalDateTime(DEFAULT_VALIDITY_END))
                );

                LOGGER.debug("Created scheduled stop point: {}", transmodelStop);
                return transmodelStop;
            }
        }

        LOGGER.error("Jore 3 stop with short id: {} isn't processed any further no digiroad stop was found with national ids: {}",
                jore3Stop.shortId().get(),
                elyNumbers
        );
        return null;
    }
}
