package fi.hsl.jore.importer.feature.batch.route_link;

import static fi.hsl.jore.importer.util.JoreCollectionUtils.concatToList;
import static fi.hsl.jore.importer.util.JoreCollectionUtils.mapWithIndex;
import static fi.hsl.jore.importer.util.JoreCollectionUtils.updateList;

import fi.hsl.jore.importer.feature.batch.route_link.dto.LastLinkAttributes;
import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.jore3.enumerated.NodeType;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.Jore3RouteStopPoint;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.immutables.value.Value;

public final class RouteStopPointConstructor {

    private RouteStopPointConstructor() {}

    private static Optional<Integer> timetableColumn(
            final boolean includeInTimetable, final Optional<Integer> timetableColumn) {

        return includeInTimetable ? timetableColumn : Optional.empty();
    }

    private static Jore3RouteStopPoint fromLink(final JrRouteLink link, final int index) {
        return Jore3RouteStopPoint.of(
                ExternalIdUtil.forRouteLinkStartNode(link),
                index,
                link.hastusStopPoint(),
                link.regulatedTimingPointStatus(),
                link.viaPoint(),
                buildViaName(link),
                timetableColumn(link.includeInTimetable(), link.timetableColumn()));
    }

    private static Jore3RouteStopPoint fromLastLink(
            final JrRouteLink link, final int index, final LastLinkAttributes attributes) {
        // Final point is always a Hastus point.
        final boolean hastusPoint = true;

        return Jore3RouteStopPoint.of(
                ExternalIdUtil.forRouteLinkEndNode(link),
                index,
                hastusPoint,
                link.regulatedTimingPointStatus(),
                link.viaPoint(),
                buildViaName(link),
                timetableColumn(attributes.includeInTimetable(), attributes.timetableColumn()));
    }

    private static Optional<MultilingualString> buildViaName(final JrRouteLink link) {
        if (link.viaName().isPresent() || link.viaNameSwedish().isPresent()) {
            return Optional.of(JoreLocaleUtil.createMultilingualString(
                    link.viaName().orElse(""), link.viaNameSwedish().orElse("")));
        }

        return Optional.empty();
    }

    private static final Function<StopPointContext, StopPointContext> ADD_STOP_POINTS_FROM_STARTING_NODES = ctx -> {
        final List<JrRouteLink> routeLinks = ctx.linksAndAttributes().routeLinks().stream()
                .filter(link -> link.startNodeType() == NodeType.BUS_STOP)
                .toList();
        final List<Jore3RouteStopPoint> points =
                mapWithIndex(routeLinks, (index, link) -> fromLink(link, index)).toList();
        return ctx.withStopPoints(concatToList(ctx.stopPoints(), points));
    };

    private static final Function<StopPointContext, StopPointContext> ADD_STOP_POINT_FROM_LAST_LINK = ctx -> {
        final LastLinkAttributes attributes = ctx.linksAndAttributes().lastLinkAttributes();

        if (attributes.nodeType() == NodeType.BUS_STOP) {
            final List<JrRouteLink> links = ctx.linksAndAttributes().routeLinks();
            final int lastIndex = ctx.stopPoints().size();

            final List<Jore3RouteStopPoint> newPoints = concatToList(
                    ctx.stopPoints(), List.of(fromLastLink(links.get(links.size() - 1), lastIndex, attributes)));
            return ctx.withStopPoints(newPoints);
        } else {
            // Special case: The last route point is not a stop point
            return ctx;
        }
    };

    private static final Function<StopPointContext, StopPointContext> MARK_FIRST_STOP_POINT_AS_HASTUS_POINT =
            ctx -> ctx.updateHead(sp -> sp.withHastusStopPoint(true));

    private static final Function<StopPointContext, StopPointContext> MARK_LAST_STOP_POINT_AS_HASTUS_POINT =
            ctx -> ctx.updateTail(sp -> sp.withHastusStopPoint(true));

    private static final Function<StopPointContext, StopPointContext> PIPELINE = ADD_STOP_POINTS_FROM_STARTING_NODES
            .andThen(ADD_STOP_POINT_FROM_LAST_LINK)
            .andThen(MARK_FIRST_STOP_POINT_AS_HASTUS_POINT)
            .andThen(MARK_LAST_STOP_POINT_AS_HASTUS_POINT);

    public static List<Jore3RouteStopPoint> extractStopPoints(final RouteLinksAndAttributes entity) {
        final StopPointContext ctx = StopPointContext.of(entity);
        return PIPELINE.apply(ctx).stopPoints();
    }

    @Value.Immutable
    public interface StopPointContext {
        RouteLinksAndAttributes linksAndAttributes();

        @Value.Default
        default List<Jore3RouteStopPoint> stopPoints() {
            return Collections.emptyList();
        }

        /**
         * A helper method for updating the head of the stop point list
         *
         * @param f The function to apply to the head
         * @return A new instance of the context
         */
        default StopPointContext updateHead(final Function<Jore3RouteStopPoint, Jore3RouteStopPoint> f) {
            final List<Jore3RouteStopPoint> stopPoints = stopPoints();

            if (stopPoints().isEmpty()) {
                return this;
            }

            return withStopPoints(updateList(stopPoints, 0, f.apply(stopPoints.get(0))));
        }

        /**
         * A helper method for updating the tail of the stop point list
         *
         * @param f The function to apply to the tail
         * @return A new instance of the context
         */
        default StopPointContext updateTail(final Function<Jore3RouteStopPoint, Jore3RouteStopPoint> f) {
            final List<Jore3RouteStopPoint> stopPoints = stopPoints();

            if (stopPoints().isEmpty()) {
                return this;
            }

            final int idx = stopPoints.size() - 1;
            return withStopPoints(updateList(stopPoints, idx, f.apply(stopPoints.get(idx))));
        }

        StopPointContext withStopPoints(Iterable<? extends Jore3RouteStopPoint> stopPoints);

        static StopPointContext of(final RouteLinksAndAttributes linksAndAttributes) {
            return ImmutableStopPointContext.builder()
                    .linksAndAttributes(linksAndAttributes)
                    .build();
        }
    }
}
