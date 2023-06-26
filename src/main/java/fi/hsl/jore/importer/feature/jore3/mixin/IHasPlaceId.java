package fi.hsl.jore.importer.feature.jore3.mixin;

import fi.hsl.jore.importer.feature.jore3.field.generated.PlaceId;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;

public interface IHasPlaceId {

    @JoreColumn(name = "paitunnus")
    PlaceId placeId();
}
