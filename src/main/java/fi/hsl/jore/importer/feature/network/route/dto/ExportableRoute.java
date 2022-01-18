package fi.hsl.jore.importer.feature.network.route.dto;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the source data of a route which is exported
 * to the Jore 4 database.
 */
@Value.Immutable
public interface ExportableRoute {

    DirectionType directionType();

    ExternalId externalId();

    MultilingualString name();

    UUID lineTransmodelId();

    String routeNumber();

    UUID startScheduledStopPointTransmodelId();

    UUID endScheduledStopPointTransmodelId();

    DateRange validDateRange();

    static ExportableRoute of(final DirectionType directionType,
                              final ExternalId externalId,
                              final MultilingualString name,
                              final UUID lineTransmodelId,
                              final String routeNumber,
                              final UUID startScheduledStopPointTransmodelId,
                              final UUID endScheduledStopPointTransmodelId,
                              final DateRange  validDateRange) {
        return ImmutableExportableRoute.builder()
                .directionType(directionType)
                .externalId(externalId)
                .name(name)
                .lineTransmodelId(lineTransmodelId)
                .routeNumber(routeNumber)
                .startScheduledStopPointTransmodelId(startScheduledStopPointTransmodelId)
                .endScheduledStopPointTransmodelId(endScheduledStopPointTransmodelId)
                .validDateRange(validDateRange)
                .build();
    }
}