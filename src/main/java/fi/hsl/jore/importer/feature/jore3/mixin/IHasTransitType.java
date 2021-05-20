package fi.hsl.jore.importer.feature.jore3.mixin;

import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;

public interface IHasTransitType {
    @JoreColumn(name = "lnkverkko")
    TransitType transitType();
}
