package fi.hsl.jore.importer.feature.network.route_direction.dto;


import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * At the moment this variation of the line header is intended to be used with
 * the {@link fi.hsl.jore.importer.feature.network.line_header.repository.ILineHeaderTestRepository test repository}.
 */
@Value.Immutable
public interface PersistableRouteDirection
        extends CommonFields<PersistableRouteDirection> {

    RoutePK routeId();

    static PersistableRouteDirection of(final ExternalId externalId,
                                        final RoutePK routeId,
                                        final DirectionType direction,
                                        final Optional<Integer> lengthMeters,
                                        final MultilingualString name,
                                        final MultilingualString nameShort,
                                        final MultilingualString origin,
                                        final MultilingualString destination,
                                        final DateRange validTime) {
        return ImmutablePersistableRouteDirection.builder()
                                                 .externalId(externalId)
                                                 .routeId(routeId)
                                                 .direction(direction)
                                                 .lengthMeters(lengthMeters)
                                                 .name(name)
                                                 .nameShort(nameShort)
                                                 .origin(origin)
                                                 .destination(destination)
                                                 .validTime(validTime)
                                                 .build();
    }
}
