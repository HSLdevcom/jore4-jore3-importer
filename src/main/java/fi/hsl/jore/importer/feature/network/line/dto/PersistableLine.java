package fi.hsl.jore.importer.feature.network.line.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.immutables.value.Value;

@Value.Immutable
public interface PersistableLine
        extends CommonFields<PersistableLine> {

    static PersistableLine of(final ExternalId externalId,
                              final String lineNumber,
                              final NetworkType networkType) {
        return ImmutablePersistableLine.builder()
                                       .externalId(externalId)
                                       .lineNumber(lineNumber)
                                       .networkType(networkType)
                                       .build();
    }
}
