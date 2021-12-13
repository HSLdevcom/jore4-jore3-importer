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
public interface PersistableRouteIdMapping {

    String externalId();

    UUID transmodelId();

    static PersistableRouteIdMapping of(final String externalId,
                                        final UUID transmodelId) {
        return ImmutablePersistableRouteIdMapping.builder()
                .externalId(externalId)
                .transmodelId(transmodelId)
                .build();
    }
}
