package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.key.JrRouteDirectionPk;
import fi.hsl.jore.importer.feature.jore3.key.JrRoutePk;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasDirection;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasDuration;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasRouteId;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import java.time.LocalDate;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrRouteDirection.TABLE)
public interface JrRouteDirection
        extends IHasPrimaryKey<JrRouteDirectionPk>, IHasRouteId, IHasDirection, IHasDuration {
    String TABLE = "jr_reitinsuunta";

    @Value.Derived
    default JrRouteDirectionPk pk() {
        return JrRouteDirectionPk.of(routeId(), direction(), validFrom());
    }

    @Value.Derived
    default JrRoutePk fkRoute() {
        return JrRoutePk.of(routeId());
    }

    @JoreColumn(name = "suunimi", example = "Kaivoksela-Vantaankoski")
    String name();

    @JoreColumn(name = "suunimilyh", nullable = true, example = "Vantaankos-Kaivoksel")
    Optional<String> nameShort();

    @JoreColumn(name = "suunimir", nullable = true, example = "Gruvsta-Vandaforsen")
    Optional<String> nameSwedish();

    @JoreColumn(name = "suunimilyhr", nullable = true, example = "Vandaforsen-Gruvsta")
    Optional<String> nameShortSwedish();

    @JoreColumn(name = "suulahpaik", nullable = true, example = "Vantaankoski")
    Optional<String> origin();

    @JoreColumn(name = "suulahpaikr", nullable = true, example = "Vandaforsen")
    Optional<String> originSwedish();

    @JoreColumn(name = "suupaapaik", nullable = true, example = "Kaivoksela")
    Optional<String> destination();

    @JoreColumn(name = "suupaapaikr", nullable = true, example = "Gruvsta")
    Optional<String> destinationSwedish();

    @JoreColumn(name = "suupituus", nullable = true)
    Optional<Integer> lengthMeters();

    static JrRouteDirection of(
            final RouteId routeId,
            final Direction direction,
            final Optional<Integer> lengthMeters,
            final LocalDate validFrom,
            final LocalDate validTo,
            final String name,
            final Optional<String> nameShort,
            final Optional<String> nameSwedish,
            final Optional<String> nameShortSwedish,
            final Optional<String> origin,
            final Optional<String> originSwedish,
            final Optional<String> destination,
            final Optional<String> destinationSwedish) {
        return ImmutableJrRouteDirection.builder()
                .routeId(routeId)
                .direction(direction)
                .lengthMeters(lengthMeters)
                .validFrom(validFrom)
                .validTo(validTo)
                .name(name)
                .nameShort(nameShort)
                .nameSwedish(nameSwedish)
                .nameShortSwedish(nameShortSwedish)
                .origin(origin)
                .originSwedish(originSwedish)
                .destination(destination)
                .destinationSwedish(destinationSwedish)
                .build();
    }
}
