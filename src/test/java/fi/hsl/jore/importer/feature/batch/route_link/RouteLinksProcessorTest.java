package fi.hsl.jore.importer.feature.batch.route_link;

import fi.hsl.jore.importer.feature.batch.route_link.dto.ImportableRoutePointsAndLinks;
import fi.hsl.jore.importer.feature.batch.route_link.dto.LastLinkAttributes;
import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.jore3.enumerated.NodeType;
import fi.hsl.jore.importer.feature.jore3.enumerated.StopPointPurpose;
import fi.hsl.jore.importer.feature.jore3.enumerated.TimingStopPoint;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.field.generated.RouteLinkId;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImmutableImportableRoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImportableRoutePoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.ImmutableImportableRouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.ImportableRouteStopPoint;
import io.vavr.collection.Vector;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RouteLinksProcessorTest {

    private static final RouteLinksProcessor PROCESSOR = new RouteLinksProcessor();

    private static final RouteId ROUTE = RouteId.from("1001");
    private static final Direction DIR = Direction.DIRECTION_1;
    private static final LocalDate VALID_FROM = LocalDate.of(2020, 1, 1);
    private static final TransitType TRANSIT = TransitType.BUS;

    @Test
    public void givenLinkBetweenTwoBusStops_thenReturnRoutePoints_andStopPoints() throws Exception {
        // Route consists of a single link between two bus stops
        final JrRouteLink link = JrRouteLink.of(RouteLinkId.of(1234),
                                                1,
                                                ROUTE,
                                                DIR,
                                                VALID_FROM,
                                                TRANSIT,
                                                NodeId.of("a"),
                                                NodeId.of("b"),
                                                NodeType.BUS_STOP,
                                                TimingStopPoint.NO,
                                                StopPointPurpose.BOARDING,
                                                false, // not explicitly a hastus point
                                                true, // include in timetable
                                                Optional.of(1));
        final RouteLinksAndAttributes linksAndAttributes =
                RouteLinksAndAttributes.of(Vector.of(link),
                                           LastLinkAttributes.of(NodeType.BUS_STOP,
                                                                 true,
                                                                 Optional.of(7)));

        final ImportableRoutePointsAndLinks result = PROCESSOR.process(linksAndAttributes);

        final Vector<ImportableRoutePoint> routePoints = result.routePoints();

        assertThat(routePoints,
                   is(Vector.of(ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(0)
                                                             .externalId(ExternalId.of("1234-a"))
                                                             .node(ExternalId.of("a"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build(),
                                ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(1)
                                                             .externalId(ExternalId.of("1234-b"))
                                                             .node(ExternalId.of("b"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build())));

        final Vector<ImportableRouteStopPoint> stopPoints = result.stopPoints();

        assertThat(stopPoints,
                   is(Vector.of(ImmutableImportableRouteStopPoint.builder()
                                                                 .orderNumber(0)
                                                                 .externalId(ExternalId.of("1234-a"))
                                                                 .hastusStopPoint(true)
                                                                 .timetableColumn(1)
                                                                 .build(),
                                ImmutableImportableRouteStopPoint.builder()
                                                                 .orderNumber(1)
                                                                 .externalId(ExternalId.of("1234-b"))
                                                                 .hastusStopPoint(true)
                                                                 .timetableColumn(7)
                                                                 .build())));
    }

    @Test
    public void givenLinkBetweenABusStopAndACrossroad_thenReturnRoutePoints_andSingleStopPoint() throws Exception {
        // (A pathological) route consists of a single link between a bus stop and a crossroad junction
        final JrRouteLink link = JrRouteLink.of(RouteLinkId.of(1234),
                                                1,
                                                ROUTE,
                                                DIR,
                                                VALID_FROM,
                                                TRANSIT,
                                                NodeId.of("a"),
                                                NodeId.of("b"),
                                                NodeType.BUS_STOP,
                                                TimingStopPoint.NO,
                                                StopPointPurpose.BOARDING,
                                                false, // not explicitly a hastus point
                                                true, // include in timetable
                                                Optional.of(1));
        final RouteLinksAndAttributes linksAndAttributes =
                RouteLinksAndAttributes.of(Vector.of(link),
                                           LastLinkAttributes.of(NodeType.CROSSROADS,
                                                                 false,
                                                                 Optional.empty()));

        final ImportableRoutePointsAndLinks result = PROCESSOR.process(linksAndAttributes);

        final Vector<ImportableRoutePoint> routePoints = result.routePoints();

        assertThat(routePoints,
                   is(Vector.of(ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(0)
                                                             .externalId(ExternalId.of("1234-a"))
                                                             .node(ExternalId.of("a"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build(),
                                ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(1)
                                                             .externalId(ExternalId.of("1234-b"))
                                                             .node(ExternalId.of("b"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build())));

        final Vector<ImportableRouteStopPoint> stopPoints = result.stopPoints();

        assertThat(stopPoints,
                   is(Vector.of(ImmutableImportableRouteStopPoint.builder()
                                                                 .orderNumber(0)
                                                                 .externalId(ExternalId.of("1234-a"))
                                                                 .hastusStopPoint(true)
                                                                 .timetableColumn(1)
                                                                 .build())));
    }

    @Test
    public void givenLinkWithNoBusStops_thenReturnRoutePoints_andNoStopPoints() throws Exception {
        // (A pathological) route consists of a single link between two crossroads
        final JrRouteLink link = JrRouteLink.of(RouteLinkId.of(1234),
                                                1,
                                                ROUTE,
                                                DIR,
                                                VALID_FROM,
                                                TRANSIT,
                                                NodeId.of("a"),
                                                NodeId.of("b"),
                                                NodeType.CROSSROADS,
                                                TimingStopPoint.NO,
                                                StopPointPurpose.BOARDING,
                                                false,
                                                false,
                                                Optional.empty());
        final RouteLinksAndAttributes linksAndAttributes =
                RouteLinksAndAttributes.of(Vector.of(link),
                                           LastLinkAttributes.of(NodeType.CROSSROADS,
                                                                 false,
                                                                 Optional.empty()));

        final ImportableRoutePointsAndLinks result = PROCESSOR.process(linksAndAttributes);

        final Vector<ImportableRoutePoint> routePoints = result.routePoints();

        assertThat(routePoints,
                   is(Vector.of(ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(0)
                                                             .externalId(ExternalId.of("1234-a"))
                                                             .node(ExternalId.of("a"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build(),
                                ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(1)
                                                             .externalId(ExternalId.of("1234-b"))
                                                             .node(ExternalId.of("b"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build())));

        final Vector<ImportableRouteStopPoint> stopPoints = result.stopPoints();

        assertThat("There are no bus stops in this route => no stop points",
                   stopPoints.isEmpty(),
                   is(true));
    }

    @Test
    public void givenComplexRoute_thenReturnRoutePoints_andStopPoints() throws Exception {
        // Node A is a bus stop
        final NodeId nodeA = NodeId.of("a");
        // Node B is a crossroads junction
        final NodeId nodeB = NodeId.of("b");
        // Node C is a crossroads junction
        final NodeId nodeC = NodeId.of("c");
        // Node D is a bus stop, but not in use
        final NodeId nodeD = NodeId.of("d");
        // Node E is a crossroads junction
        final NodeId nodeE = NodeId.of("e");
        // Node F is a bus stop
        final NodeId nodeF = NodeId.of("f");

        final Vector<JrRouteLink> links = Vector.of(
                // A->B
                JrRouteLink.of(RouteLinkId.of(10000),
                               1,
                               ROUTE,
                               DIR,
                               VALID_FROM,
                               TRANSIT,
                               nodeA,
                               nodeB,
                               NodeType.BUS_STOP,
                               TimingStopPoint.NO,
                               StopPointPurpose.BOARDING,
                               true, // is a hastus point
                               true, // include in timetable
                               Optional.of(1)), //The column number of the timetable column
                // B->C
                JrRouteLink.of(RouteLinkId.of(10001),
                               2,
                               ROUTE,
                               DIR,
                               VALID_FROM,
                               TRANSIT,
                               nodeB,
                               nodeC,
                               NodeType.CROSSROADS,
                               TimingStopPoint.NO,
                               StopPointPurpose.UNKNOWN,
                               false, // not a hastus point
                               false, // do not include in timetable
                               Optional.empty()), // The column number isn't given because link isn't included in timetable
                // C->D
                JrRouteLink.of(RouteLinkId.of(10002),
                               3,
                               ROUTE,
                               DIR,
                               VALID_FROM,
                               TRANSIT,
                               nodeC,
                               nodeD,
                               NodeType.CROSSROADS,
                               TimingStopPoint.NO,
                               StopPointPurpose.UNKNOWN,
                               false, // not a hastus point
                               false, // do not include in timetable
                               Optional.empty()), // The column number isn't given because link isn't included in timetable
                // D->E
                JrRouteLink.of(RouteLinkId.of(10003),
                               4,
                               ROUTE,
                               DIR,
                               VALID_FROM,
                               TRANSIT,
                               nodeD,
                               nodeE,
                               NodeType.BUS_STOP_NOT_IN_USE,
                               TimingStopPoint.NO,
                               StopPointPurpose.NOT_IN_USE,
                               false, // not a hastus point
                               false, // do not include in timetable
                               Optional.empty()), // The column number isn't given because link isn't included in timetable
                // E->F
                JrRouteLink.of(RouteLinkId.of(10004),
                               5,
                               ROUTE,
                               DIR,
                               VALID_FROM,
                               TRANSIT,
                               nodeE,
                               nodeF,
                               NodeType.CROSSROADS,
                               TimingStopPoint.NO,
                               StopPointPurpose.UNKNOWN,
                               false, // not a hastus point
                               false, // do not include in timetable
                               Optional.empty()) // The column number isn't given because link isn't included in timetable
        );

        final LastLinkAttributes attributes = LastLinkAttributes.of(NodeType.BUS_STOP,
                                                                    true,
                                                                    Optional.of(7));

        final RouteLinksAndAttributes linksAndAttributes = RouteLinksAndAttributes.of(links,
                                                                                      attributes);

        final ImportableRoutePointsAndLinks result = PROCESSOR.process(linksAndAttributes);

        final Vector<ImportableRoutePoint> routePoints = result.routePoints();

        assertThat(routePoints,
                   is(Vector.of(ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(0)
                                                             .externalId(ExternalId.of("10000-a"))
                                                             .node(ExternalId.of("a"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build(),
                                ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(1)
                                                             .externalId(ExternalId.of("10001-b"))
                                                             .node(ExternalId.of("b"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build(),
                                ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(2)
                                                             .externalId(ExternalId.of("10002-c"))
                                                             .node(ExternalId.of("c"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build(),
                                ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(3)
                                                             .externalId(ExternalId.of("10003-d"))
                                                             .node(ExternalId.of("d"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build(),
                                ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(4)
                                                             .externalId(ExternalId.of("10004-e"))
                                                             .node(ExternalId.of("e"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build(),
                                // Note how the last route point "borrows" the link id from the final link
                                ImmutableImportableRoutePoint.builder()
                                                             .orderNumber(5)
                                                             .externalId(ExternalId.of("10004-f"))
                                                             .node(ExternalId.of("f"))
                                                             .routeDirection(ExternalId.of("1001-1-20200101"))
                                                             .build()
                   )));

        final Vector<ImportableRouteStopPoint> stopPoints = result.stopPoints();

        assertThat(stopPoints,
                   is(Vector.of(ImmutableImportableRouteStopPoint.builder()
                                                                 .orderNumber(0)
                                                                 .externalId(ExternalId.of("10000-a"))
                                                                 .hastusStopPoint(true)
                                                                 .timetableColumn(1)
                                                                 .build(),
                                // Note how node D is absent, as it's a BUS_STOP_NOT_IN_USE
                                ImmutableImportableRouteStopPoint.builder()
                                                                 .orderNumber(1)
                                                                 .externalId(ExternalId.of("10004-f"))
                                                                 .hastusStopPoint(true)
                                                                 .timetableColumn(7)
                                                                 .build()
                   )));
    }
}
