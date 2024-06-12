package fi.hsl.jore.importer.feature.jore4.entity;

import org.immutables.value.Value;

/** Contains the information that's required to link an infrastructure link with a route. */
@Value.Immutable
public interface Jore4RouteInfrastructureLink {

    String infrastructureLinkExtId();

    int infrastructureLinkSequence();

    String infrastructureLinkSource();

    boolean isTraversalForwards();

    static Jore4RouteInfrastructureLink of(
            final String infrastructureLinkSource,
            final String infrastructureLinkExtId,
            final int infrastructureLinkSequence,
            final boolean isTraversalForwards) {
        return ImmutableJore4RouteInfrastructureLink.builder()
                .infrastructureLinkExtId(infrastructureLinkExtId)
                .infrastructureLinkSequence(infrastructureLinkSequence)
                .infrastructureLinkSource(infrastructureLinkSource)
                .isTraversalForwards(isTraversalForwards)
                .build();
    }
}
