package fi.hsl.jore.importer.feature.batch.util;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.entity.JrLine;
import fi.hsl.jore.importer.feature.jore3.entity.JrLineHeader;
import fi.hsl.jore.importer.feature.jore3.entity.JrLink;
import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import fi.hsl.jore.importer.feature.jore3.entity.JrRoute;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteDirection;
import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.key.JrLineHeaderPk;
import fi.hsl.jore.importer.feature.jore3.key.JrLinePk;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import fi.hsl.jore.importer.feature.jore3.key.JrNodePk;
import fi.hsl.jore.importer.feature.jore3.key.JrRouteDirectionPk;
import fi.hsl.jore.importer.feature.jore3.key.JrRoutePk;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class ExternalIdUtil {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private ExternalIdUtil() {
    }

    public static ExternalId forNode(final NodeId nodeId) {
        return ExternalId.of(nodeId.value());
    }

    public static ExternalId forNode(final JrNodePk nodePk) {
        return forNode(nodePk.nodeId());
    }

    public static ExternalId forNode(final JrNode node) {
        return forNode(node.pk());
    }

    public static ExternalId forLink(final TransitType transitType,
                                     final NodeId from,
                                     final NodeId to) {
        return ExternalId.of(String.format("%d-%s-%s",
                                           transitType.value(),
                                           from.value(),
                                           to.value()));
    }

    public static ExternalId forLink(final JrLinkPk linkPk) {
        return forLink(linkPk.transitType(),
                       linkPk.startNode(),
                       linkPk.endNode());
    }

    public static ExternalId forLink(final JrLink link) {
        return forLink(link.pk());
    }

    public static ExternalId forLine(final LineId lineId) {
        return ExternalId.of(lineId.originalValue());
    }

    public static ExternalId forLine(final JrLinePk linePk) {
        return forLine(linePk.lineId());
    }

    public static ExternalId forLine(final JrLine line) {
        return forLine(line.pk());
    }

    public static ExternalId forLineHeader(final LineId lineId,
                                           final LocalDate validFrom) {
        return ExternalId.of(String.format("%s-%s",
                                           lineId.originalValue(),
                                           validFrom.format(FORMAT)));
    }

    public static ExternalId forLineHeader(final JrLineHeaderPk lineHeaderPk) {
        return forLineHeader(lineHeaderPk.lineId(),
                             lineHeaderPk.validFrom());
    }

    public static ExternalId forLineHeader(final JrLineHeader lineHeader) {
        return forLineHeader(lineHeader.pk());
    }

    public static ExternalId forRoute(final RouteId routeId) {
        return ExternalId.of(routeId.originalValue());
    }

    public static ExternalId forRoute(final JrRoutePk routePk) {
        return forRoute(routePk.routeId());
    }

    public static ExternalId forRoute(final JrRoute route) {
        return forRoute(route.pk());
    }

    public static ExternalId forRouteDirection(final RouteId routeId,
                                               final Direction direction,
                                               final LocalDate validFrom) {
        return ExternalId.of(String.format("%s-%s-%s",
                                           routeId.originalValue(),
                                           direction.value(),
                                           validFrom.format(FORMAT)));
    }

    public static ExternalId forRouteDirection(final JrRouteDirectionPk routeDirectionPk) {
        return forRouteDirection(routeDirectionPk.routeId(),
                                 routeDirectionPk.direction(),
                                 routeDirectionPk.validFrom());
    }

    public static ExternalId forRouteDirection(final JrRouteDirection routeDirection) {
        return forRouteDirection(routeDirection.pk());
    }
}
