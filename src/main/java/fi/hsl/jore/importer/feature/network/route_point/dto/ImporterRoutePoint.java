package fi.hsl.jore.importer.feature.network.route_point.dto;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import java.util.Optional;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

/** Contains the information of a route point which is exported to the Jore 4 database. */
@Value.Immutable
public interface ImporterRoutePoint {

    Point location();

    int orderNumber();

    Optional<Point> projectedLocation();

    NodeType type();

    Optional<Long> stopPointElyNumber();

    Optional<String> stopPointShortId();

    static ImporterRoutePoint from(
            final Point location,
            final int orderNumber,
            final Optional<Point> projectedLocation,
            final NodeType type,
            final Optional<Long> stopPointElyNumber,
            final Optional<String> stopPointShortId) {
        return ImmutableImporterRoutePoint.builder()
                .location(location)
                .orderNumber(orderNumber)
                .projectedLocation(projectedLocation)
                .type(type)
                .stopPointElyNumber(stopPointElyNumber)
                .stopPointShortId(stopPointShortId)
                .build();
    }
}
