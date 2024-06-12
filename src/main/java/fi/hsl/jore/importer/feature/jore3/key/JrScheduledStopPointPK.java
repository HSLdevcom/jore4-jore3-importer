package fi.hsl.jore.importer.feature.jore3.key;

import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasNodeId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrScheduledStopPointPK extends IHasNodeId {
    static JrScheduledStopPointPK of(final NodeId nodeId) {
        return ImmutableJrScheduledStopPointPK.builder().nodeId(nodeId).build();
    }
}
