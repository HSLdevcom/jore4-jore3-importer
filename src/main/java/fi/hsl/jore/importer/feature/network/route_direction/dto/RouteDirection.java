package fi.hsl.jore.importer.feature.network.route_direction.dto;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteDirectionsRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteDirectionsWithHistoryRecord;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

/**
 * At the moment this variation of the route direction is intended to be used with the
 * {@link fi.hsl.jore.importer.feature.network.route_direction.repository.IRouteDirectionTestRepository test
 * repository}.
 */
@Value.Immutable
public interface RouteDirection extends IHasPK<RouteDirectionPK>, IHasSystemTime, CommonFields<RouteDirection> {

    Optional<UUID> journeyPatternJore4Id();

    RoutePK routeId();

    Optional<UUID> routeJore4Id();

    static RouteDirection of(
            final RouteDirectionPK pk,
            final RoutePK routeId,
            final ExternalId externalId,
            final DirectionType direction,
            final Optional<Integer> lengthMeters,
            final MultilingualString name,
            final MultilingualString nameShort,
            final MultilingualString origin,
            final MultilingualString destination,
            final DateRange validTime,
            final TimeRange systemTime,
            final Optional<UUID> journeyPatternJore4Id,
            final Optional<UUID> routeJore4Id) {
        return ImmutableRouteDirection.builder()
                .pk(pk)
                .routeId(routeId)
                .externalId(externalId)
                .direction(direction)
                .lengthMeters(lengthMeters)
                .name(name)
                .nameShort(nameShort)
                .origin(origin)
                .destination(destination)
                .validTime(validTime)
                .systemTime(systemTime)
                .journeyPatternJore4Id(journeyPatternJore4Id)
                .routeJore4Id(routeJore4Id)
                .build();
    }

    static RouteDirection from(final NetworkRouteDirectionsRecord record, final IJsonbConverter converter) {
        return of(
                RouteDirectionPK.of(record.getNetworkRouteDirectionId()),
                RoutePK.of(record.getNetworkRouteId()),
                ExternalId.of(record.getNetworkRouteDirectionExtId()),
                DirectionType.of(record.getNetworkRouteDirectionType()),
                Optional.ofNullable(record.getNetworkRouteDirectionLength()),
                converter.fromJson(record.getNetworkRouteDirectionName(), MultilingualString.class),
                converter.fromJson(record.getNetworkRouteDirectionNameShort(), MultilingualString.class),
                converter.fromJson(record.getNetworkRouteDirectionOrigin(), MultilingualString.class),
                converter.fromJson(record.getNetworkRouteDirectionDestination(), MultilingualString.class),
                record.getNetworkRouteDirectionValidDateRange(),
                record.getNetworkRouteDirectionSysPeriod(),
                Optional.ofNullable(record.getJourneyPatternJore4Id()),
                Optional.ofNullable(record.getNetworkRouteJore4Id()));
    }

    static RouteDirection from(final NetworkRouteDirectionsWithHistoryRecord record, final IJsonbConverter converter) {
        return of(
                RouteDirectionPK.of(record.getNetworkRouteDirectionId()),
                RoutePK.of(record.getNetworkRouteId()),
                ExternalId.of(record.getNetworkRouteDirectionExtId()),
                DirectionType.of(record.getNetworkRouteDirectionType()),
                Optional.ofNullable(record.getNetworkRouteDirectionLength()),
                converter.fromJson(record.getNetworkRouteDirectionName(), MultilingualString.class),
                converter.fromJson(record.getNetworkRouteDirectionNameShort(), MultilingualString.class),
                converter.fromJson(record.getNetworkRouteDirectionOrigin(), MultilingualString.class),
                converter.fromJson(record.getNetworkRouteDirectionDestination(), MultilingualString.class),
                record.getNetworkRouteDirectionValidDateRange(),
                record.getNetworkRouteDirectionSysPeriod(),
                Optional.ofNullable(record.getJourneyPatternJore4Id()),
                Optional.ofNullable(record.getNetworkRouteJore4Id()));
    }
}
