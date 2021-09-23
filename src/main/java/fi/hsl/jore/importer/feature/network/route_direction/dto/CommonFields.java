package fi.hsl.jore.importer.feature.network.route_direction.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasValidDates;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;

import java.util.Optional;

public interface CommonFields<T>
        extends IHasExternalId,
                IHasValidDates {

    DirectionType direction();

    Optional<Integer> lengthMeters();

    MultilingualString name();

    MultilingualString nameShort();

    MultilingualString origin();

    MultilingualString destination();
}
