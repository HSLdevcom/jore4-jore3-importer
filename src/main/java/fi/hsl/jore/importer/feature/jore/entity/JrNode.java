package fi.hsl.jore.importer.feature.jore.entity;


import fi.hsl.jore.importer.feature.jore.field.NodeType;
import fi.hsl.jore.importer.feature.jore.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore.key.JrNodePk;
import fi.hsl.jore.importer.feature.jore.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore.mixin.IHasNodeId;
import fi.hsl.jore.importer.feature.jore.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore.style.JoreDtoStyle;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Coordinate;

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

    @JoreColumn(name = "solomx")
    double latitude();

    @JoreColumn(name = "solomy")
    double longitude();

    @JoreColumn(name = "solstmx")
    double projectedLatitude();

    @JoreColumn(name = "solstmy")
    double projectedLongitude();

    default Coordinate location() {
        return new Coordinate(longitude(), latitude(), 0);
    }

    default Coordinate projectedLocation() {
        return new Coordinate(projectedLongitude(), projectedLatitude(), 0);
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
