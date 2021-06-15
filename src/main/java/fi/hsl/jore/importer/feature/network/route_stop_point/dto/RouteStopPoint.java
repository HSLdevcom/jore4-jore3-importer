package fi.hsl.jore.importer.feature.network.route_stop_point.dto;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.generated.RouteStopPointPK;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteStopPointsRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteStopPointsWithHistoryRecord;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface RouteStopPoint
        extends IHasPK<RouteStopPointPK>,
                IHasSystemTime,
                CommonFields {

    static RouteStopPoint of(final RouteStopPointPK pk,
                             final ExternalId externalId,
                             final int orderNumber,
                             final boolean hastusStopPoint,
                             final Optional<Integer> timetableColumn,
                             final TimeRange systemTime) {
        return ImmutableRouteStopPoint.builder()
                                      .pk(pk)
                                      .externalId(externalId)
                                      .orderNumber(orderNumber)
                                      .hastusStopPoint(hastusStopPoint)
                                      .timetableColumn(timetableColumn)
                                      .systemTime(systemTime)
                                      .build();
    }

    static RouteStopPoint from(final NetworkRouteStopPointsRecord record) {
        return of(
                RouteStopPointPK.of(record.getNetworkRoutePointId()),
                ExternalId.of(record.getNetworkRouteStopPointExtId()),
                record.getNetworkRouteStopPointOrder(),
                record.getNetworkRouteStopPointHastusPoint(),
                Optional.ofNullable(record.getNetworkRouteStopPointTimetableColumn()),
                record.getNetworkRouteStopPointSysPeriod()
        );
    }

    static RouteStopPoint from(final NetworkRouteStopPointsWithHistoryRecord record) {
        return of(
                RouteStopPointPK.of(record.getNetworkRoutePointId()),
                ExternalId.of(record.getNetworkRouteStopPointExtId()),
                record.getNetworkRouteStopPointOrder(),
                record.getNetworkRouteStopPointHastusPoint(),
                Optional.ofNullable(record.getNetworkRouteStopPointTimetableColumn()),
                record.getNetworkRouteStopPointSysPeriod()
        );
    }
}
