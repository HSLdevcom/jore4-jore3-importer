package fi.hsl.jore.importer.feature.network.line.dto;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;

public interface CommonFields<T> extends IHasExternalId {

    String exportId();

    String lineNumber();

    NetworkType networkType();

    TypeOfLine typeOfLine();
}
