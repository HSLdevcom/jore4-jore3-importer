package fi.hsl.jore.importer.feature.jore.key;


import fi.hsl.jore.importer.feature.jore.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore.mixin.IHasNodeId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrNodePk extends IHasNodeId {
    static JrNodePk of(final NodeId nodeId) {
        return ImmutableJrNodePk.builder()
                                .nodeId(nodeId)
                                .build();
    }
}
