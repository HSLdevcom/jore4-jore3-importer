package fi.hsl.jore.importer.feature.jore3.entity;


import fi.hsl.jore.importer.feature.jore3.field.NodeType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.key.JrNodePk;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasNodeId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrNode.TABLE)
public interface JrNode
        extends IHasPrimaryKey<JrNodePk>,
                IHasNodeId {

    String TABLE = "jr_solmu";

    @Value.Derived
    default JrNodePk pk() {
        return JrNodePk.of(nodeId());
    }

    @JoreColumn(name = "soltunnus")
    NodeType nodeType();

    @JoreColumn(name = "solomx",
                srid = JoreGeometryUtil.JORE_SRID)
    double latitude();

    @JoreColumn(name = "solomy",
                srid = JoreGeometryUtil.JORE_SRID)
    double longitude();

    @JoreColumn(name = "solstmx",
                srid = JoreGeometryUtil.JORE_SRID)
    double projectedLatitude();

    @JoreColumn(name = "solstmy",
                srid = JoreGeometryUtil.JORE_SRID)
    double projectedLongitude();

    default Point location() {
        return JoreGeometryUtil.fromDbCoordinates(latitude(), longitude());
    }

    default Point projectedLocation() {
        return JoreGeometryUtil.fromDbCoordinates(projectedLatitude(), projectedLongitude());
    }

    static JrNode of(final NodeId nodeId,
                     final NodeType nodeType,
                     final double latitude,
                     final double longitude,
                     final double projectedLatitude,
                     final double projectedLongitude) {
        return ImmutableJrNode.builder()
                              .nodeId(nodeId)
                              .nodeType(nodeType)
                              .latitude(latitude)
                              .longitude(longitude)
                              .projectedLatitude(projectedLatitude)
                              .projectedLongitude(projectedLongitude)
                              .build();
    }
}
