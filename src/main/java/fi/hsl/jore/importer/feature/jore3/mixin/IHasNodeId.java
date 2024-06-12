package fi.hsl.jore.importer.feature.jore3.mixin;

import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;

public interface IHasNodeId {
    @JoreColumn(name = "soltunnus")
    NodeId nodeId();
}
