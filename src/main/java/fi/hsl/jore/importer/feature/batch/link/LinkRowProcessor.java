package fi.hsl.jore.importer.feature.batch.link;

import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.batch.link.support.TransitTypeToNetworkTypeMapper;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class LinkRowProcessor implements ItemProcessor<LinkRow, PersistableLink> {

    @Override
    @Nullable
    public PersistableLink process(final LinkRow item) {
        final ExternalId id = ExternalId.of(String.format("%s-%s",
                                                          item.from().nodeId().value(),
                                                          item.to().nodeId().value()));
        final NetworkType type = TransitTypeToNetworkTypeMapper.resolveNetworkType(item.link().transitType());
        return PersistableLink.of(id,
                                  type,
                                  item.geometry());
    }
}
