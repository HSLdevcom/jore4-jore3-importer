package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.batch.util.LineClassificationUtil;
import fi.hsl.jore.importer.feature.batch.util.TransitTypeToNetworkTypeMapper;
import fi.hsl.jore.importer.feature.jore3.entity.JrLine;
import fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class LineProcessor implements ItemProcessor<JrLine, PersistableLine> {

    @Override
    @Nullable
    public PersistableLine process(final JrLine item) {
        final String lineNumber =  item.lineId().displayId();
        final LegacyPublicTransportDestination lineLegacyPublicTransportDestination = item.lineId().destination();

        return PersistableLine.of(ExternalIdUtil.forLine(item),
                                  lineNumber,
                                  TransitTypeToNetworkTypeMapper.resolveNetworkType(item.transitType()),
                                  LineClassificationUtil.resolveTypeOfLine(item.transitType(),
                                                                           item.isTrunkLine(),
                                                                           item.publicTransportType(),
                                                                           lineNumber),
                                  LegacyHslMunicipalityCode.of(lineLegacyPublicTransportDestination)
        );
    }
}
