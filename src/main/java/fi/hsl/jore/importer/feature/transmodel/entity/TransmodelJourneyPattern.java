package fi.hsl.jore.importer.feature.transmodel.entity;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information of a journey pattern which can be
 * inserted into the Jore 4 database.
 */
@Value.Immutable
public interface TransmodelJourneyPattern {

    UUID journeyPatternId();

    UUID routeDirectionExtId();

    UUID routeId();

    static TransmodelJourneyPattern of(final UUID journeyPatternId,
                                       final UUID routeDirectionExtId,
                                       final UUID routeId) {
        return ImmutableTransmodelJourneyPattern.builder()
                .journeyPatternId(journeyPatternId)
                .routeDirectionExtId(routeDirectionExtId)
                .routeId(routeId)
                .build();
    }
}
