package fi.hsl.jore.importer.feature.transmodel.entity;

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
public interface TransmodelScheduledStopPoint {

    UUID scheduledStopPointId();

    String externalInfrastructureLinkId();

    String externalScheduledStopPointId();

    TransmodelScheduledStopPointDirection directionOnInfraLink();

    String label();

    Point measuredLocation();

    int priority();

    Optional<LocalDate> validityStart();

    Optional<LocalDate> validityEnd();

    static ImmutableTransmodelScheduledStopPoint of(final UUID scheduledStopPointId,
                                                    final String externalScheduledStopPointId,
                                                    final String externalInfrastructureLinkId,
                                                    final TransmodelScheduledStopPointDirection directionOnInfraLink,
                                                    final String label,
                                                    final Point measuredLocation,
                                                    final int priority,
                                                    final Optional<LocalDate> validityStart,
                                                    final Optional<LocalDate> validityEnd) {
        return ImmutableTransmodelScheduledStopPoint.builder()
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
