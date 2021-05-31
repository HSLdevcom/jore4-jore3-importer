package fi.hsl.jore.importer.feature.network.route.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;

public interface CommonFields<T> extends IHasExternalId {

    String routeNumber();

    MultilingualString name();

}
