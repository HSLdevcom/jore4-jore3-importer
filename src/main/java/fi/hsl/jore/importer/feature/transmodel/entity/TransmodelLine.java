package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.immutables.value.Value;

/**
 * Contains the information of a line which can
 * inserted into the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface TransmodelLine {

    MultilingualString name();

    MultilingualString shortName();

    VehicleMode primaryVehicleMode();

    static ImmutableTransmodelLine of(final MultilingualString name,
                                      final MultilingualString shortName,
                                      final VehicleMode primaryVehicleMode) {
        return ImmutableTransmodelLine.builder()
                .name(name)
                .shortName(shortName)
                .primaryVehicleMode(primaryVehicleMode)
                .build();
    }
}
