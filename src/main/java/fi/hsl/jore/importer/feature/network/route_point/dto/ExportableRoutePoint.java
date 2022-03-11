package fi.hsl.jore.importer.feature.network.route_point.dto;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

/**
 * Contains the information of a route point which is exported
 * to the Jore 4 database.
 */
@Value.Immutable
public interface ExportableRoutePoint {

    Point location();

    int orderNumber();

    Optional<Point> projectedLocation();

    NodeType type();

    Optional<String> stopPointElyNumber();

    Optional<String> stopPointShortId();

    static ExportableRoutePoint from(final Point location,
                                     final int orderNumber,
                                     final Optional<Point> projectedLocation,
                                     final NodeType type,
                                     final Optional<String> stopPointElyNumber,
                                     final Optional<String> stopPointShortId) {
        return ImmutableExportableRoutePoint.builder()
                .location(location)
                .orderNumber(orderNumber)
                .projectedLocation(projectedLocation)
                .type(type)
                .stopPointElyNumber(stopPointElyNumber)
                .stopPointShortId(stopPointShortId)
                .build();
    }
}
