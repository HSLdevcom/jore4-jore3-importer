package fi.hsl.jore.importer.feature.network.line.dto;

import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImmutablePersistableScheduledStopPointTransmodelId;
import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information that's required to update
 * the transmodel id (primary key of a line found from
 * Jore 4 database) of a line stored in the database
 * of the importer application.
 */
@Value.Immutable
public interface PersistableLineTransmodelId {

    String externalId();

    UUID transmodelId();

    static PersistableLineTransmodelId of(final String externalId,
                                          final UUID transmodelId) {
        return ImmutablePersistableLineTransmodelId.builder()
                .externalId(externalId)
                .transmodelId(transmodelId)
                .build();
    }
}
