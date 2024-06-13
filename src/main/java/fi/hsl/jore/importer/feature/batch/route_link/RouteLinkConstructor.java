package fi.hsl.jore.importer.feature.batch.route_link;

import static fi.hsl.jore.importer.util.JoreCollectionUtils.mapWithIndex;

import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.Jore3RouteLink;
import java.util.List;

public final class RouteLinkConstructor {

    private RouteLinkConstructor() {}

    private static Jore3RouteLink fromLink(final JrRouteLink link, final int index) {
        return Jore3RouteLink.of(
                ExternalIdUtil.forRouteLink(link),
                ExternalIdUtil.forLink(link.fkLink()),
                ExternalIdUtil.forRouteDirection(link.fkRouteDirection()),
                index);
    }

    public static List<Jore3RouteLink> extractLinks(final RouteLinksAndAttributes entity) {
        return mapWithIndex(entity.routeLinks(), (index, link) -> fromLink(link, index))
                .toList();
    }
}
