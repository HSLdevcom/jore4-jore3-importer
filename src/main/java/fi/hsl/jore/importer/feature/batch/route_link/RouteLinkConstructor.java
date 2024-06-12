package fi.hsl.jore.importer.feature.batch.route_link;

import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.Jore3RouteLink;
import io.vavr.collection.Vector;

public final class RouteLinkConstructor {

    private RouteLinkConstructor() {}

    private static Jore3RouteLink fromLink(final JrRouteLink link, final int index) {
        return Jore3RouteLink.of(
                ExternalIdUtil.forRouteLink(link),
                ExternalIdUtil.forLink(link.fkLink()),
                ExternalIdUtil.forRouteDirection(link.fkRouteDirection()),
                index);
    }

    public static Vector<Jore3RouteLink> extractLinks(final RouteLinksAndAttributes entity) {
        return entity.routeLinks()
                // The order number property is NOT guaranteed to be strictly incremental by 1,
                // instead they can go 1, 2, 5, 10, 14.. => reindex them from [0, 1, 2...]
                .zipWithIndex()
                .map(linkAndIndex -> fromLink(linkAndIndex._1, linkAndIndex._2));
    }
}
