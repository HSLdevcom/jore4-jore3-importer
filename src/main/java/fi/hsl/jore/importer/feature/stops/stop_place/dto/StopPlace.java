package fi.hsl.jore.importer.feature.stops.stop_place.dto;

import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.stops.stop_place.dto.generated.StopPlacePK;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface StopPlace extends IHasPK<StopPlacePK>, CommonFields<StopPlace>, IHasSystemTime {

    Optional<String> jore4NetexId();

    static StopPlace of(
            final StopPlacePK pk,
            final ExternalId stopPlaceExternalId,
            final MultilingualString name,
            final MultilingualString longName,
            final MultilingualString location,
            final TimeRange systemTime
            ) {
        return ImmutableStopPlace.builder()
                .pk(pk)
                .externalId(stopPlaceExternalId)
                .name(name)
                .longName(longName)
                .location(location)
                .systemTime(systemTime)
                .build();
    }
}
