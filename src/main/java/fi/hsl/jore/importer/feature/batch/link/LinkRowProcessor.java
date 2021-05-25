package fi.hsl.jore.importer.feature.batch.link;

import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.batch.link.support.TransitTypeToNetworkTypeMapper;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.ImportableLink;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class LinkRowProcessor implements ItemProcessor<LinkRow, ImportableLink> {

    @Override
    @Nullable
    public ImportableLink process(final LinkRow item) {
        final ExternalId id = ExternalIdUtil.forLink(item.link());
        final NetworkType type = TransitTypeToNetworkTypeMapper.resolveNetworkType(item.link().transitType());
        return ImportableLink.of(id,
                                 type,
                                 item.geometry(),
                                 ExternalIdUtil.forNode(item.from()),
                                 ExternalIdUtil.forNode(item.to()));
    }
}
