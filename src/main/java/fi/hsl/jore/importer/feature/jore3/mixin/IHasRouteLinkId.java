package fi.hsl.jore.importer.feature.jore3.mixin;


import fi.hsl.jore.importer.feature.jore3.field.generated.RouteLinkId;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;

public interface IHasRouteLinkId {
    @JoreColumn(name = "relid")
    RouteLinkId routeLinkId();
}
