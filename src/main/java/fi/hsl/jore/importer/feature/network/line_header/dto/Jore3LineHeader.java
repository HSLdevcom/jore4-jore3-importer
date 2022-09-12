package fi.hsl.jore.importer.feature.network.line_header.dto;


import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import org.immutables.value.Value;

@Value.Immutable
public interface Jore3LineHeader
        extends CommonFields<Jore3LineHeader>,
                IHasExternalId {

    ExternalId lineId();

    static Jore3LineHeader of(final ExternalId externalId,
                              final ExternalId lineId,
                              final MultilingualString name,
                              final MultilingualString nameShort,
                              final MultilingualString origin1,
                              final MultilingualString origin2,
                              final DateRange validTime) {
        return ImmutableJore3LineHeader.builder()
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
