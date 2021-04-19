package fi.hsl.jore.importer.feature.batch.link.support;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.field.TransitType;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public final class TransitTypeToNetworkTypeMapper {

    public static final Map<TransitType, NetworkType> TO_NETWORK_TYPE =
            HashMap.of(TransitType.BUS, NetworkType.ROAD,
                       TransitType.SUBWAY, NetworkType.METRO_TRACK,
                       TransitType.TRAM, NetworkType.TRAM_TRACK,
                       TransitType.TRAIN, NetworkType.RAILWAY,
                       TransitType.FERRY, NetworkType.WATERWAY,
                       TransitType.UNKNOWN, NetworkType.UNKNOWN);

    private TransitTypeToNetworkTypeMapper() {
    }

    public static NetworkType resolveNetworkType(final TransitType transitType) {
        return TO_NETWORK_TYPE.getOrElse(transitType,
                                         NetworkType.UNKNOWN);
    }
}
