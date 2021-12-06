package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information that's required to update
 * the transmodel id (primary key of a scheduled stop found from
 * Jore 4 database) of a scheduled stop point stored in the database
 * of the importer application.
 */
@Value.Immutable
public interface PersistableScheduledStopPointTransmodelId {

    String externalId();

    UUID transmodelId();

    static PersistableScheduledStopPointTransmodelId of(final String externalId,
                                                        final UUID transmodelId) {
        return ImmutablePersistableScheduledStopPointTransmodelId.builder()
                .externalId(externalId)
                .transmodelId(transmodelId)
                .build();
    }
}
