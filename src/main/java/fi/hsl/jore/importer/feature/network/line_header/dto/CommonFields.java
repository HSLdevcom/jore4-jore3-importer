package fi.hsl.jore.importer.feature.network.line_header.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasValidDates;

public interface CommonFields<T> extends IHasValidDates,
                                         IHasExternalId {

    MultilingualString name();

    MultilingualString nameShort();

    MultilingualString origin1();

    MultilingualString origin2();
}
