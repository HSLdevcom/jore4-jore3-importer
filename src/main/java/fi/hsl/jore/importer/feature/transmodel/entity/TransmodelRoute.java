package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.immutables.value.Value;

import java.time.LocalDate;
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

    Optional<LocalDate> validityStart();

    Optional<LocalDate> validityEnd();

    Optional<Short> hiddenVariant();

    static TransmodelRoute of(final UUID routeId,
                              final MultilingualString description,
                              final TransmodelRouteDirection direction,
                              final UUID directionExtId,
                              final String label,
                              final Optional<Short> hiddenVariant,
                              final UUID lineId,
                              final int priority,
                              final Optional<LocalDate> validityStart,
                              final Optional<LocalDate> validityEnd) {
        return ImmutableTransmodelRoute.builder()
                .routeId(routeId)
                .description(description)
                .direction(direction)
                .directionExtId(directionExtId)
                .label(label)
                .hiddenVariant(hiddenVariant)
                .lineId(lineId)
                .priority(priority)
                .validityStart(validityStart)
                .validityEnd(validityEnd)
                .build();
    }
}
