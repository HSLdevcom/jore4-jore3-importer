package fi.hsl.jore.importer.feature.network.line.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import org.immutables.value.Value;

@Value.Immutable
public interface PersistableLine extends CommonFields<PersistableLine> {

    LegacyHslMunicipalityCode lineLegacyHslMunicipalityCode();

    static PersistableLine of(
            final ExternalId externalId,
            final String lineNumber,
            final NetworkType networkType,
            final TypeOfLine typeOfLine,
            final LegacyHslMunicipalityCode lineLegacyHslMunicipalityCode) {
        return ImmutablePersistableLine.builder()
                .externalId(externalId)
                .lineNumber(lineNumber)
                .networkType(networkType)
                .typeOfLine(typeOfLine)
                .lineLegacyHslMunicipalityCode(lineLegacyHslMunicipalityCode)
                .build();
    }
}
