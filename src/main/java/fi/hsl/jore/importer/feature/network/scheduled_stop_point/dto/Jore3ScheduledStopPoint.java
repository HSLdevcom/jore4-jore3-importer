package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * Contains the information of a one scheduled stop point which can
 * imported into the target database.
 */
@Value.Immutable
public interface Jore3ScheduledStopPoint
        extends CommonFields<Jore3ScheduledStopPoint>
{
    /**
     * Number of times used in routes
     */
    int usageInRoutes();

    static Jore3ScheduledStopPoint of(final ExternalId externalId,
                                      final Optional<Long> elynumber,
                                      final MultilingualString name,
                                      final Optional<String> shortId,
                                      final Optional<String> placeExternalId,
                                      final int usageInRoutes) {
        return ImmutableJore3ScheduledStopPoint.builder()
                .externalId(externalId)
                .elyNumber(elynumber)
                .name(name)
                .shortId(shortId)
                .placeExternalId(placeExternalId)
                .usageInRoutes(usageInRoutes)
                .build();
    }
}
