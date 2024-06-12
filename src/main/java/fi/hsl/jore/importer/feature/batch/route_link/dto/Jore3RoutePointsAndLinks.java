package fi.hsl.jore.importer.feature.batch.route_link.dto;

import fi.hsl.jore.importer.feature.network.route_link.dto.Jore3RouteLink;
import fi.hsl.jore.importer.feature.network.route_point.dto.Jore3RoutePoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.Jore3RouteStopPoint;
import io.vavr.collection.Vector;
import org.immutables.value.Value;

@Value.Immutable
public interface Jore3RoutePointsAndLinks {

    Vector<Jore3RoutePoint> routePoints();

    Vector<Jore3RouteStopPoint> stopPoints();

    Vector<Jore3RouteLink> routeLinks();

    static Jore3RoutePointsAndLinks of(
            final Vector<Jore3RoutePoint> routePoints,
            final Vector<Jore3RouteStopPoint> stopPoints,
            final Vector<Jore3RouteLink> routeLinks) {
        return ImmutableJore3RoutePointsAndLinks.builder()
                .routePoints(routePoints)
                .stopPoints(stopPoints)
                .routeLinks(routeLinks)
                .build();
    }
}
