package fi.hsl.jore.importer.feature.jore3.mixin;


import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;

public interface IHasRouteId {
    @JoreColumn(name = "reitunnus")
    RouteId routeId();
}
