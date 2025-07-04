package fi.hsl.jore.importer.feature.stops.stop_place.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;

public interface CommonFields<T> extends IHasExternalId {

    MultilingualString name();

    MultilingualString longName();

    MultilingualString location();
}
