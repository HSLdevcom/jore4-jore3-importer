package fi.hsl.jore.importer.feature.network.route_direction.dto;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information that's required to update
 * the journey pattern id (primary key of a journey pattern
 * found from Jore 4 database) of a route direction stored in
 * the database of the importer application.
 */
@Value.Immutable
public interface PersistableJourneyPatternIdMapping {

    UUID routeDirectionId();

    UUID journeyPatternId();

    static PersistableJourneyPatternIdMapping of(final UUID routeDirectionId,
                                                 final UUID journeyPatternId) {
        return ImmutablePersistableJourneyPatternIdMapping.builder()
                .routeDirectionId(routeDirectionId)
                .journeyPatternId(journeyPatternId)
                .build();
    }
}
