package fi.hsl.jore.importer.feature.network.route_link.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

@Value.Immutable
public interface Jore3RouteLink extends CommonFields {

    ExternalId link();

    ExternalId routeDirection();

    static Jore3RouteLink of(
            final ExternalId externalId,
            final ExternalId linkId,
            final ExternalId routeDirectionId,
            final int orderNumber) {
        return ImmutableJore3RouteLink.builder()
                .externalId(externalId)
                .link(linkId)
                .routeDirection(routeDirectionId)
                .orderNumber(orderNumber)
                .build();
    }
}
