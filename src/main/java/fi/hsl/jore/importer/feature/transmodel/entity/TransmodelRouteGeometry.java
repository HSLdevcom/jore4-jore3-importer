package fi.hsl.jore.importer.feature.transmodel.entity;

import io.vavr.collection.List;
import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains all infrastructure links of a route.
 */
@Value.Immutable
public interface TransmodelRouteGeometry {

    UUID routeId();

    List<TransmodelRouteInfrastructureLink> infrastructureLinks();

    static TransmodelRouteGeometry of(final UUID routeId,
                               final List<TransmodelRouteInfrastructureLink> infrastructureLinks) {
        return ImmutableTransmodelRouteGeometry.builder()
                .routeId(routeId)
                .infrastructureLinks(infrastructureLinks)
                .build();
    }
}
