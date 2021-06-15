package fi.hsl.jore.importer.feature.network.route_point.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;
import org.immutables.value.Value;

@Value.Immutable
public interface PersistableRoutePoint
        extends CommonFields {

    NodePK node();

    RouteDirectionPK routeDirection();

    static PersistableRoutePoint of(final ExternalId externalId,
                                    final NodePK nodeId,
                                    final RouteDirectionPK routeDirectionId,
                                    final int orderNumber) {
        return ImmutablePersistableRoutePoint.builder()
                                             .externalId(externalId)
                                             .node(nodeId)
                                             .routeDirection(routeDirectionId)
                                             .orderNumber(orderNumber)
                                             .build();
    }
}
