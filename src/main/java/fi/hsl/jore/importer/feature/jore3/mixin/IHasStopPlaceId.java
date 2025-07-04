package fi.hsl.jore.importer.feature.jore3.mixin;

import fi.hsl.jore.importer.feature.jore3.field.generated.StopPlaceId;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;

public interface IHasStopPlaceId {
    @JoreColumn(name = "pysalueid", example = "123456")
    StopPlaceId stopPlaceId();
}
