package fi.hsl.jore.importer.feature.batch.link;

import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.batch.util.TransitTypeToNetworkTypeMapper;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Jore3Link;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import jakarta.annotation.Nullable;
import org.springframework.batch.item.ItemProcessor;

public class LinkRowProcessor implements ItemProcessor<LinkRow, Jore3Link> {

    @Override
    @Nullable
    public Jore3Link process(final LinkRow item) {
        final ExternalId id = ExternalIdUtil.forLink(item.link());
        final NetworkType type =
                TransitTypeToNetworkTypeMapper.resolveNetworkType(item.link().transitType());
        return Jore3Link.of(
                id, type, item.geometry(), ExternalIdUtil.forNode(item.from()), ExternalIdUtil.forNode(item.to()));
    }
}
