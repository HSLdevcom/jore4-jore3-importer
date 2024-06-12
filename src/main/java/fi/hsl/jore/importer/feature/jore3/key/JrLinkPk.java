package fi.hsl.jore.importer.feature.jore3.key;

import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasNodes;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasTransitType;
import org.immutables.value.Value;

@Value.Immutable
public interface JrLinkPk extends IHasTransitType, IHasNodes {
    static JrLinkPk of(final TransitType transitType, final NodeId startNode, final NodeId endNode) {
        return ImmutableJrLinkPk.builder()
                .transitType(transitType)
                .startNode(startNode)
                .endNode(endNode)
                .build();
    }
}
