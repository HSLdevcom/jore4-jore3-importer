package fi.hsl.jore.importer.feature.jore.mixin;

import fi.hsl.jore.importer.feature.jore.mapping.JoreColumn;

public interface IHasPointId {
    @JoreColumn(name = "pisid")
    int pointId();
}
