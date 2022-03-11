package fi.hsl.jore.importer.feature.transmodel.entity;

import org.immutables.value.Value;

/**
 * Contains the information that's required to link an infrastructure
 * link with a route.
 */
@Value.Immutable
public interface TransmodelRouteInfrastructureLink {

    String infrastructureLinkExtId();

    int infrastructureLinkSequence();

    String infrastructureLinkSource();

    boolean isTraversalForwards();

    static TransmodelRouteInfrastructureLink of(final String infrastructureLinkSource,
                                                final String infrastructureLinkExtId,
                                                final int infrastructureLinkSequence,
                                                final boolean isTraversalForwards) {
        return ImmutableTransmodelRouteInfrastructureLink.builder()
                .infrastructureLinkExtId(infrastructureLinkExtId)
                .infrastructureLinkSequence(infrastructureLinkSequence)
                .infrastructureLinkSource(infrastructureLinkSource)
                .isTraversalForwards(isTraversalForwards)
                .build();
    }
}
