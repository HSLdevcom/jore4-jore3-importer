package fi.hsl.jore.importer.feature.jore4.entity;

import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

/** Contains the information of the Jore 4 infrastructure link which is closest to a given point. */
@Value.Immutable
public interface Jore4ClosestInfrastructureLink {

    String DIRECTION_FORWARD = "forward";
    String DIRECTION_BACKWARD = "backward";

    UUID infrastructureLinkId();

    String externalLinkId();

    /** The direction of traffic on the link: 'forward', 'backward' or 'bidirectional'. */
    String direction();

    /** The distance from the point of interest to the link, in metres. */
    double distanceInMeters();

    /**
     * Returns the stop point direction implied by a one-way link, or an empty optional if the link is bidirectional.
     */
    default Optional<Jore4ScheduledStopPointDirection> oneWayDirection() {
        switch (direction()) {
            case DIRECTION_FORWARD:
                return Optional.of(Jore4ScheduledStopPointDirection.FORWARD);
            case DIRECTION_BACKWARD:
                return Optional.of(Jore4ScheduledStopPointDirection.BACKWARD);
            default:
                return Optional.empty();
        }
    }

    static ImmutableJore4ClosestInfrastructureLink of(
            final UUID infrastructureLinkId,
            final String externalLinkId,
            final String direction,
            final double distanceInMeters) {
        return ImmutableJore4ClosestInfrastructureLink.builder()
                .infrastructureLinkId(infrastructureLinkId)
                .externalLinkId(externalLinkId)
                .direction(direction)
                .distanceInMeters(distanceInMeters)
                .build();
    }
}
