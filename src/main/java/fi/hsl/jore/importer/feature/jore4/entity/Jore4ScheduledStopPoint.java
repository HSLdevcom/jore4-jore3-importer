package fi.hsl.jore.importer.feature.jore4.entity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

/** Contains the information of a scheduled stop point which can be written to the Jore 4 transmodel schema. */
@Value.Immutable
public interface Jore4ScheduledStopPoint {

    UUID scheduledStopPointId();

    /** The id of the Jore 4 infrastructure link on which the stop point is located. */
    UUID infrastructureLinkId();

    /** The external id of the infrastructure link. Kept for logging and traceability. */
    String externalInfrastructureLinkId();

    String externalScheduledStopPointId();

    Jore4ScheduledStopPointDirection directionOnInfraLink();

    /** The vehicle mode which is used for the stop point and its infrastructure link. */
    VehicleMode vehicleMode();

    String label();

    Point measuredLocation();

    Optional<String> timingPlaceLabel();

    int priority();

    Optional<LocalDate> validityStart();

    Optional<LocalDate> validityEnd();

    static ImmutableJore4ScheduledStopPoint of(
            final UUID scheduledStopPointId,
            final String externalScheduledStopPointId,
            final UUID infrastructureLinkId,
            final String externalInfrastructureLinkId,
            final Jore4ScheduledStopPointDirection directionOnInfraLink,
            final VehicleMode vehicleMode,
            final String label,
            final Point measuredLocation,
            final Optional<String> timingPlaceLabel,
            final int priority,
            final Optional<LocalDate> validityStart,
            final Optional<LocalDate> validityEnd) {
        return ImmutableJore4ScheduledStopPoint.builder()
                .scheduledStopPointId(scheduledStopPointId)
                .externalScheduledStopPointId(externalScheduledStopPointId)
                .infrastructureLinkId(infrastructureLinkId)
                .externalInfrastructureLinkId(externalInfrastructureLinkId)
                .directionOnInfraLink(directionOnInfraLink)
                .vehicleMode(vehicleMode)
                .label(label)
                .measuredLocation(measuredLocation)
                .timingPlaceLabel(timingPlaceLabel)
                .priority(priority)
                .validityStart(validityStart)
                .validityEnd(validityEnd)
                .build();
    }
}
