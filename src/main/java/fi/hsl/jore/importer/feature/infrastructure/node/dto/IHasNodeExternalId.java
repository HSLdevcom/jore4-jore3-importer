package fi.hsl.jore.importer.feature.infrastructure.node.dto;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasExternalId;

/**
 * Fields used to identify this entity in external systems
 */
public interface IHasNodeExternalId extends IHasExternalId {
    NodeType nodeType();
}
