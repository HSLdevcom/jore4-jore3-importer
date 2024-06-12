package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.jore3.enumerated.NodeType;
import fi.hsl.jore.importer.feature.jore3.enumerated.RegulatedTimingPointStatus;
import fi.hsl.jore.importer.feature.jore3.enumerated.StopPointPurpose;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.field.generated.RouteLinkId;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import fi.hsl.jore.importer.feature.jore3.key.JrRouteDirectionPk;
import fi.hsl.jore.importer.feature.jore3.key.JrRouteLinkPk;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasDirection;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasNodes;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasOrderNumber;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasRouteId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasRouteLinkId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasTransitType;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import java.time.LocalDate;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrRouteLink.TABLE)
public interface JrRouteLink
        extends IHasPrimaryKey<JrRouteLinkPk>,
                IHasRouteLinkId,
                IHasRouteId,
                IHasDirection,
                IHasTransitType,
                IHasNodes,
                IHasOrderNumber {

    String TABLE = "jr_reitinlinkki";

    @JoreColumn(name = "suuvoimast")
    LocalDate validFrom();

    @JoreColumn(name = "relpysakki", example = "E")
    NodeType startNodeType();

    // TODO: rl_relvpistaikpys

    @JoreColumn(name = "relohaikpys")
    StopPointPurpose stopPointPurpose();

    @JoreColumn(name = "ajantaspys")
    RegulatedTimingPointStatus regulatedTimingPointStatus();

    @JoreColumn(name = "paikka")
    boolean hastusStopPoint();

    @JoreColumn(name = "via")
    boolean viaPoint();

    @JoreColumn(name = "maaranpaa2")
    Optional<String> viaName();

    @JoreColumn(name = "maaranpaa2r")
    Optional<String> viaNameSwedish();

    @JoreColumn(name = "kirjaan")
    boolean includeInTimetable();

    @JoreColumn(name = "kirjasarake")
    Optional<Integer> timetableColumn();

    @Value.Derived
    default JrRouteLinkPk pk() {
        return JrRouteLinkPk.of(routeLinkId());
    }

    @Value.Derived
    default JrRouteDirectionPk fkRouteDirection() {
        return JrRouteDirectionPk.of(routeId(), direction(), validFrom());
    }

    @Value.Derived
    default JrLinkPk fkLink() {
        return JrLinkPk.of(transitType(), startNode(), endNode());
    }

    static JrRouteLink of(
            final RouteLinkId routeLinkId,
            final int orderNumber,
            final RouteId routeId,
            final Direction direction,
            final LocalDate validFrom,
            final TransitType transitType,
            final NodeId startNode,
            final NodeId endNode,
            final NodeType startNodeType,
            final RegulatedTimingPointStatus regulatedTimingPointStatus,
            final StopPointPurpose stopPointPurpose,
            final boolean hastusStopPoint,
            final boolean includeInTimetable,
            final boolean viaPoint,
            final Optional<String> viaName,
            final Optional<String> viaNameSwedish,
            final Optional<Integer> timetableColumn) {
        return ImmutableJrRouteLink.builder()
                // Fields of this route link
                .routeLinkId(routeLinkId)
                .orderNumber(orderNumber)
                .hastusStopPoint(hastusStopPoint)
                .includeInTimetable(includeInTimetable)
                .viaPoint(viaPoint)
                .viaName(viaName)
                .viaNameSwedish(viaNameSwedish)
                .timetableColumn(timetableColumn)
                .regulatedTimingPointStatus(regulatedTimingPointStatus)
                .stopPointPurpose(stopPointPurpose)
                .startNodeType(startNodeType)
                // Foreign key fields to the route direction
                .routeId(routeId)
                .direction(direction)
                .validFrom(validFrom)
                // Foreign key fields to the link
                .transitType(transitType)
                .startNode(startNode)
                .endNode(endNode)
                .build();
    }
}
