package fi.hsl.jore.importer.feature.batch.route_link;

import fi.hsl.jore.importer.feature.batch.route_link.dto.ImportableRoutePointsAndLinks;
import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteDirection;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImportableRoutePoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.ImportableRouteStopPoint;
import io.micrometer.core.lang.NonNull;
import org.springframework.batch.item.ItemProcessor;

/**
 * Given a list of Jore {@link JrRouteLink links} for a particular {@link JrRouteDirection route direction}
 * (embedded within a {@link RouteLinksAndAttributes container}), construct list of route {@link ImportableRoutePoint points} and
 * route {@link ImportableRouteStopPoint stop points} (embedded into a {@link ImportableRoutePointsAndLinks container}).
 * <p>
 * Note that it's not guaranteed that a route direction starts and ends in a bus stop! This restriction applies
 * to current route directions, but historic route directions might start or end with e.g. a cross roads junction.
 * Ossi Berg suggested that such leading or trailing non-bus-stop points could be filtered out, however the current
 * implementation does not implement such filtering and instead preserves the original structure.
 */
public class RouteLinksProcessor implements ItemProcessor<RouteLinksAndAttributes, ImportableRoutePointsAndLinks> {

    @Override
    @NonNull
    public ImportableRoutePointsAndLinks process(final RouteLinksAndAttributes item) {
        return ImportableRoutePointsAndLinks.of(RoutePointConstructor.extractPoints(item),
                                                RouteStopPointConstructor.extractStopPoints(item),
                                                RouteLinkConstructor.extractLinks(item));
    }
}
