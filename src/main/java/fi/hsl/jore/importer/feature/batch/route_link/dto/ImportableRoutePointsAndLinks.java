package fi.hsl.jore.importer.feature.batch.route_link.dto;

import fi.hsl.jore.importer.feature.network.route_link.dto.ImportableRouteLink;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImportableRoutePoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.ImportableRouteStopPoint;
import io.vavr.collection.Vector;
import org.immutables.value.Value;

@Value.Immutable
public interface ImportableRoutePointsAndLinks {

    Vector<ImportableRoutePoint> routePoints();

    Vector<ImportableRouteStopPoint> stopPoints();

    Vector<ImportableRouteLink> routeLinks();

    static ImportableRoutePointsAndLinks of(final Vector<ImportableRoutePoint> routePoints,
                                            final Vector<ImportableRouteStopPoint> stopPoints,
                                            final Vector<ImportableRouteLink> routeLinks) {
        return ImmutableImportableRoutePointsAndLinks.builder()
                                                     .routePoints(routePoints)
                                                     .stopPoints(stopPoints)
                                                     .routeLinks(routeLinks)
                                                     .build();
    }
}
