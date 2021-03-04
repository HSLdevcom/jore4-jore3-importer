package fi.hsl.jore.importer.feature.batch.link.support;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore.field.TransitType;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public final class TransitTypeToNetworkTypeMapper {

    public static final Map<TransitType, NetworkType> TO_NETWORK_TYPE =
            HashMap.of(TransitType.BUS, NetworkType.BUS,
                       TransitType.SUBWAY, NetworkType.SUBWAY,
                       TransitType.TRAM, NetworkType.TRAM,
                       TransitType.TRAIN, NetworkType.TRAIN,
                       TransitType.FERRY, NetworkType.FERRY,
                       TransitType.UNKNOWN, NetworkType.UNKNOWN);

    private TransitTypeToNetworkTypeMapper() {
    }
}
