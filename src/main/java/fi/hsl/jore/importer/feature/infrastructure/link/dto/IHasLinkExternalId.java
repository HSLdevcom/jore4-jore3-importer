package fi.hsl.jore.importer.feature.infrastructure.link.dto;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;

public interface IHasLinkExternalId extends IHasExternalId {
    NetworkType networkType();
}
