package fi.hsl.jore.importer.feature.network.route_point.dto;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;
import fi.hsl.jore.importer.feature.network.route_point.dto.generated.RoutePointPK;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutePointsRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRoutePointsWithHistoryRecord;
import org.immutables.value.Value;

@Value.Immutable
public interface RoutePoint
        extends IHasPK<RoutePointPK>,
                IHasSystemTime,
                CommonFields {

    NodePK node();

    RouteDirectionPK routeDirection();

    static RoutePoint of(final RoutePointPK pk,
                         final NodePK nodeId,
                         final RouteDirectionPK routeDirectionId,
                         final ExternalId externalId,
                         final int orderNumber,
                         final TimeRange systemTime) {
        return ImmutableRoutePoint.builder()
                                  .pk(pk)
                                  .externalId(externalId)
                                  .node(nodeId)
                                  .routeDirection(routeDirectionId)
                                  .orderNumber(orderNumber)
                                  .systemTime(systemTime)
                                  .build();
    }

    static RoutePoint from(final NetworkRoutePointsRecord record) {
        return of(
                RoutePointPK.of(record.getNetworkRoutePointId()),
                NodePK.of(record.getInfrastructureNode()),
                RouteDirectionPK.of(record.getNetworkRouteDirectionId()),
                ExternalId.of(record.getNetworkRoutePointExtId()),
                record.getNetworkRoutePointOrder(),
                record.getNetworkRoutePointSysPeriod()
        );
    }

    static RoutePoint from(final NetworkRoutePointsWithHistoryRecord record) {
        return of(
                RoutePointPK.of(record.getNetworkRoutePointId()),
                NodePK.of(record.getInfrastructureNode()),
                RouteDirectionPK.of(record.getNetworkRouteDirectionId()),
                ExternalId.of(record.getNetworkRoutePointExtId()),
                record.getNetworkRoutePointOrder(),
                record.getNetworkRoutePointSysPeriod()
        );
    }
}
