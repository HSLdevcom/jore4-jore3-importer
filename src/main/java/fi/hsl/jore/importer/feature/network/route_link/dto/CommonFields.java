package fi.hsl.jore.importer.feature.network.route_link.dto;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;

public interface CommonFields
        extends IHasExternalId {

    // 0-indexed
    int orderNumber();
}
