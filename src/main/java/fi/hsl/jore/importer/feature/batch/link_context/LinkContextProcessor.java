package fi.hsl.jore.importer.feature.batch.link_context;

import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.network_type.cache.INetworkTypeCache;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;
import fi.hsl.jore.importer.feature.jore.entity.JrLink;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

import static fi.hsl.jore.importer.feature.batch.link_context.support.TransitTypeToNetworkTypeMapper.TO_NETWORK_TYPE;

public class LinkContextProcessor implements ItemProcessor<LinkContext, PersistableLink> {


    private final INetworkTypeCache networkTypeCache;

    public LinkContextProcessor(final INetworkTypeCache networkTypeCache) {
        this.networkTypeCache = networkTypeCache;
    }

    private NetworkTypePK resolveNetworkType(final JrLink link) {
        // link -> transit type -> network type -> network type primary key
        final NetworkType networkType = TO_NETWORK_TYPE.getOrElse(link.transitType(),
                                                                  NetworkType.UNKNOWN);
        return networkTypeCache.getCachedKey(networkType);
    }

    @Override
    @Nullable
    public PersistableLink process(final LinkContext item) {
        final NetworkTypePK pk = resolveNetworkType(item.link());
        return PersistableLink.of(pk,
                                  item.geometry());
    }
}
