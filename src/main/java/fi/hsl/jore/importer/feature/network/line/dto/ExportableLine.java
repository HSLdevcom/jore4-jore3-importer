package fi.hsl.jore.importer.feature.network.line.dto;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.transmodel.entity.TypeOfLine;
import org.immutables.value.Value;

/**
 * Contains the information of a line which is required when
 * we want to export lines to the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface ExportableLine {

    ExternalId externalId();

    String lineNumber();

    MultilingualString name();

    NetworkType networkType();

    MultilingualString shortName();

    DateRange validDateRange();

    TypeOfLine typeOfLine();

    static ImmutableExportableLine of (final ExternalId externalId,
                                       final String lineNumber,
                                       final MultilingualString name,
                                       final NetworkType networkType,
                                       final MultilingualString shortName,
                                       final DateRange validDateRange,
                                       final TypeOfLine typeOfLine) {
        return ImmutableExportableLine.builder()
                .externalId(externalId)
                .lineNumber(lineNumber)
                .name(name)
                .networkType(networkType)
                .shortName(shortName)
                .validDateRange(validDateRange)
                .typeOfLine(typeOfLine)
                .build();
    }
}
