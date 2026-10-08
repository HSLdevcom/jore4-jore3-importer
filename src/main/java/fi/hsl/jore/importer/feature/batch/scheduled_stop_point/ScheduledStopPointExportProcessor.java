package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ClosestInfrastructureLink;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPointDirection;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4InfrastructureNetworkRepository;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImporterScheduledStopPoint;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Combines the stop information imported from Jore 3 and Digiroad, and creates a new {@link Jore4ScheduledStopPoint}
 * object which can be inserted into the Jore 4 database.
 *
 * <p>For each ELY number of the stop, the distinct vehicle modes of the matching Digiroad stop are tried in order and
 * the first one that resolves to an infrastructure link and a direction is used:
 *
 * <ul>
 *   <li>Bus: the Digiroad link is used if it is a <code>generic_bus</code> link, with the Digiroad direction.
 *   <li>Tram: the closest <code>generic_tram</code> link to the Jore 3 location is used. The direction is resolved with
 *       PostGIS, or taken from the link itself if it is one-way.
 * </ul>
 */
@Component
public class ScheduledStopPointExportProcessor
        implements ItemProcessor<ImporterScheduledStopPoint, Jore4ScheduledStopPoint> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledStopPointExportProcessor.class);

    private static final int DEFAULT_PRIORITY = 10;

    // The validity period is hard coded here because it was requested by
    // the customer. The idea was that these weird dates would make it easier to
    // find scheduled stop points which are imported from Jore 3 and are valid until
    // further notice.
    private static final LocalDate DEFAULT_VALIDITY_START = LocalDate.of(1990, 1, 1);
    private static final LocalDate DEFAULT_VALIDITY_END = LocalDate.of(2051, 1, 1);

    private final DigiroadStopService digiroadStopService;
    private final IJore4InfrastructureNetworkRepository infrastructureNetworkRepository;

    @Autowired
    public ScheduledStopPointExportProcessor(
            final DigiroadStopService digiroadStopService,
            final IJore4InfrastructureNetworkRepository infrastructureNetworkRepository) {
        this.digiroadStopService = digiroadStopService;
        this.infrastructureNetworkRepository = infrastructureNetworkRepository;
    }

    @Override
    public Jore4ScheduledStopPoint process(final ImporterScheduledStopPoint importerStop) throws Exception {
        LOGGER.info("Processing stop point in Importer's database: {}", importerStop);

        final List<ExternalId> externalIds = importerStop.externalIds();
        final List<Long> elyNumbers = importerStop.elyNumbers();

        if (externalIds.size() != elyNumbers.size()) {
            LOGGER.error(
                    "Error processing the the stop with short ID: {}. The amount of external IDs {} differs from the "
                            + "amount of ELY numbers {} which blocks processing any further.",
                    importerStop.shortId(),
                    externalIds.size(),
                    elyNumbers.size());
            return null;
        }

        for (int index = 0; index < elyNumbers.size(); index++) {
            final ExternalId externalId = externalIds.get(index);
            final Long elyNumber = elyNumbers.get(index);

            final Optional<DigiroadStop> digiroadStopContainer = digiroadStopService.findByNationalId(elyNumber);
            if (digiroadStopContainer.isEmpty()) {
                continue;
            }

            final DigiroadStop digiroadStop = digiroadStopContainer.get();
            LOGGER.info("Found Digiroad stop: {}", digiroadStop);

            if (externalId.value().isBlank()) {
                LOGGER.info(
                        "Stop point with short ID: {}, elyNumber {}, Digiroad stop {} doesn't have an external id",
                        importerStop.shortId().get(),
                        elyNumber,
                        digiroadStop.digiroadStopId());
                continue;
            }

            final List<VehicleMode> vehicleModes =
                    digiroadStop.vehicleModes().stream().distinct().toList();
            if (vehicleModes.size() > 1) {
                LOGGER.info(
                        "Stop point with short ID: {}, elyNumber {}, Digiroad stop {} has multiple vehicle modes: {}",
                        importerStop.shortId().get(),
                        elyNumber,
                        digiroadStop.digiroadStopId(),
                        vehicleModes);
            }

            for (final VehicleMode vehicleMode : vehicleModes) {
                final Optional<Jore4ScheduledStopPoint> jore4Stop =
                        resolveForMode(importerStop, externalId, elyNumber, digiroadStop, vehicleMode);
                if (jore4Stop.isPresent()) {
                    LOGGER.info("Created scheduled stop point: {}", jore4Stop.get());
                    return jore4Stop.get();
                }
            }

            LOGGER.error(
                    "Stop point with short ID: {}, elyNumber {}, Digiroad stop {} couldn't be resolved with any of "
                            + "the vehicle modes: {}",
                    importerStop.shortId().get(),
                    elyNumber,
                    digiroadStop.digiroadStopId(),
                    vehicleModes);
        }

        LOGGER.error(
                "Stop point with short ID: {} isn't processed any further, no digiroad stop was found or resolved "
                        + "with national ids: {}",
                importerStop.shortId().get(),
                elyNumbers);
        return null;
    }

    private Optional<Jore4ScheduledStopPoint> resolveForMode(
            final ImporterScheduledStopPoint importerStop,
            final ExternalId externalId,
            final Long elyNumber,
            final DigiroadStop digiroadStop,
            final VehicleMode vehicleMode) {
        final Optional<String> vehicleSubmode = vehicleMode.vehicleSubmode();
        if (vehicleSubmode.isEmpty()
                || vehicleSubmode.get().isBlank()
                || !List.of("generic_bus", "generic_tram").contains(vehicleSubmode.get())) {
            LOGGER.error(
                    "Stop point with short ID: {}, elyNumber {}, Digiroad stop {}: vehicle mode {} is not supported",
                    importerStop.shortId().get(),
                    elyNumber,
                    digiroadStop.digiroadStopId(),
                    vehicleMode);
            return Optional.empty();
        }

        return switch (vehicleMode) {
            case BUS -> resolveBus(importerStop, externalId, elyNumber, digiroadStop, vehicleSubmode.get());
            case TRAM -> resolveTram(importerStop, externalId, elyNumber, digiroadStop, vehicleSubmode.get());
            default -> Optional.empty();
        };
    }

    private Optional<Jore4ScheduledStopPoint> resolveBus(
            final ImporterScheduledStopPoint importerStop,
            final ExternalId externalId,
            final Long elyNumber,
            final DigiroadStop digiroadStop,
            final String vehicleSubmode) {
        if (digiroadStop.digiroadLinkId().isBlank()) {
            LOGGER.error(
                    "Stop point with short ID: {}, elyNumber {}, Digiroad stop {} doesn't have a digiroad link id, "
                            + "vehicle mode {}",
                    importerStop.shortId().get(),
                    elyNumber,
                    digiroadStop.digiroadStopId(),
                    VehicleMode.BUS);
            return Optional.empty();
        }

        final Optional<UUID> linkId =
                infrastructureNetworkRepository.findLinkIdByExternalId(digiroadStop.digiroadLinkId(), vehicleSubmode);
        if (linkId.isEmpty()) {
            LOGGER.error(
                    "Stop point with short ID: {}, elyNumber {}, Digiroad stop {}: no {} infrastructure link found "
                            + "with external id {}, vehicle mode {}",
                    importerStop.shortId().get(),
                    elyNumber,
                    digiroadStop.digiroadStopId(),
                    vehicleSubmode,
                    digiroadStop.digiroadLinkId(),
                    VehicleMode.BUS);
            return Optional.empty();
        }

        return Optional.of(createStop(
                importerStop,
                externalId,
                linkId.get(),
                digiroadStop.digiroadLinkId(),
                Jore4ScheduledStopPointDirection.valueOf(
                        digiroadStop.directionOnInfraLink().name()),
                VehicleMode.BUS));
    }

    private Optional<Jore4ScheduledStopPoint> resolveTram(
            final ImporterScheduledStopPoint importerStop,
            final ExternalId externalId,
            final Long elyNumber,
            final DigiroadStop digiroadStop,
            final String vehicleSubmode) {
        final Optional<Jore4ClosestInfrastructureLink> closestLink =
                infrastructureNetworkRepository.findClosestLink(importerStop.location(), vehicleSubmode);
        if (closestLink.isEmpty()) {
            LOGGER.error(
                    "Stop point with short ID: {}, elyNumber {}, Digiroad stop {}: no {} infrastructure link found "
                            + "within {} m, vehicle mode {}",
                    importerStop.shortId().get(),
                    elyNumber,
                    digiroadStop.digiroadStopId(),
                    vehicleSubmode,
                    IJore4InfrastructureNetworkRepository.SEARCH_RADIUS_METERS,
                    VehicleMode.TRAM);
            return Optional.empty();
        }

        final Jore4ClosestInfrastructureLink link = closestLink.get();
        LOGGER.debug(
                "Stop point with short ID: {}, elyNumber {}, Digiroad stop {}: closest {} link {} (external id {}) "
                        + "is {} m away",
                importerStop.shortId().get(),
                elyNumber,
                digiroadStop.digiroadStopId(),
                vehicleSubmode,
                link.infrastructureLinkId(),
                link.externalLinkId(),
                link.distanceInMeters());

        Optional<Jore4ScheduledStopPointDirection> direction = infrastructureNetworkRepository.findPointDirectionOnLink(
                importerStop.location(), link.infrastructureLinkId());
        if (direction.isEmpty()) {
            direction = link.oneWayDirection();
            direction.ifPresent(oneWayDirection -> LOGGER.info(
                    "Stop point with short ID: {}, elyNumber {}, Digiroad stop {}: direction on tram link {} "
                            + "couldn't be resolved from the location, using the one-way direction of the link: {}",
                    importerStop.shortId().get(),
                    elyNumber,
                    digiroadStop.digiroadStopId(),
                    link.infrastructureLinkId(),
                    oneWayDirection));
        }

        if (direction.isEmpty()) {
            LOGGER.error(
                    "Stop point with short ID: {}, elyNumber {}, Digiroad stop {}: direction on bidirectional tram "
                            + "link {} (external id {}, {} m away) couldn't be resolved, vehicle mode {}",
                    importerStop.shortId().get(),
                    elyNumber,
                    digiroadStop.digiroadStopId(),
                    link.infrastructureLinkId(),
                    link.externalLinkId(),
                    link.distanceInMeters(),
                    VehicleMode.TRAM);
            return Optional.empty();
        }

        return Optional.of(createStop(
                importerStop,
                externalId,
                link.infrastructureLinkId(),
                link.externalLinkId(),
                direction.get(),
                VehicleMode.TRAM));
    }

    private static Jore4ScheduledStopPoint createStop(
            final ImporterScheduledStopPoint importerStop,
            final ExternalId externalId,
            final UUID infrastructureLinkId,
            final String externalInfrastructureLinkId,
            final Jore4ScheduledStopPointDirection direction,
            final VehicleMode vehicleMode) {
        return Jore4ScheduledStopPoint.of(
                UUID.randomUUID(),
                externalId.value(),
                infrastructureLinkId,
                externalInfrastructureLinkId,
                direction,
                vehicleMode,
                importerStop.shortId().get(),
                importerStop.location(),
                importerStop.placeExternalId(),
                DEFAULT_PRIORITY,
                Optional.of(DEFAULT_VALIDITY_START),
                Optional.of(DEFAULT_VALIDITY_END));
    }
}
