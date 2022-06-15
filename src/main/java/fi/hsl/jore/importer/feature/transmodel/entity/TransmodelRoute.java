package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.checkerframework.checker.nullness.Opt;
import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Contains the information of a route which can be inserted into
 * the Jore 4 database.
 */
@Value.Immutable
public interface TransmodelRoute {

    UUID routeId();

    MultilingualString  description();

    TransmodelRouteDirection direction();

    UUID directionExtId();

    String label();

    UUID lineId();

    int priority();

    Optional<OffsetDateTime> validityStart();

    Optional<OffsetDateTime> validityEnd();

    static TransmodelRoute of(final UUID routeId,
                              final MultilingualString description,
                              final TransmodelRouteDirection direction,
                              final UUID directionExtId,
                              final String label,
                              final UUID lineId,
                              final int priority,
                              final Optional<OffsetDateTime> validityStart,
                              final Optional<OffsetDateTime> validityEnd) {
        return ImmutableTransmodelRoute.builder()
                .routeId(routeId)
                .description(description)
                .direction(direction)
                .directionExtId(directionExtId)
                .label(label)
                .lineId(lineId)
                .priority(priority)
                .validityStart(validityStart)
                .validityEnd(validityEnd)
                .build();
    }
}
