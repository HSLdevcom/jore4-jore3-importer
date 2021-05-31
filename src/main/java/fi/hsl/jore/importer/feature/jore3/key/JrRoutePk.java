package fi.hsl.jore.importer.feature.jore3.key;


import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasRouteId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrRoutePk extends IHasRouteId {
    static JrRoutePk of(final RouteId routeId) {
        return ImmutableJrRoutePk.builder()
                                 .routeId(routeId)
                                 .build();
    }
}
