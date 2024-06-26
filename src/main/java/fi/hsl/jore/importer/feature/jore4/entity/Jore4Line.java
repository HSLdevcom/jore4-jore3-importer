package fi.hsl.jore.importer.feature.jore4.entity;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

/** Contains the information of a line which can inserted into the Jore 4 Transmodel schema. */
@Value.Immutable
public interface Jore4Line {

    UUID lineId();

    String externalIdOfLineHeader();

    String label();

    MultilingualString name();

    MultilingualString shortName();

    VehicleMode primaryVehicleMode();

    TypeOfLine typeOfLine();

    int priority();

    Optional<LocalDate> validityStart();

    Optional<LocalDate> validityEnd();

    LegacyHslMunicipalityCode legacyHslMunicipalityCode();

    static ImmutableJore4Line of(
            final UUID lineId,
            final String externalIdOfLineHeader,
            final String label,
            final MultilingualString name,
            final MultilingualString shortName,
            final VehicleMode primaryVehicleMode,
            final TypeOfLine typeOfLine,
            final int priority,
            final Optional<LocalDate> validityStart,
            final Optional<LocalDate> validityEnd,
            final LegacyHslMunicipalityCode legacyHslMunicipalityCode) {
        return ImmutableJore4Line.builder()
                .lineId(lineId)
                .externalIdOfLineHeader(externalIdOfLineHeader)
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
