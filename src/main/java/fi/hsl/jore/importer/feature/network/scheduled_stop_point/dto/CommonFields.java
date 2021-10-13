package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import org.locationtech.jts.geom.Point;

public interface CommonFields<T> extends IHasExternalId {

    MultilingualString name();
}
