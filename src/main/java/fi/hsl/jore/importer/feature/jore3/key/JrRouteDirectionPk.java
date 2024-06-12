package fi.hsl.jore.importer.feature.jore3.key;

import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasDirection;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasRouteId;
import java.time.LocalDate;
import org.immutables.value.Value;

@Value.Immutable
public interface JrRouteDirectionPk extends IHasRouteId, IHasDirection {

    LocalDate validFrom();

    static JrRouteDirectionPk of(
            final RouteId routeId, final Direction direction, final LocalDate validFrom) {
        return ImmutableJrRouteDirectionPk.builder()
                .routeId(routeId)
                .direction(direction)
                .validFrom(validFrom)
                .build();
    }
}
