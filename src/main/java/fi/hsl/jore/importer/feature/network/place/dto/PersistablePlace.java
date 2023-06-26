package fi.hsl.jore.importer.feature.network.place.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import org.immutables.value.Value;

@Value.Immutable
public interface PersistablePlace extends IHasExternalId {

    String name();

    static PersistablePlace of(final ExternalId externalId,
                               final String name) {

        return ImmutablePersistablePlace.builder()
                .externalId(externalId)
                .name(name)
                .build();
    }
}
