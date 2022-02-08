package fi.hsl.jore.importer.feature.network.route.dto;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the source data of a journey pattern which is exported
 * to the Jore 4 database.
 */
@Value.Immutable
public interface ExportableJourneyPattern {

    UUID routeDirectionId();

    UUID routeTransmodelId();

    static ExportableJourneyPattern of(final UUID routeDirectionId,
                                       final UUID routeTransmodelId) {
        return ImmutableExportableJourneyPattern.builder()
                .routeDirectionId(routeDirectionId)
                .routeTransmodelId(routeTransmodelId)
                .build();
    }
}
