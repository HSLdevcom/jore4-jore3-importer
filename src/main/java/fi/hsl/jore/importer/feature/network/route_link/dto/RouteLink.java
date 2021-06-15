package fi.hsl.jore.importer.feature.network.route_link.dto;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;
import fi.hsl.jore.importer.feature.network.route_link.dto.generated.RouteLinkPK;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteLinksRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkRouteLinksWithHistoryRecord;
import org.immutables.value.Value;

@Value.Immutable
public interface RouteLink
        extends IHasPK<RouteLinkPK>,
                IHasSystemTime,
                CommonFields {

    LinkPK link();

    RouteDirectionPK routeDirection();

    static RouteLink of(final RouteLinkPK pk,
                        final ExternalId externalId,
                        final LinkPK link,
                        final RouteDirectionPK routeDirection,
                        final int orderNumber,
                        final TimeRange systemTime) {
        return ImmutableRouteLink.builder()
                                 .pk(pk)
                                 .link(link)
                                 .routeDirection(routeDirection)
                                 .externalId(externalId)
                                 .orderNumber(orderNumber)
                                 .systemTime(systemTime)
                                 .build();
    }

    static RouteLink from(final NetworkRouteLinksRecord record) {
        return of(
                RouteLinkPK.of(record.getNetworkRouteLinkId()),
                ExternalId.of(record.getNetworkRouteLinkExtId()),
                LinkPK.of(record.getInfrastructureLinkId()),
                RouteDirectionPK.of(record.getNetworkRouteDirectionId()),
                record.getNetworkRouteLinkOrder(),
                record.getNetworkRouteLinkSysPeriod()
        );
    }

    static RouteLink from(final NetworkRouteLinksWithHistoryRecord record) {
        return of(
                RouteLinkPK.of(record.getNetworkRouteLinkId()),
                ExternalId.of(record.getNetworkRouteLinkExtId()),
                LinkPK.of(record.getInfrastructureLinkId()),
                RouteDirectionPK.of(record.getNetworkRouteDirectionId()),
                record.getNetworkRouteLinkOrder(),
                record.getNetworkRouteLinkSysPeriod()
        );
    }
}
