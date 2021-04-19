package fi.hsl.jore.importer.feature.jore3.mixin;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;

public interface IHasPointId {
    @JoreColumn(name = "pisid")
    int pointId();
}
