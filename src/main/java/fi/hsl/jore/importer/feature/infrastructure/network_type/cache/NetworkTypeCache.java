package fi.hsl.jore.importer.feature.infrastructure.network_type.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;
import fi.hsl.jore.importer.feature.infrastructure.network_type.repository.INetworkTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetworkTypeCache
        implements INetworkTypeCache {

    private final LoadingCache<NetworkType, NetworkTypePK> cache = CacheBuilder.newBuilder()
                                                                               .build(new NodeTypeToPKCacheLoader());

    private final INetworkTypeRepository networkTypeRepository;

    @Autowired
    public NetworkTypeCache(final INetworkTypeRepository networkTypeRepository) {
        this.networkTypeRepository = networkTypeRepository;
    }

    @Override
    public NetworkTypePK getCachedKey(final NetworkType networkType) {
        return cache.getUnchecked(networkType);
    }

    private class NodeTypeToPKCacheLoader extends CacheLoader<NetworkType, NetworkTypePK> {
        @Override
        public NetworkTypePK load(final NetworkType key) {
            return networkTypeRepository.findOrCreate(key);
        }
    }
}
