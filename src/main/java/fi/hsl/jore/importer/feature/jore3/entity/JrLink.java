package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import fi.hsl.jore.importer.feature.jore3.key.JrNodePk;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreForeignKey;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasNodes;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasTransitType;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * A link identifies a segment of a infrastructure network between two junctions.
 * <p>
 * For example, in a road network a link identifies the road section between two road
 * intersections.
 * <p>
 * Links only specify the start and end nodes of the segment, the actual precise
 * link path is described with a list of points.
 */
@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrLink.TABLE)
public interface JrLink
        extends IHasPrimaryKey<JrLinkPk>,
                IHasTransitType,
                IHasNodes {

    String TABLE = "jr_linkki";

    @JoreColumn(name = "lnkpituus")
    int length();

    @JoreColumn(name = "lnkmitpituus")
    Optional<Integer> measuredLength();

    @Value.Derived
    default JrLinkPk pk() {
        return JrLinkPk.of(transitType(),
                           startNode(),
                           endNode());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = JrNode.TABLE)
    default JrNodePk fkStartNode() {
        return JrNodePk.of(startNode());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = JrNode.TABLE)
    default JrNodePk fkEndNode() {
        return JrNodePk.of(endNode());
    }

    static JrLink of(final TransitType transitType,
                     final NodeId startNode,
                     final NodeId endNode,
                     final int length,
                     final Optional<Integer> measuredLength) {
        return ImmutableJrLink.builder()
                              .transitType(transitType)
                              .startNode(startNode)
                              .endNode(endNode)
                              .length(length)
                              .measuredLength(measuredLength)
                              .build();
    }
}
