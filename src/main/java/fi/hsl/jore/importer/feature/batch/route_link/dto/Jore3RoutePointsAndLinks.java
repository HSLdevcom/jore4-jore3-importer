package fi.hsl.jore.importer.feature.batch.route_link.dto;

import fi.hsl.jore.importer.feature.network.route_link.dto.Jore3RouteLink;
import fi.hsl.jore.importer.feature.network.route_point.dto.Jore3RoutePoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.Jore3RouteStopPoint;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface Jore3RoutePointsAndLinks {

    List<Jore3RoutePoint> routePoints();

    List<Jore3RouteStopPoint> stopPoints();

    List<Jore3RouteLink> routeLinks();

    static Jore3RoutePointsAndLinks of(
            final List<Jore3RoutePoint> routePoints,
            final List<Jore3RouteStopPoint> stopPoints,
            final List<Jore3RouteLink> routeLinks) {
        return ImmutableJore3RoutePointsAndLinks.builder()
                .routePoints(routePoints)
                .stopPoints(stopPoints)
                .routeLinks(routeLinks)
                .build();
    }
}
