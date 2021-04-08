package fi.hsl.jore.importer.feature.jore.entity;

import fi.hsl.jore.importer.feature.jore.field.TransitType;
import fi.hsl.jore.importer.feature.jore.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore.key.JrLinkPk;
import fi.hsl.jore.importer.feature.jore.key.JrPointPk;
import fi.hsl.jore.importer.feature.jore.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore.mapping.JoreForeignKey;
import fi.hsl.jore.importer.feature.jore.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore.mixin.IHasCoordinates;
import fi.hsl.jore.importer.feature.jore.mixin.IHasNodes;
import fi.hsl.jore.importer.feature.jore.mixin.IHasOrderNumber;
import fi.hsl.jore.importer.feature.jore.mixin.IHasPointId;
import fi.hsl.jore.importer.feature.jore.mixin.IHasTransitType;
import fi.hsl.jore.importer.feature.jore.style.JoreDtoStyle;
import fi.hsl.jore.importer.feature.jore.util.JoreGeometryUtil;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrPoint.TABLE)
public interface JrPoint
        extends IHasTransitType,
                IHasNodes,
                IHasOrderNumber,
                IHasCoordinates,
                IHasPointId {

    String TABLE = "jr_piste";

    @JoreColumn(name = "pisjarjnro")
    int orderNumber();

    @JoreColumn(name = "pismx",
                srid = JoreGeometryUtil.JORE_SRID)
    double latitude();

    @JoreColumn(name = "pismu",
                srid = JoreGeometryUtil.JORE_SRID)
    double longitude();

    @Value.Derived
    default JrPointPk pk() {
        return JrPointPk.of(pointId());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = JrLink.TABLE)
    default JrLinkPk fkLink() {
        return JrLinkPk.of(transitType(),
                           startNode(),
                           endNode());
    }

    static JrPoint of(final TransitType transitType,
                      final NodeId startNode,
                      final NodeId endNode,
                      final int pointId,
                      final int order,
                      final double latitude,
                      final double longitude) {
        return ImmutableJrPoint.builder()
                               .pointId(pointId)
                               .orderNumber(order)
                               .transitType(transitType)
                               .startNode(startNode)
                               .endNode(endNode)
                               .latitude(latitude)
                               .longitude(longitude)
                               .build();
    }
}
