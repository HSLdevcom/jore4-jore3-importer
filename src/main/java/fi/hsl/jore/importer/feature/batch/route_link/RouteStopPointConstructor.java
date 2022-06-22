package fi.hsl.jore.importer.feature.batch.route_link;

import fi.hsl.jore.importer.feature.batch.route_link.dto.LastLinkAttributes;
import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.jore3.enumerated.NodeType;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.ImportableRouteStopPoint;
import io.vavr.collection.Vector;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.function.Function;

public final class RouteStopPointConstructor {

    private RouteStopPointConstructor() {
    }

    private static Optional<Integer> timetableColumn(final boolean includeInTimetable,
                                                     final Optional<Integer> timetableColumn) {
        return includeInTimetable ?
                timetableColumn :
                Optional.empty();
    }

    private static ImportableRouteStopPoint fromLink(final JrRouteLink link,
                                                     final int index) {
        return ImportableRouteStopPoint.of(ExternalIdUtil.forRouteLinkStartNode(link),
                                           index,
                                           link.hastusStopPoint(),
                                           link.viaPoint(),
                                           buildViaName(link),
                                           timetableColumn(link.includeInTimetable(),
                                                           link.timetableColumn()));
    }

    private static ImportableRouteStopPoint fromLastLink(final JrRouteLink link,
                                                         final int index,
                                                         final LastLinkAttributes attributes) {
        // Final point is always a hastus point
        final boolean hastusPoint = true;
        return ImportableRouteStopPoint.of(ExternalIdUtil.forRouteLinkEndNode(link),
                                           index,
                                           hastusPoint,
                                           link.viaPoint(),
                                           buildViaName(link),
                                           timetableColumn(attributes.includeInTimetable(),
                                                           attributes.timetableColumn()));
    }

    private static Optional<MultilingualString> buildViaName(final JrRouteLink link) {
        if (link.viaName().isPresent() || link.viaNameSwedish().isPresent()) {
            return Optional.of(
                    JoreLocaleUtil.createMultilingualString(
                            link.viaName().orElse(""),
                            link.viaNameSwedish().orElse("")
                    )
            );
        }

        return Optional.empty();
    }

    private static final Function<StopPointContext, StopPointContext> ADD_STOP_POINTS_FROM_STARTING_NODES =
            ctx -> {
                final Vector<ImportableRouteStopPoint> points = ctx.linksAndAttributes()
                                                                   .routeLinks()
                                                                   .filter(link -> link.startNodeType() == NodeType.BUS_STOP)
                                                                   .zipWithIndex()
                                                                   .map(indexAndLink -> fromLink(indexAndLink._1, indexAndLink._2));
                return ctx.withStopPoints(ctx.stopPoints()
                                             .appendAll(points));
            };

    private static final Function<StopPointContext, StopPointContext> ADD_STOP_POINT_FROM_LAST_LINK =
            ctx -> {
                final LastLinkAttributes attributes = ctx.linksAndAttributes().lastLinkAttributes();

                if (attributes.nodeType() == NodeType.BUS_STOP) {
                    final int lastIndex = ctx.stopPoints().size();
                    return ctx
                            .withStopPoints(ctx.stopPoints()
                                               .append(fromLastLink(ctx.linksAndAttributes().routeLinks().last(),
                                                                    lastIndex,
                                                                    attributes)));
                } else {
                    // Special case: The last route point is not a bus stop
                    return ctx;
                }
            };

    private static final Function<StopPointContext, StopPointContext> MARK_FIRST_STOP_POINT_AS_HASTUS_POINT =
            ctx -> ctx.updateHead(sp -> sp.withHastusStopPoint(true));


    private static final Function<StopPointContext, StopPointContext> MARK_LAST_STOP_POINT_AS_HASTUS_POINT =
            ctx -> ctx.updateTail(sp -> sp.withHastusStopPoint(true));

    private static final Function<StopPointContext, StopPointContext> PIPELINE =
            ADD_STOP_POINTS_FROM_STARTING_NODES
                    .andThen(ADD_STOP_POINT_FROM_LAST_LINK)
                    .andThen(MARK_FIRST_STOP_POINT_AS_HASTUS_POINT)
                    .andThen(MARK_LAST_STOP_POINT_AS_HASTUS_POINT);

    public static Vector<ImportableRouteStopPoint> extractStopPoints(final RouteLinksAndAttributes entity) {
        final StopPointContext ctx = StopPointContext.of(entity);
        return PIPELINE.apply(ctx)
                       .stopPoints();
    }

    @Value.Immutable
    public interface StopPointContext {
        RouteLinksAndAttributes linksAndAttributes();

        @Value.Default
        default Vector<ImportableRouteStopPoint> stopPoints() {
            return Vector.empty();
        }

        /**
         * A helper method for updating the head of the stop point vector
         *
         * @param f The function to apply to the head
         * @return A new instance of the context
         */
        default StopPointContext updateHead(final Function<ImportableRouteStopPoint, ImportableRouteStopPoint> f) {
            if (stopPoints().isEmpty()) {
                return this;
            }
            return withStopPoints(stopPoints().update(0, f));
        }

        /**
         * A helper method for updating the tail of the stop point vector
         *
         * @param f The function to apply to the tail
         * @return A new instance of the context
         */
        default StopPointContext updateTail(final Function<ImportableRouteStopPoint, ImportableRouteStopPoint> f) {
            if (stopPoints().isEmpty()) {
                return this;
            }
            final int idx = stopPoints().size() - 1;
            return withStopPoints(stopPoints().update(idx, f));
        }

        StopPointContext withStopPoints(Vector<ImportableRouteStopPoint> stopPoints);

        static StopPointContext of(final RouteLinksAndAttributes linksAndAttributes) {
            return ImmutableStopPointContext.builder()
                                            .linksAndAttributes(linksAndAttributes)
                                            .build();
        }
    }
}
