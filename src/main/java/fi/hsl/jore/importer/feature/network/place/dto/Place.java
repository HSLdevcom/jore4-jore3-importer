package fi.hsl.jore.importer.feature.network.place.dto;

import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.network.place.dto.generated.PlacePK;
import org.immutables.value.Value;

@Value.Immutable
public interface Place extends IHasPK<PlacePK>, IHasExternalId, IHasSystemTime {

    String name();

    static Place of(
            final PlacePK pk,
            final ExternalId externalId,
            final String name,
            final TimeRange systemTime) {

        return ImmutablePlace.builder()
                .pk(pk)
                .externalId(externalId)
                .name(name)
                .systemTime(systemTime)
                .build();
    }
}
