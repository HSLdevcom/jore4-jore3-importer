package fi.hsl.jore.importer.feature.network.line_header.dto;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.feature.network.line_header.dto.generated.LineHeaderPK;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLineHeadersRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLineHeadersWithHistoryRecord;
import org.immutables.value.Value;

@Value.Immutable
public interface LineHeader
        extends IHasPK<LineHeaderPK>,
                IHasSystemTime,
                CommonFields<LineHeader> {

    LinePK line();

    static LineHeader of(final LineHeaderPK pk,
                         final LinePK line,
                         final ExternalId externalId,
                         final MultilingualString name,
                         final MultilingualString nameShort,
                         final MultilingualString origin1,
                         final MultilingualString origin2,
                         final DateRange validTime,
                         final TimeRange systemTime) {
        return ImmutableLineHeader.builder()
                                  .pk(pk)
                                  .line(line)
                                  .externalId(externalId)
                                  .name(name)
                                  .nameShort(nameShort)
                                  .origin1(origin1)
                                  .origin2(origin2)
                                  .validTime(validTime)
                                  .systemTime(systemTime)
                                  .build();
    }

    static LineHeader from(final NetworkLineHeadersRecord record,
                           final IJsonbConverter converter) {
        return of(LineHeaderPK.of(record.getNetworkLineHeaderId()),
                  LinePK.of(record.getNetworkLineId()),
                  ExternalId.of(record.getNetworkLineHeaderExtId()),
                  converter.fromJson(record.getNetworkLineHeaderName(), MultilingualString.class),
                  converter.fromJson(record.getNetworkLineHeaderNameShort(), MultilingualString.class),
                  converter.fromJson(record.getNetworkLineHeaderOrigin_1(), MultilingualString.class),
                  converter.fromJson(record.getNetworkLineHeaderOrigin_2(), MultilingualString.class),
                  record.getNetworkLineHeaderValidDateRange(),
                  record.getNetworkLineHeaderSysPeriod());

    }

    static LineHeader from(final NetworkLineHeadersWithHistoryRecord record,
                           final IJsonbConverter converter) {
        return of(LineHeaderPK.of(record.getNetworkLineHeaderId()),
                  LinePK.of(record.getNetworkLineId()),
                  ExternalId.of(record.getNetworkLineHeaderExtId()),
                  converter.fromJson(record.getNetworkLineHeaderName(), MultilingualString.class),
                  converter.fromJson(record.getNetworkLineHeaderNameShort(), MultilingualString.class),
                  converter.fromJson(record.getNetworkLineHeaderOrigin_1(), MultilingualString.class),
                  converter.fromJson(record.getNetworkLineHeaderOrigin_2(), MultilingualString.class),
                  record.getNetworkLineHeaderValidDateRange(),
                  record.getNetworkLineHeaderSysPeriod());
    }
}
