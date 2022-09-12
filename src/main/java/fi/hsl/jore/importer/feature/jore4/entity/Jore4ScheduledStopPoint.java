package fi.hsl.jore.importer.feature.jore4.entity;

import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;
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

    String externalScheduledStopPointId();

    Jore4ScheduledStopPointDirection directionOnInfraLink();

    String label();

    Point measuredLocation();

    int priority();

    Optional<OffsetDateTime> validityStart();

    Optional<OffsetDateTime> validityEnd();

    static ImmutableJore4ScheduledStopPoint of(final UUID scheduledStopPointId,
                                               final String externalScheduledStopPointId,
                                               final String externalInfrastructureLinkId,
                                               final Jore4ScheduledStopPointDirection directionOnInfraLink,
                                               final String label,
                                               final Point measuredLocation,
                                               final int priority,
                                               final Optional<OffsetDateTime> validityStart,
                                               final Optional<OffsetDateTime> validityEnd) {
        return ImmutableJore4ScheduledStopPoint.builder()
                .scheduledStopPointId(scheduledStopPointId)
                .externalScheduledStopPointId(externalScheduledStopPointId)
                .externalInfrastructureLinkId(externalInfrastructureLinkId)
                .directionOnInfraLink(directionOnInfraLink)
                .label(label)
                .measuredLocation(measuredLocation)
                .priority(priority)
                .validityStart(validityStart)
                .validityEnd(validityEnd)
                .build();
    }
}
