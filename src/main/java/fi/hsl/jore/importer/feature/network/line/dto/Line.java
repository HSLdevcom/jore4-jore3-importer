package fi.hsl.jore.importer.feature.network.line.dto;

import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLinesRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLinesWithHistoryRecord;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.UUID;

@Value.Immutable
public interface Line
        extends IHasPK<LinePK>,
                IHasSystemTime,
                CommonFields<Line> {

    Optional<UUID> jore4Id();

    LegacyHslMunicipalityCode legacyHslMunicipalityCode();

    static Line of(final LinePK pk,
                   final ExternalId externalId,
                   final String exportId,
                   final NetworkType networkType,
                   final String lineNumber,
                   final TimeRange systemTime,
                   final Optional<UUID> jore4Id,
                   final TypeOfLine typeOfLine,
                   final LegacyHslMunicipalityCode legacyHslMunicipalityCode) {
        return ImmutableLine.builder()
                            .pk(pk)
                            .externalId(externalId)
                            .exportId(exportId)
                            .networkType(networkType)
                            .lineNumber(lineNumber)
                            .systemTime(systemTime)
                            .jore4Id(jore4Id)
                            .typeOfLine(typeOfLine)
                            .legacyHslMunicipalityCode(legacyHslMunicipalityCode)
                            .build();
    }

    static Line from(final NetworkLinesRecord record) {
        return of(
                LinePK.of(record.getNetworkLineId()),
                ExternalId.of(record.getNetworkLineExtId()),
                record.getNetworkLineExportId(),
                NetworkType.of(record.getInfrastructureNetworkType()),
                record.getNetworkLineNumber(),
                record.getNetworkLineSysPeriod(),
                Optional.ofNullable(record.getNetworkLineJore4Id()),
                TypeOfLine.of(record.getNetworkLineTypeOfLine()),
                LegacyHslMunicipalityCode.valueOf(record.getNetworkLineLegacyHslMunicipalityCode())
        );
    }

    static Line from(final NetworkLinesWithHistoryRecord record) {
        return of(
                LinePK.of(record.getNetworkLineId()),
                ExternalId.of(record.getNetworkLineExtId()),
                record.getNetworkLineExportId(),
                NetworkType.of(record.getInfrastructureNetworkType()),
                record.getNetworkLineNumber(),
                record.getNetworkLineSysPeriod(),
                Optional.ofNullable(record.getNetworkLineJore4Id()),
                TypeOfLine.of(record.getNetworkLineTypeOfLine()),
                LegacyHslMunicipalityCode.valueOf(record.getNetworkLineLegacyHslMunicipalityCode())
        );
    }
}
