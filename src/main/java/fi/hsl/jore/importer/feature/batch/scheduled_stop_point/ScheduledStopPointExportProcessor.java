package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImporterScheduledStopPoint;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Combines the stop information imported from Jore 3 and Digiroad, and creates
 * a new {@link Jore4ScheduledStopPoint} object which can be inserted into
 * the Jore 4 database.
 */
@Component
public class ScheduledStopPointExportProcessor implements ItemProcessor<ImporterScheduledStopPoint, Jore4ScheduledStopPoint> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledStopPointExportProcessor.class);

    private static final int DEFAULT_PRIORITY = 10;

    //The validity period is hard coded here because it was requested by
    //the customer. The idea was that these weird dates would make it easier to
    //find scheduled stop points which are imported from Jore 3 and are valid until
    //further notice.
    private static final LocalDate DEFAULT_VALIDITY_START =
            LocalDate.of(1990, 1, 1);
    private static final LocalDate DEFAULT_VALIDITY_END =
            LocalDate.of(2051, 1, 1);

    private final DigiroadStopService digiroadStopService;

    @Autowired
    public ScheduledStopPointExportProcessor(final DigiroadStopService digiroadStopService) {
        this.digiroadStopService = digiroadStopService;
    }

    @Override
    public Jore4ScheduledStopPoint process(final ImporterScheduledStopPoint jore3Stop) throws Exception {
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
            final Integer externalIdForExport = tryParseExternalId(externalId);
            if (externalIdForExport == null) {
                // Jore3 stop ids are always supposed to be numbers, even if they were stored as strings.
                // The field is required, so abort processing of whole stop point if we can't handle it.
                LOGGER.warn(
                    "Skipping importing scheduled stop point {} from Jore3: could not parse external_id as integer: {}.",
                    jore3Stop.shortId().get(),
                    externalId.value()
                );
                continue;
            }

            final Optional<DigiroadStop> digiroadStopContainer = digiroadStopService.findByNationalId(elyNumber);
            if (digiroadStopContainer.isPresent()) {
                final DigiroadStop digiroadStop = digiroadStopContainer.get();
                LOGGER.debug("Found Digiroad stop: {}", digiroadStop);

                final Jore4ScheduledStopPoint jore4Stop = Jore4ScheduledStopPoint.of(
                        UUID.randomUUID(),
                        externalId.value(),
                        externalIdForExport,
                        digiroadStop.digiroadLinkId(),
                        Jore4ScheduledStopPointDirection.valueOf(digiroadStop.directionOnInfraLink().name()),
                        jore3Stop.shortId().get(),
                        jore3Stop.location(),
                        jore3Stop.hastusPlaceId(),
                        DEFAULT_PRIORITY,
                        Optional.of(DEFAULT_VALIDITY_START),
                        Optional.of(DEFAULT_VALIDITY_END)
                );

                LOGGER.debug("Created scheduled stop point: {}", jore4Stop);
                return jore4Stop;
            }
        }

        LOGGER.error("Jore 3 stop with short id: {} isn't processed any further no digiroad stop was found with national ids: {}",
                jore3Stop.shortId().get(),
                elyNumbers
        );
        return null;
    }

    private Integer tryParseExternalId(ExternalId externalId) {
        try {
            return Integer.parseInt(externalId.value());
        } catch (final NumberFormatException ignored) {
            return null;
        }
    }
}
