package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import java.util.UUID;
import org.immutables.value.Value;

/**
 * Contains the information that's required to update the Jore 4 id (primary key of a scheduled stop found from Jore 4
 * database) of a scheduled stop point stored in the database of the importer application.
 */
@Value.Immutable
public interface PersistableScheduledStopPointIdMapping {

    String externalId();

    UUID jore4Id();

    static PersistableScheduledStopPointIdMapping of(final String externalId, final UUID jore4Id) {
        return ImmutablePersistableScheduledStopPointIdMapping.builder()
                .externalId(externalId)
                .jore4Id(jore4Id)
                .build();
    }
}
