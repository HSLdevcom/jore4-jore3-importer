package fi.hsl.jore.importer.feature.batch.link;

import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.network_type.cache.INetworkTypeCache;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;
import fi.hsl.jore.importer.feature.jore.entity.JrLink;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;
import java.util.Objects;

import static fi.hsl.jore.importer.feature.batch.link.support.TransitTypeToNetworkTypeMapper.TO_NETWORK_TYPE;

public class LinkRowProcessor implements ItemProcessor<LinkRow, PersistableLink> {

    private final INetworkTypeCache networkTypeCache;

    public LinkRowProcessor(final INetworkTypeCache networkTypeCache) {
        this.networkTypeCache = Objects.requireNonNull(networkTypeCache);
    }

    private NetworkTypePK resolveNetworkType(final JrLink link) {
        // link -> transit type -> network type -> network type primary key
        final NetworkType networkType = TO_NETWORK_TYPE.getOrElse(link.transitType(),
                                                                  NetworkType.UNKNOWN);
        return networkTypeCache.getCachedKey(networkType);
    }

    @Override
    @Nullable
    public PersistableLink process(final LinkRow item) {
        final NetworkTypePK pk = resolveNetworkType(item.link());
        return PersistableLink.of(pk,
                                  item.geometry());
    }
}
