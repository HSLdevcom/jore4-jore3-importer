package fi.hsl.jore.importer.feature.jore4.entity;

import org.immutables.value.Value;

/**
 * Contains the information of a line external id
 * which can inserted into the Jore 4 database.
 */
@Value.Immutable
public interface Jore4LineExternalId {

    String label();

    Short externalId();

    static ImmutableJore4LineExternalId of(final String label,
                                           final Short externalId) {
        return ImmutableJore4LineExternalId.builder()
                .label(label)
                .externalId(externalId)
                .build();
    }
}
