package fi.hsl.jore.importer.feature.network.route_direction.dto;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information that's required to update
 * the Jore 4 id (primary key of a route found from
 * Jore 4 database) of a route direction stored in the database
 * of the importer application.
 */
@Value.Immutable
public interface PersistableRouteIdMapping {

    UUID routeDirectionId();

    UUID transmodelId();

    static PersistableRouteIdMapping of(final UUID routeDirectionId,
                                        final UUID transmodelId) {
        return ImmutablePersistableRouteIdMapping.builder()
                .routeDirectionId(routeDirectionId)
                .transmodelId(transmodelId)
                .build();
    }
}
