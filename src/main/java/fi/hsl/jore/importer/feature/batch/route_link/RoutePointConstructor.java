package fi.hsl.jore.importer.feature.batch.route_link;

import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImportableRoutePoint;
import io.vavr.collection.Vector;

public final class RoutePointConstructor {

    private RoutePointConstructor() {
    }

    private static ImportableRoutePoint fromLink(final JrRouteLink link,
                                                 final int index) {
        return ImportableRoutePoint.of(ExternalIdUtil.forRouteLinkStartNode(link),
                                       ExternalIdUtil.forNode(link.startNode()),
                                       ExternalIdUtil.forRouteDirection(link.fkRouteDirection()),
                                       index);
    }

    private static ImportableRoutePoint fromLastLink(final JrRouteLink link,
                                                     final int index) {
        return ImportableRoutePoint.of(ExternalIdUtil.forRouteLinkEndNode(link),
                                       ExternalIdUtil.forNode(link.endNode()),
                                       ExternalIdUtil.forRouteDirection(link.fkRouteDirection()),
                                       index);
    }

    public static Vector<ImportableRoutePoint> extractPoints(final RouteLinksAndAttributes entity) {
        final Vector<ImportableRoutePoint> points =
                entity.routeLinks()
                      // The order number property is NOT guaranteed to be strictly incremental by 1,
                      // instead they can go 1, 2, 5, 10, 14.. => reindex them from [0, 1, 2...]
                      .zipWithIndex()
                      .map(linkAndIndex -> fromLink(linkAndIndex._1, linkAndIndex._2));
        final int lastIndex = points.size();
        return points
                .append(fromLastLink(entity.routeLinks().last(), lastIndex));
    }
}
