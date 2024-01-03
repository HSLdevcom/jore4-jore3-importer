package fi.hsl.jore.importer.feature.network.line.dto;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information that's required to update the Jore 4 ID (primary
 * key of a line found from the Jore 4 database) of a line stored in the
 * database of the importer application.
 */
@Value.Immutable
public interface PersistableLineIdMapping {

    String externalIdOfLine();

    UUID jore4IdOfLine();

    static PersistableLineIdMapping of(final String externalIdOfLine,
                                       final UUID jore4IdOfLine) {
        return ImmutablePersistableLineIdMapping.builder()
                .externalIdOfLine(externalIdOfLine)
                .jore4IdOfLine(jore4IdOfLine)
                .build();
    }
}
