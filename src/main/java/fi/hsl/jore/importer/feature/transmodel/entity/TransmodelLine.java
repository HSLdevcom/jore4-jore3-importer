package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Contains the information of a line which can
 * inserted into the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface TransmodelLine {

    String lineId();

    String externalLineId();

    MultilingualString name();

    MultilingualString shortName();

    VehicleMode primaryVehicleMode();

    int priority();

    Optional<LocalDateTime> validityStart();

    Optional<LocalDateTime> validityEnd();

    static ImmutableTransmodelLine of(final String lineId,
                                      final String externalLineId,
                                      final MultilingualString name,
                                      final MultilingualString shortName,
                                      final VehicleMode primaryVehicleMode,
                                      final int priority,
                                      final Optional<LocalDateTime> validityStart,
                                      final Optional<LocalDateTime> validityEnd) {
        return ImmutableTransmodelLine.builder()
                .lineId(lineId)
                .externalLineId(externalLineId)
                .name(name)
                .shortName(shortName)
                .primaryVehicleMode(primaryVehicleMode)
                .priority(priority)
                .validityStart(validityStart)
                .validityEnd(validityEnd)
                .build();
    }
}
