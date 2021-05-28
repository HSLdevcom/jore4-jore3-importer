package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.batch.util.TransitTypeToNetworkTypeMapper;
import fi.hsl.jore.importer.feature.jore3.entity.JrLine;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class LineProcessor implements ItemProcessor<JrLine, PersistableLine> {

    @Override
    @Nullable
    public PersistableLine process(final JrLine item) {
        return PersistableLine.of(ExternalIdUtil.forLine(item),
                                  item.lineId().displayId(),
                                  TransitTypeToNetworkTypeMapper.resolveNetworkType(item.transitType()));
    }
}
