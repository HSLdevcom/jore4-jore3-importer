package fi.hsl.jore.importer.feature.jore.entity;


import fi.hsl.jore.importer.feature.jore.field.NodeType;
import fi.hsl.jore.importer.feature.jore.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore.key.JrNodePk;
import fi.hsl.jore.importer.feature.jore.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore.mixin.IHasNodeId;
import fi.hsl.jore.importer.feature.jore.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore.style.JoreDtoStyle;
import fi.hsl.jore.importer.feature.jore.util.JoreGeometryUtil;
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
                     final NodeType type,
                     final double latitude,
                     final double longitude,
                     final double projectedLatitude,
                     final double projectedLongitude) {
        return ImmutableJrNode.builder()
                              .nodeId(nodeId)
                              .nodeType(type)
                              .latitude(latitude)
                              .longitude(longitude)
                              .projectedLatitude(projectedLatitude)
                              .projectedLongitude(projectedLongitude)
                              .build();
    }
}
