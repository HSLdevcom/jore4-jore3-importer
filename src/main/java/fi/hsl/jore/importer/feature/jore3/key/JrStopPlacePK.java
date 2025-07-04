package fi.hsl.jore.importer.feature.jore3.key;

import fi.hsl.jore.importer.feature.jore3.field.generated.StopPlaceId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasStopPlaceId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrStopPlacePK extends IHasStopPlaceId {
    static JrStopPlacePK of(final StopPlaceId stopPlaceId) {
        return ImmutableJrStopPlacePK.builder().stopPlaceId(stopPlaceId).build();
    }
}
