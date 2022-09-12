package fi.hsl.jore.importer.feature.network.route.dto;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.UUID;

/**
 * Contains the source data of a route which is exported
 * to the Jore 4 database.
 */
@Value.Immutable
public interface ImporterRoute {

    UUID directionId();

    DirectionType directionType();

    MultilingualString name();

    UUID lineTransmodelId();

    String routeNumber();

    Optional<Short> hiddenVariant();

    DateRange validDateRange();

    LegacyHslMunicipalityCode legacyHslMunicipalityCode();

    static ImporterRoute of(final UUID directionId,
                            final DirectionType directionType,
                            final MultilingualString name,
                            final UUID lineTransmodelId,
                            final String routeNumber,
                            final Optional<Short> hiddenVariant,
                            final DateRange  validDateRange,
                            final LegacyHslMunicipalityCode legacyHslMunicipalityCode) {
        return ImmutableImporterRoute.builder()
                .directionId(directionId)
                .directionType(directionType)
                .name(name)
                .lineTransmodelId(lineTransmodelId)
                .routeNumber(routeNumber)
                .hiddenVariant(hiddenVariant)
                .validDateRange(validDateRange)
                .legacyHslMunicipalityCode(legacyHslMunicipalityCode)
                .build();
    }
}
