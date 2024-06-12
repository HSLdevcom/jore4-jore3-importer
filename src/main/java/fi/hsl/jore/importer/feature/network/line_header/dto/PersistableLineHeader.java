package fi.hsl.jore.importer.feature.network.line_header.dto;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import org.immutables.value.Value;

/**
 * At the moment this variation of the line header is intended to be used with the {@link
 * fi.hsl.jore.importer.feature.network.line_header.repository.ILineHeaderTestRepository test
 * repository}.
 */
@VisibleForTesting
@Value.Immutable
public interface PersistableLineHeader extends CommonFields<PersistableLineHeader> {

    LinePK lineId();

    static PersistableLineHeader of(
            final ExternalId externalId,
            final LinePK lineId,
            final MultilingualString name,
            final MultilingualString nameShort,
            final MultilingualString origin1,
            final MultilingualString origin2,
            final DateRange validTime) {
        return ImmutablePersistableLineHeader.builder()
                .externalId(externalId)
                .lineId(lineId)
                .name(name)
                .nameShort(nameShort)
                .origin1(origin1)
                .origin2(origin2)
                .validTime(validTime)
                .build();
    }
}
