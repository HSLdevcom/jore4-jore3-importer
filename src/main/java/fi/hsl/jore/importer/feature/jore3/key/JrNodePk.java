package fi.hsl.jore.importer.feature.jore3.key;

import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasNodeId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrNodePk extends IHasNodeId {
    static JrNodePk of(final NodeId nodeId) {
        return ImmutableJrNodePk.builder().nodeId(nodeId).build();
    }
}
