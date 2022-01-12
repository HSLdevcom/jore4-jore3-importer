package fi.hsl.jore.importer.feature.transmodel.entity;

import org.immutables.value.Value;

/**
 * Contains the information of a journey pattern which can be
 * inserted into the Jore 4 database.
 */
@Value.Immutable
public interface TransmodelJourneyPattern {

    String journeyPatternId();

    String routeId();

    static TransmodelJourneyPattern of(final String journeyPatternId,
                                       final String routeId) {
        return ImmutableTransmodelJourneyPattern.builder()
                .journeyPatternId(journeyPatternId)
                .routeId(routeId)
                .build();
    }
}
