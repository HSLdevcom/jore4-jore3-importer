package fi.hsl.jore.importer.feature.infrastructure.network_type.cache;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;

public interface INetworkTypeCache {
    NetworkTypePK getCachedKey(final NetworkType networkType);
}
