package fi.hsl.jore.importer.feature.network.route_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

@Value.Immutable
public interface Jore3RoutePoint extends CommonFields {

    ExternalId node();

    ExternalId routeDirection();

    static Jore3RoutePoint of(
            final ExternalId externalId,
            final ExternalId nodeId,
            final ExternalId routeDirectionId,
            final int orderNumber) {
        return ImmutableJore3RoutePoint.builder()
                .externalId(externalId)
                .node(nodeId)
                .routeDirection(routeDirectionId)
                .orderNumber(orderNumber)
                .build();
    }
}
