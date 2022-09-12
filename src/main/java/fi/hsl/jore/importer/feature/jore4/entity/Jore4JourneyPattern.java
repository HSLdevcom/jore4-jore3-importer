package fi.hsl.jore.importer.feature.jore4.entity;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information of a journey pattern which can be
 * inserted into the Jore 4 database.
 */
@Value.Immutable
public interface Jore4JourneyPattern {

    UUID journeyPatternId();

    UUID routeDirectionExtId();

    UUID routeId();

    static Jore4JourneyPattern of(final UUID journeyPatternId,
                                  final UUID routeDirectionExtId,
                                  final UUID routeId) {
        return ImmutableJore4JourneyPattern.builder()
                .journeyPatternId(journeyPatternId)
                .routeDirectionExtId(routeDirectionExtId)
                .routeId(routeId)
                .build();
    }
}
