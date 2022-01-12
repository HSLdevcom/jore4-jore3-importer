package fi.hsl.jore.importer.feature.network.route.dto;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutesRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutesWithHistoryRecord;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.UUID;

@Value.Immutable
public interface Route
        extends IHasPK<RoutePK>,
                IHasSystemTime,
                CommonFields<Route> {

    LinePK line();

    Optional<UUID> transmodelId();

    Optional<UUID> journeyPatternTransmodelId();

    static Route of(final RoutePK pk,
                    final ExternalId externalId,
                    final LinePK line,
                    final String routeNumber,
                    final MultilingualString name,
                    final TimeRange systemTime,
                    final Optional<UUID> transmodelId,
                    final Optional<UUID> journeyPatternTransmodelId) {
        return ImmutableRoute.builder()
                .pk(pk)
                .externalId(externalId)
                .line(line)
                .routeNumber(routeNumber)
                .name(name)
                .systemTime(systemTime)
                .transmodelId(transmodelId)
                .journeyPatternTransmodelId(journeyPatternTransmodelId)
                .build();
    }

    static Route from(final NetworkRoutesRecord record,
                      final IJsonbConverter converter) {
        return of(RoutePK.of(record.getNetworkRouteId()),
                ExternalId.of(record.getNetworkRouteExtId()),
                LinePK.of(record.getNetworkLineId()),
                record.getNetworkRouteNumber(),
                converter.fromJson(record.getNetworkRouteName(), MultilingualString.class),
                record.getNetworkRouteSysPeriod(),
                Optional.ofNullable(record.getNetworkRouteTransmodelId()),
                Optional.ofNullable(record.getJourneyPatternTransmodelId())
        );
    }

    static Route from(final NetworkRoutesWithHistoryRecord record,
                      final IJsonbConverter converter) {
        return of(RoutePK.of(record.getNetworkRouteId()),
                ExternalId.of(record.getNetworkRouteExtId()),
                LinePK.of(record.getNetworkLineId()),
                record.getNetworkRouteNumber(),
                converter.fromJson(record.getNetworkRouteName(), MultilingualString.class),
                record.getNetworkRouteSysPeriod(),
                Optional.ofNullable(record.getNetworkRouteTransmodelId()),
                Optional.ofNullable(record.getJourneyPatternTransmodelId())
        );
    }
}
