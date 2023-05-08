package fi.hsl.jore.importer.feature.jore4.entity;

import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Contains the information of a scheduled stop point which
 * can be written to the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface Jore4ScheduledStopPoint {

    UUID scheduledStopPointId();

    String externalInfrastructureLinkId();

    /**
     * The unprocessed external id from importer database.
     * Required because write Jore4 ids back to importer database and use this for matching.
     */
    String externalScheduledStopPointId();

    /**
     * Tha parsed external id that will be exported to Jore4 database.
     */
    Integer externalIdForExport();

    Jore4ScheduledStopPointDirection directionOnInfraLink();

    String label();

    Point measuredLocation();

    Optional<String> hastusPlaceId();

    int priority();

    Optional<LocalDate> validityStart();

    Optional<LocalDate> validityEnd();

    static ImmutableJore4ScheduledStopPoint of(final UUID scheduledStopPointId,
                                               final String externalScheduledStopPointId,
                                               final Integer externalIdForExport,
                                               final String externalInfrastructureLinkId,
                                               final Jore4ScheduledStopPointDirection directionOnInfraLink,
                                               final String label,
                                               final Point measuredLocation,
                                               final Optional<String> hastusPlaceId,
                                               final int priority,
                                               final Optional<LocalDate> validityStart,
                                               final Optional<LocalDate> validityEnd) {
        return ImmutableJore4ScheduledStopPoint.builder()
                .scheduledStopPointId(scheduledStopPointId)
                .externalScheduledStopPointId(externalScheduledStopPointId)
                .externalIdForExport(externalIdForExport)
                .externalInfrastructureLinkId(externalInfrastructureLinkId)
                .directionOnInfraLink(directionOnInfraLink)
                .label(label)
                .measuredLocation(measuredLocation)
                .hastusPlaceId(hastusPlaceId)
                .priority(priority)
                .validityStart(validityStart)
                .validityEnd(validityEnd)
                .build();
    }
}
