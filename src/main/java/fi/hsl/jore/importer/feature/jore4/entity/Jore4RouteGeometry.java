package fi.hsl.jore.importer.feature.jore4.entity;

import io.vavr.collection.List;
import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains all infrastructure links of a route.
 */
@Value.Immutable
public interface Jore4RouteGeometry {

    UUID routeId();

    List<Jore4RouteInfrastructureLink> infrastructureLinks();

    static Jore4RouteGeometry of(final UUID routeId,
                                 final List<Jore4RouteInfrastructureLink> infrastructureLinks) {
        return ImmutableJore4RouteGeometry.builder()
                .routeId(routeId)
                .infrastructureLinks(infrastructureLinks)
                .build();
    }
}
