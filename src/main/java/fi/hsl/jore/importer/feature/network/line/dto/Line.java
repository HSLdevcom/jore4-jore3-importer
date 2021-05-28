package fi.hsl.jore.importer.feature.network.line.dto;

import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLinesRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLinesWithHistoryRecord;
import org.immutables.value.Value;

@Value.Immutable
public interface Line
        extends IHasPK<LinePK>,
                IHasSystemTime,
                CommonFields<Line> {

    static Line of(final LinePK pk,
                   final ExternalId externalId,
                   final NetworkType networkType,
                   final String lineNumber,
                   final TimeRange systemTime) {
        return ImmutableLine.builder()
                            .pk(pk)
                            .externalId(externalId)
                            .networkType(networkType)
                            .lineNumber(lineNumber)
                            .systemTime(systemTime)
                            .build();
    }

    static Line from(final NetworkLinesRecord record) {
        return of(LinePK.of(record.getNetworkLineId()),
                  ExternalId.of(record.getNetworkLineExtId()),
                  NetworkType.of(record.getInfrastructureNetworkType()),
                  record.getNetworkLineNumber(),
                  record.getNetworkLineSysPeriod());
    }

    static Line from(final NetworkLinesWithHistoryRecord record) {
        return of(LinePK.of(record.getNetworkLineId()),
                  ExternalId.of(record.getNetworkLineExtId()),
                  NetworkType.of(record.getInfrastructureNetworkType()),
                  record.getNetworkLineNumber(),
                  record.getNetworkLineSysPeriod());
    }
}
