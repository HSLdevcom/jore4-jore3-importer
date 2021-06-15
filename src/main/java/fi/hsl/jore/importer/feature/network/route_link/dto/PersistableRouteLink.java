package fi.hsl.jore.importer.feature.network.route_link.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;
import org.immutables.value.Value;

@Value.Immutable
public interface PersistableRouteLink
        extends CommonFields {

    LinkPK link();

    RouteDirectionPK routeDirection();

    static PersistableRouteLink of(final ExternalId externalId,
                                   final LinkPK linkId,
                                   final RouteDirectionPK routeDirectionId,
                                   final int orderNumber) {
        return ImmutablePersistableRouteLink.builder()
                                            .externalId(externalId)
                                            .link(linkId)
                                            .routeDirection(routeDirectionId)
                                            .orderNumber(orderNumber)
                                            .build();
    }
}
