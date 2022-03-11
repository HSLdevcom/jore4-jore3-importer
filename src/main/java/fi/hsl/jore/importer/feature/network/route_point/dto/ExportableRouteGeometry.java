package fi.hsl.jore.importer.feature.network.route_point.dto;

import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

import java.util.UUID;

/**
 * Contains the route geometry of a route. This information
 * is used when the infrastructure links from the Jore 4
 * database are linked with the routes found from the same
 * database.
 */
@Value.Immutable
public interface ExportableRouteGeometry {

    LineString geometry();

    UUID routeDirectionId();

    String routeDirectionExtId();

    UUID routeTransmodelId();

    static ExportableRouteGeometry from(final LineString geometry,
                                        final UUID routeDirectionId,
                                        final String routeDirectionExtId,
                                        final UUID routeTransmodelId) {
        return ImmutableExportableRouteGeometry.builder()
                .geometry(geometry)
                .routeDirectionId(routeDirectionId)
                .routeDirectionExtId(routeDirectionExtId)
                .routeTransmodelId(routeTransmodelId)
                .build();
    }
}
