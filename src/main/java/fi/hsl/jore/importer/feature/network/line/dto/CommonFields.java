package fi.hsl.jore.importer.feature.network.line.dto;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;

public interface CommonFields<T> extends IHasExternalId {

    String lineNumber();

    NetworkType networkType();
}
