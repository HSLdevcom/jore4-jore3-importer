package fi.hsl.jore.importer.feature.jore3.key;

import fi.hsl.jore.importer.feature.jore3.field.generated.PlaceId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPlaceId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrPlacePk extends IHasPlaceId {

    static JrPlacePk of(final PlaceId placeId) {
        return ImmutableJrPlacePk.builder()
                .placeId(placeId)
                .build();
    }
}
