package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Contains the information of a line which can
 * inserted into the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface TransmodelLine {

    UUID lineId();

    String externalLineId();

    String label();

    MultilingualString name();

    MultilingualString shortName();

    VehicleMode primaryVehicleMode();

    TypeOfLine typeOfLine();

    int priority();

    Optional<LocalDate> validityStart();

    Optional<LocalDate> validityEnd();

    LegacyHslMunicipalityCode legacyHslMunicipalityCode();

    static ImmutableTransmodelLine of(final UUID lineId,
                                      final String externalLineId,
                                      final String label,
                                      final MultilingualString name,
                                      final MultilingualString shortName,
                                      final VehicleMode primaryVehicleMode,
                                      final TypeOfLine typeOfLine,
                                      final int priority,
                                      final Optional<LocalDate> validityStart,
                                      final Optional<LocalDate> validityEnd,
                                      final LegacyHslMunicipalityCode legacyHslMunicipalityCode) {
        return ImmutableTransmodelLine.builder()
                .lineId(lineId)
                .externalLineId(externalLineId)
                .label(label)
                .name(name)
                .shortName(shortName)
                .primaryVehicleMode(primaryVehicleMode)
                .typeOfLine(typeOfLine)
                .priority(priority)
                .validityStart(validityStart)
                .validityEnd(validityEnd)
                .legacyHslMunicipalityCode(legacyHslMunicipalityCode)
                .build();
    }
}
