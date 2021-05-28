package fi.hsl.jore.importer.feature.batch.line_header;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.entity.JrLineHeader;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.line_header.dto.ImportableLineHeader;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class LineHeaderProcessor implements ItemProcessor<JrLineHeader, ImportableLineHeader> {

    @Override
    @Nullable
    public ImportableLineHeader process(final JrLineHeader item) {
        return ImportableLineHeader.of(ExternalIdUtil.forLineHeader(item),
                                       ExternalIdUtil.forLine(item.fkLine()),
                                       MultilingualString.empty()
                                                         .with(JoreLocaleUtil.FINNISH, item.name())
                                                         .with(JoreLocaleUtil.SWEDISH, item.nameSwedish()),
                                       MultilingualString.empty()
                                                         .with(JoreLocaleUtil.FINNISH, item.nameShort())
                                                         .with(JoreLocaleUtil.SWEDISH, item.nameShortSwedish()),
                                       MultilingualString.empty()
                                                         .with(JoreLocaleUtil.FINNISH, item.origin1())
                                                         .with(JoreLocaleUtil.SWEDISH, item.origin1Swedish()),
                                       MultilingualString.empty()
                                                         .with(JoreLocaleUtil.FINNISH, item.origin2())
                                                         .with(JoreLocaleUtil.SWEDISH, item.origin2Swedish()),
                                       DateRange.between(item.validFrom(),
                                                         item.validTo()));
    }
}
