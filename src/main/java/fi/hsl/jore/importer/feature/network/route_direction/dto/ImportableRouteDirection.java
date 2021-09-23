package fi.hsl.jore.importer.feature.network.route_direction.dto;


import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface ImportableRouteDirection
        extends CommonFields<ImportableRouteDirection> {

    ExternalId routeId();

    static ImportableRouteDirection of(final ExternalId externalId,
                                       final ExternalId routeId,
                                       final DirectionType direction,
                                       final Optional<Integer> lengthMeters,
                                       final MultilingualString name,
                                       final MultilingualString nameShort,
                                       final MultilingualString origin,
                                       final MultilingualString destination,
                                       final DateRange validTime) {
        return ImmutableImportableRouteDirection.builder()
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
