package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import fi.hsl.jore.importer.feature.jore3.key.JrPointPk;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreForeignKey;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasCoordinates;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasNodes;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasOrderNumber;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPointId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasTransitType;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrPoint.TABLE)
public interface JrPoint extends IHasTransitType, IHasNodes, IHasOrderNumber, IHasCoordinates, IHasPointId {

    String TABLE = "jr_piste";

    @JoreColumn(name = "pisjarjnro")
    int orderNumber();

    @JoreColumn(name = "pismx", srid = JoreGeometryUtil.JORE_SRID)
    double latitude();

    @JoreColumn(name = "pismu", srid = JoreGeometryUtil.JORE_SRID)
    double longitude();

    @Value.Derived
    default JrPointPk pk() {
        return JrPointPk.of(pointId());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = JrLink.TABLE)
    default JrLinkPk fkLink() {
        return JrLinkPk.of(transitType(), startNode(), endNode());
    }

    static JrPoint of(
            final TransitType transitType,
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
