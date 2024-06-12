package fi.hsl.jore.importer.feature.network.line.dto;

import java.util.UUID;
import org.immutables.value.Value;

/**
 * Contains the information that's required to update the Jore 4 ID (primary key of a line found from Jore 4 database)
 * to the line header stored in the internal database of the importer application.
 */
@Value.Immutable
public interface PersistableLineIdMapping {

    String externalIdOfLineHeader();

    UUID jore4IdOfLine();

    static PersistableLineIdMapping of(final String externalIdOfLineHeader, final UUID jore4IdOfLine) {
        return ImmutablePersistableLineIdMapping.builder()
                .externalIdOfLineHeader(externalIdOfLineHeader)
                .jore4IdOfLine(jore4IdOfLine)
                .build();
    }
}
