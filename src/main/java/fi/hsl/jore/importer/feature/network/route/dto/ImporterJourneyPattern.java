package fi.hsl.jore.importer.feature.network.route.dto;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the source data of a journey pattern which is exported
 * to the Jore 4 database.
 */
@Value.Immutable
public interface ImporterJourneyPattern {

    UUID routeDirectionId();

    UUID routeJore4Id();

    static ImporterJourneyPattern of(final UUID routeDirectionId,
                                     final UUID routeJore4Id) {
        return ImmutableImporterJourneyPattern.builder()
                .routeDirectionId(routeDirectionId)
                .routeJore4Id(routeJore4Id)
                .build();
    }
}
