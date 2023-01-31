package fi.hsl.jore.importer.feature.network.line.dto;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information that's required to update
 * the Jore 4 id (primary key of a line found from
 * Jore 4 database) of a line stored in the database
 * of the importer application.
 */
@Value.Immutable
public interface PersistableLineIdMapping {

    String externalId();

    UUID transmodelId();

    static PersistableLineIdMapping of(final String externalId,
                                       final UUID transmodelId) {
        return ImmutablePersistableLineIdMapping.builder()
                .externalId(externalId)
                .transmodelId(transmodelId)
                .build();
    }
}
