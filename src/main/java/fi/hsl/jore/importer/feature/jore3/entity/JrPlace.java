package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.field.generated.PlaceId;
import fi.hsl.jore.importer.feature.jore3.key.JrPlacePk;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPlaceId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrPlace.TABLE)
public interface JrPlace extends IHasPrimaryKey<JrPlacePk>, IHasPlaceId {

    String TABLE = "jr_paikka";

    @Value.Derived
    default JrPlacePk pk() {
        return JrPlacePk.of(placeId());
    }

    @JoreColumn(name = "nimi", example = "Kalasatama")
    String name();

    static JrPlace of(final PlaceId placeId, final String name) {
        return ImmutableJrPlace.builder().placeId(placeId).name(name).build();
    }
}
