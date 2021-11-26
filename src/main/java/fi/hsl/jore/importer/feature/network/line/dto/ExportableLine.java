package fi.hsl.jore.importer.feature.network.line.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.immutables.value.Value;

/**
 * Contains the information of a line which is required when
 * we want to export lines to the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface ExportableLine {

    MultilingualString name();

    NetworkType networkType();

    MultilingualString shortName();

    static ImmutableExportableLine of (final MultilingualString name,
                                       final NetworkType networkType,
                                       final MultilingualString shortName) {
        return ImmutableExportableLine.builder()
                .name(name)
                .networkType(networkType)
                .shortName(shortName)
                .build();
    }
}
