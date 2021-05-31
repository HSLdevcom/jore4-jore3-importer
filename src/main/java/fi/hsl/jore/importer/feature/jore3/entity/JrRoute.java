package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.key.JrLinePk;
import fi.hsl.jore.importer.feature.jore3.key.JrRoutePk;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasLineId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasRouteId;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import org.immutables.value.Value;

import java.util.Optional;


@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrRoute.TABLE)
public interface JrRoute
        extends IHasPrimaryKey<JrRoutePk>,
                IHasRouteId,
                IHasLineId {

    String TABLE = "jr_reitti";

    @Value.Derived
    default JrRoutePk pk() {
        return JrRoutePk.of(routeId());
    }

    @Value.Derived
    default JrLinePk fkLine() {
        return JrLinePk.of(lineId());
    }

    @JoreColumn(name = "reinimi",
                example = "Olympiaterminaali - Kamppi (M) - Ooppera")
    Optional<String> name();

    @JoreColumn(name = "reinimilyh",
                nullable = true,
                example = "Olympiaterminalen - Kampen (M) - Operan")
    Optional<String> nameSwedish();

    static JrRoute of(final RouteId routeId,
                      final LineId lineId,
                      final Optional<String> name,
                      final Optional<String> nameSwedish) {
        return ImmutableJrRoute.builder()
                               .routeId(routeId)
                               .lineId(lineId)
                               .name(name)
                               .nameSwedish(nameSwedish)
                               .build();
    }
}
