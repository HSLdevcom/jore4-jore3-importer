package fi.hsl.jore.importer.feature.batch.line_header;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.entity.JrLineHeader;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.line_header.dto.Jore3LineHeader;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class LineHeaderProcessor implements ItemProcessor<JrLineHeader, Jore3LineHeader> {

    // If you change this cutoff date, make sure to update the test LineHeaderProcessorTest's sql in
    // src/test/resources/sql/jore3/populate_line_headers.sql accordingly.
    static final LocalDate VALID_TO_CUTOFF = LocalDate.of(2020, 1, 1);

    @Override
    @Nullable
    public Jore3LineHeader process(final JrLineHeader item) {
        if (item.validTo().isBefore(VALID_TO_CUTOFF)) {
            return null;
        }

        return Jore3LineHeader.of(
                ExternalIdUtil.forLineHeader(item),
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
                DateRange.between(item.validFrom(), item.validTo()));
    }
}
