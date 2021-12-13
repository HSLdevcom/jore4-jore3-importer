package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.checkerframework.checker.nullness.Opt;
import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Contains the information of a route which can be inserted into
 * the Jore 4 database.
 */
@Value.Immutable
public interface TransmodelRoute {

    String routeId();

    MultilingualString  description();

    TransmodelRouteDirection direction();

    String externalId();

    String label();

    String lineId();

    int priority();

    String startScheduledStopPointId();

    String endScheduledStopPointId();

    Optional<OffsetDateTime> validityStart();

    Optional<OffsetDateTime> validityEnd();

    static TransmodelRoute of(final String routeId,
                              final MultilingualString description,
                              final TransmodelRouteDirection direction,
                              final String externalId,
                              final String label,
                              final String lineId,
                              final int priority,
                              final String startScheduledStopPointId,
                              final String endScheduledStopPointId,
                              final Optional<OffsetDateTime> validityStart,
                              final Optional<OffsetDateTime> validityEnd) {
        return ImmutableTransmodelRoute.builder()
                .routeId(routeId)
                .description(description)
                .direction(direction)
                .externalId(externalId)
                .label(label)
                .lineId(lineId)
                .priority(priority)
                .startScheduledStopPointId(startScheduledStopPointId)
                .endScheduledStopPointId(endScheduledStopPointId)
                .validityStart(validityStart)
                .validityEnd(validityEnd)
                .build();
    }
}
