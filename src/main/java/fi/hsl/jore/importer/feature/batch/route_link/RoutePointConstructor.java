package fi.hsl.jore.importer.feature.batch.route_link;

import static fi.hsl.jore.importer.util.JoreCollectionUtils.concatToList;
import static fi.hsl.jore.importer.util.JoreCollectionUtils.mapWithIndex;

import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.network.route_point.dto.Jore3RoutePoint;
import java.util.List;

public final class RoutePointConstructor {

    private RoutePointConstructor() {}

    private static Jore3RoutePoint fromLink(final JrRouteLink link, final int index) {
        return Jore3RoutePoint.of(
                ExternalIdUtil.forRouteLinkStartNode(link),
                ExternalIdUtil.forNode(link.startNode()),
                ExternalIdUtil.forRouteDirection(link.fkRouteDirection()),
                index);
    }

    private static Jore3RoutePoint fromLastLink(final JrRouteLink link, final int index) {
        return Jore3RoutePoint.of(
                ExternalIdUtil.forRouteLinkEndNode(link),
                ExternalIdUtil.forNode(link.endNode()),
                ExternalIdUtil.forRouteDirection(link.fkRouteDirection()),
                index);
    }

    public static List<Jore3RoutePoint> extractPoints(final RouteLinksAndAttributes entity) {
        final List<JrRouteLink> links = entity.routeLinks();
        final List<Jore3RoutePoint> points =
                mapWithIndex(links, (index, link) -> fromLink(link, index)).toList();
        final int lastIndex = points.size();
        return concatToList(points, List.of(fromLastLink(links.get(links.size() - 1), lastIndex)));
    }
}
