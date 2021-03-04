package fi.hsl.jore.importer.feature.jore.mixin;

import fi.hsl.jore.importer.feature.jore.field.TransitType;
import fi.hsl.jore.importer.feature.jore.mapping.JoreColumn;

public interface IHasTransitType {
    @JoreColumn(name = "lnkverkko")
    TransitType transitType();
}
