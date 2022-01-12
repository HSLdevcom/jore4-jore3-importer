package fi.hsl.jore.importer.feature.network.route.dto;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information that's required to update
 * the transmodel id (primary key of a route found from
 * Jore 4 database) of a route stored in the database
 * of the importer application.
 */
@Value.Immutable
public interface PersistableJourneyPatternIdMapping {

    UUID routeId();

    UUID journeyPatternId();

    static PersistableJourneyPatternIdMapping of(final UUID routeId,
                                                 final UUID journeyPatternId) {
        return ImmutablePersistableJourneyPatternIdMapping.builder()
                .routeId(routeId)
                .journeyPatternId(journeyPatternId)
                .build();
    }
}
