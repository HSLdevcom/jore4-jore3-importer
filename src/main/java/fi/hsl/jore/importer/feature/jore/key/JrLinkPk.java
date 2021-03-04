package fi.hsl.jore.importer.feature.jore.key;


import fi.hsl.jore.importer.feature.jore.field.TransitType;
import fi.hsl.jore.importer.feature.jore.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore.mixin.IHasNodes;
import fi.hsl.jore.importer.feature.jore.mixin.IHasTransitType;
import org.immutables.value.Value;

@Value.Immutable
public interface JrLinkPk extends IHasTransitType,
                                  IHasNodes {
    static JrLinkPk of(final TransitType transitType,
                       final NodeId startNode,
                       final NodeId endNode) {
        return ImmutableJrLinkPk.builder()
                                .transitType(transitType)
                                .startNode(startNode)
                                .endNode(endNode)
                                .build();
    }
}
