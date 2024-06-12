package fi.hsl.jore.importer.feature.network.route_point.dto;

import java.util.UUID;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

/**
 * Contains the route geometry of a route. This information is used when the infrastructure links from the Jore 4
 * database are linked with the routes found from the same database.
 */
@Value.Immutable
public interface ImporterRouteGeometry {

    LineString geometry();

    UUID routeDirectionId();

    String routeDirectionExtId();

    UUID routeJore4Id();

    static ImporterRouteGeometry from(
            final LineString geometry,
            final UUID routeDirectionId,
            final String routeDirectionExtId,
            final UUID routeJore4Id) {
        return ImmutableImporterRouteGeometry.builder()
                .geometry(geometry)
                .routeDirectionId(routeDirectionId)
                .routeDirectionExtId(routeDirectionExtId)
                .routeJore4Id(routeJore4Id)
                .build();
    }
}
