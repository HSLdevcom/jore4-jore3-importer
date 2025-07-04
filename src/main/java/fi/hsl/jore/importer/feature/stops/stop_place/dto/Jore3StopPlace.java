package fi.hsl.jore.importer.feature.stops.stop_place.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

@Value.Immutable
public interface Jore3StopPlace extends CommonFields<Jore3StopPlace> {

    static Jore3StopPlace of(
            final ExternalId stopPlaceExternalId,
            final MultilingualString name,
            final MultilingualString longName,
            final MultilingualString location
            ) {
        return ImmutableJore3StopPlace.builder()
                .externalId(stopPlaceExternalId)
                .name(name)
                .longName(longName)
                .location(location)
                .build();
    }
}
