package fi.hsl.jore.importer.feature.network.line.dto;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import org.immutables.value.Value;

/** Contains the information of a line that is required when we want to export lines to the Jore 4 Transmodel schema. */
@Value.Immutable
public interface ImporterLine {

    ExternalId externalIdOfLineHeader();

    String lineNumber();

    MultilingualString name();

    NetworkType networkType();

    MultilingualString shortName();

    DateRange validDateRange();

    TypeOfLine typeOfLine();

    LegacyHslMunicipalityCode legacyHslMunicipalityCode();

    static ImmutableImporterLine of(
            final ExternalId externalIdOfLineHeader,
            final String lineNumber,
            final MultilingualString name,
            final NetworkType networkType,
            final MultilingualString shortName,
            final DateRange validDateRange,
            final TypeOfLine typeOfLine,
            final LegacyHslMunicipalityCode legacyHslMunicipalityCode) {
        return ImmutableImporterLine.builder()
                .externalIdOfLineHeader(externalIdOfLineHeader)
                .lineNumber(lineNumber)
                .name(name)
                .networkType(networkType)
                .shortName(shortName)
                .validDateRange(validDateRange)
                .typeOfLine(typeOfLine)
                .legacyHslMunicipalityCode(legacyHslMunicipalityCode)
                .build();
    }
}
