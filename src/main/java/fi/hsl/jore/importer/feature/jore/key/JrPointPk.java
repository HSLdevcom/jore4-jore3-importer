package fi.hsl.jore.importer.feature.jore.key;


import fi.hsl.jore.importer.feature.jore.field.TransitType;
import fi.hsl.jore.importer.feature.jore.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore.mixin.IHasNodes;
import fi.hsl.jore.importer.feature.jore.mixin.IHasOrderNumber;
import fi.hsl.jore.importer.feature.jore.mixin.IHasPointId;
import fi.hsl.jore.importer.feature.jore.mixin.IHasTransitType;
import org.immutables.value.Value;

@Value.Immutable
public interface JrPointPk extends IHasTransitType,
                                   IHasNodes,
                                   IHasOrderNumber,
                                   IHasPointId {
    static JrPointPk of(final TransitType transitType,
                        final NodeId start,
                        final NodeId end,
                        final int orderNumber,
                        final int pointId) {
        return ImmutableJrPointPk.builder()
                                 .transitType(transitType)
                                 .startNode(start)
                                 .endNode(end)
                                 .orderNumber(orderNumber)
                                 .pointId(pointId)
                                 .build();
    }
}
