package fi.hsl.jore.importer.feature.batch.route_link;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.GenerativeTest;
import fi.hsl.jore.importer.feature.batch.route_link.dto.Jore3RoutePointsAndLinks;
import fi.hsl.jore.importer.feature.batch.route_link.dto.LastLinkAttributes;
import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.jore3.entity.ImmutableJrRouteLink;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.jore3.enumerated.NodeType;
import fi.hsl.jore.importer.feature.jore3.enumerated.RegulatedTimingPointStatus;
import fi.hsl.jore.importer.feature.jore3.enumerated.StopPointPurpose;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.field.generated.RouteLinkId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasOrderNumber;
import fi.hsl.jore.importer.feature.network.route_link.dto.Jore3RouteLink;
import fi.hsl.jore.importer.feature.network.route_point.dto.Jore3RoutePoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.Jore3RouteStopPoint;
import io.vavr.collection.Vector;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.quicktheories.core.Gen;

public class RouteLinksProcessorGenerativeTest extends GenerativeTest {

    private static final RouteLinksProcessor PROCESSOR = new RouteLinksProcessor();

    public Gen<NodeType> nodeTypeGen() {
        return arbitrary().enumValues(NodeType.class);
    }

    public Gen<Integer> orderNumberGen() {
        return integers().between(1, 100);
    }

    public Gen<JrRouteLink> routeLinkGen() {
        return orderNumberGen()
                .zip(
                        nodeTypeGen(),
                        booleans().all(),
                        (orderNumber, nodeType, hastusPoint) ->
                                ImmutableJrRouteLink.builder()
                                        .orderNumber(orderNumber)
                                        .startNodeType(nodeType)
                                        .hastusStopPoint(hastusPoint)
                                        // Constant fields which don't really matter (for now at
                                        // least)
                                        .routeId(RouteId.from("1001"))
                                        .direction(Direction.DIRECTION_1)
                                        .validFrom(LocalDate.of(2020, 1, 1))
                                        .routeLinkId(RouteLinkId.of(1))
                                        .startNode(NodeId.of("start"))
                                        .endNode(NodeId.of("end"))
                                        .transitType(TransitType.BUS)
                                        .stopPointPurpose(StopPointPurpose.BOARDING)
                                        .regulatedTimingPointStatus(RegulatedTimingPointStatus.NO)
                                        .includeInTimetable(true)
                                        .timetableColumn(3)
                                        .viaPoint(true)
                                        .build());
    }

    public Gen<Vector<JrRouteLink>> routeLinkListGen() {
        return lists().of(routeLinkGen())
                .ofSizeBetween(1, 100)
                .map(
                        listOfLinks ->
                                Vector.ofAll(listOfLinks)
                                        // raw list of route links is in random order and might
                                        // contain duplicate order numbers
                                        .sortBy(IHasOrderNumber::orderNumber)
                                        .distinctBy(IHasOrderNumber::orderNumber));
    }

    public Gen<LastLinkAttributes> lastLinkAttributesGen() {
        return nodeTypeGen()
                .map(
                        nodeType ->
                                LastLinkAttributes.of(
                                        nodeType,
                                        true, // is included in the timetable
                                        Optional.of(
                                                5))); // The column number of the timetable column
    }

    public Gen<RouteLinksAndAttributes> routeLinksAndAttributesGen() {
        return routeLinkListGen().zip(lastLinkAttributesGen(), RouteLinksAndAttributes::of);
    }

    @Test
    public void testProcessorGeneratively() {
        qt().withExamples(1000)
                .forAll(routeLinksAndAttributesGen())
                .checkAssert(
                        linksAndAttributes -> {
                            final Vector<JrRouteLink> inputRouteLinks =
                                    linksAndAttributes.routeLinks();

                            final Jore3RoutePointsAndLinks results =
                                    PROCESSOR.process(linksAndAttributes);

                            final Vector<Jore3RoutePoint> routePoints = results.routePoints();
                            final Vector<Jore3RouteStopPoint> stopPoints = results.stopPoints();
                            final Vector<Jore3RouteLink> routeLinks = results.routeLinks();

                            assertThat(
                                    "given N jore3 route links we get N jore4 route links",
                                    routeLinks.size(),
                                    is(inputRouteLinks.size()));

                            assertThat(
                                    "order numbers for N route links increment from 0 to N-1",
                                    routeLinks.map(rl -> rl.orderNumber()),
                                    is(Vector.range(0, routeLinks.size())));

                            assertThat(
                                    "given N jore3 route links we get N+1 route points",
                                    routePoints.size(),
                                    is(inputRouteLinks.size() + 1));

                            assertThat(
                                    "order numbers for N route points increment from 0 to N-1",
                                    routePoints.map(rp -> rp.orderNumber()),
                                    is(Vector.range(0, routePoints.size())));

                            assertThat(
                                    "for N route points we get at most N stop points",
                                    stopPoints.size() <= routePoints.size(),
                                    is(true));

                            assertThat(
                                    "order numbers for N stop points increment from 0 to N-1",
                                    stopPoints.map(sp -> sp.orderNumber()),
                                    is(Vector.range(0, stopPoints.size())));

                            // A pathological route might contain no bus stops
                            if (!stopPoints.isEmpty()) {
                                assertThat(
                                        "first stop point is always a hastus point",
                                        stopPoints.head().hastusStopPoint(),
                                        is(true));
                                assertThat(
                                        "last stop point is always a hastus point",
                                        stopPoints.last().hastusStopPoint(),
                                        is(true));
                            }
                        });
    }
}
