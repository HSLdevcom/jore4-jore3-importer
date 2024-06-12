package fi.hsl.jore.importer.feature.batch.route_link;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.feature.batch.route_link.dto.Jore3RoutePointsAndLinks;
import fi.hsl.jore.importer.feature.batch.route_link.dto.LastLinkAttributes;
import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.jore3.enumerated.NodeType;
import fi.hsl.jore.importer.feature.jore3.enumerated.RegulatedTimingPointStatus;
import fi.hsl.jore.importer.feature.jore3.enumerated.StopPointPurpose;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.field.generated.RouteLinkId;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImmutableJore3RoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.Jore3RoutePoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.ImmutableJore3RouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.Jore3RouteStopPoint;
import io.vavr.collection.Vector;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class RouteLinksProcessorTest {

    private static final RouteLinksProcessor PROCESSOR = new RouteLinksProcessor();

    private static final RouteId ROUTE = RouteId.from("1001");
    private static final Direction DIR = Direction.DIRECTION_1;
    private static final LocalDate VALID_FROM = LocalDate.of(2020, 1, 1);
    private static final TransitType TRANSIT = TransitType.BUS;

    private static final String VIA_NAME = "ViaNameFinnish";
    private static final String VIA_NAME_SWEDISH = "ViaNameSwedish";

    @Test
    public void givenLinkBetweenTwoBusStops_thenReturnRoutePoints_andStopPoints() throws Exception {
        // Route consists of a single link between two bus stops
        final JrRouteLink link = JrRouteLink.of(
                RouteLinkId.of(1234),
                1,
                ROUTE,
                DIR,
                VALID_FROM,
                TRANSIT,
                NodeId.of("a"),
                NodeId.of("b"),
                NodeType.BUS_STOP,
                RegulatedTimingPointStatus.NO,
                StopPointPurpose.BOARDING,
                false, // not explicitly a Hastus point
                true, // include in timetable,
                true, // via point,
                Optional.of(VIA_NAME),
                Optional.of(VIA_NAME_SWEDISH),
                Optional.of(1));
        final RouteLinksAndAttributes linksAndAttributes = RouteLinksAndAttributes.of(
                Vector.of(link), LastLinkAttributes.of(NodeType.BUS_STOP, true, Optional.of(7)));

        final Jore3RoutePointsAndLinks result = PROCESSOR.process(linksAndAttributes);

        final Vector<Jore3RoutePoint> routePoints = result.routePoints();

        assertThat(
                routePoints,
                is(Vector.of(
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(0)
                                .externalId(ExternalId.of("1234-a"))
                                .node(ExternalId.of("a"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build(),
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(1)
                                .externalId(ExternalId.of("1234-b"))
                                .node(ExternalId.of("b"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build())));

        final Vector<Jore3RouteStopPoint> stopPoints = result.stopPoints();

        assertThat(
                stopPoints,
                is(Vector.of(
                        ImmutableJore3RouteStopPoint.builder()
                                .orderNumber(0)
                                .externalId(ExternalId.of("1234-a"))
                                .hastusStopPoint(true)
                                .regulatedTimingPointStatus(RegulatedTimingPointStatus.NO)
                                .viaPoint(true)
                                .viaName(JoreLocaleUtil.createMultilingualString(VIA_NAME, VIA_NAME_SWEDISH))
                                .timetableColumn(1)
                                .build(),
                        ImmutableJore3RouteStopPoint.builder()
                                .orderNumber(1)
                                .externalId(ExternalId.of("1234-b"))
                                .hastusStopPoint(true)
                                .regulatedTimingPointStatus(RegulatedTimingPointStatus.NO)
                                .viaPoint(true)
                                .viaName(JoreLocaleUtil.createMultilingualString(VIA_NAME, VIA_NAME_SWEDISH))
                                .timetableColumn(7)
                                .build())));
    }

    @Test
    public void givenLinkBetweenABusStopAndACrossroad_thenReturnRoutePoints_andSingleStopPoint() {
        // (A pathological) route consists of a single link between a bus stop and a crossroad junction
        final JrRouteLink link = JrRouteLink.of(
                RouteLinkId.of(1234),
                1,
                ROUTE,
                DIR,
                VALID_FROM,
                TRANSIT,
                NodeId.of("a"),
                NodeId.of("b"),
                NodeType.BUS_STOP,
                RegulatedTimingPointStatus.NO,
                StopPointPurpose.BOARDING,
                false, // not explicitly a Hastus point
                true, // include in timetable
                true, // via point
                Optional.of(VIA_NAME),
                Optional.of(VIA_NAME_SWEDISH),
                Optional.of(1));
        final RouteLinksAndAttributes linksAndAttributes = RouteLinksAndAttributes.of(
                Vector.of(link), LastLinkAttributes.of(NodeType.CROSSROADS, false, Optional.empty()));

        final Jore3RoutePointsAndLinks result = PROCESSOR.process(linksAndAttributes);

        final Vector<Jore3RoutePoint> routePoints = result.routePoints();

        assertThat(
                routePoints,
                is(Vector.of(
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(0)
                                .externalId(ExternalId.of("1234-a"))
                                .node(ExternalId.of("a"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build(),
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(1)
                                .externalId(ExternalId.of("1234-b"))
                                .node(ExternalId.of("b"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build())));

        final Vector<Jore3RouteStopPoint> stopPoints = result.stopPoints();

        assertThat(
                stopPoints,
                is(Vector.of(ImmutableJore3RouteStopPoint.builder()
                        .orderNumber(0)
                        .externalId(ExternalId.of("1234-a"))
                        .hastusStopPoint(true)
                        .regulatedTimingPointStatus(RegulatedTimingPointStatus.NO)
                        .viaPoint(true)
                        .viaName(JoreLocaleUtil.createMultilingualString(VIA_NAME, VIA_NAME_SWEDISH))
                        .timetableColumn(1)
                        .build())));
    }

    @Test
    public void givenLinkWithNoBusStops_thenReturnRoutePoints_andNoStopPoints() throws Exception {
        // (A pathological) route consists of a single link between two crossroads
        final JrRouteLink link = JrRouteLink.of(
                RouteLinkId.of(1234),
                1,
                ROUTE,
                DIR,
                VALID_FROM,
                TRANSIT,
                NodeId.of("a"),
                NodeId.of("b"),
                NodeType.CROSSROADS,
                RegulatedTimingPointStatus.NO,
                StopPointPurpose.BOARDING,
                false, // not Hastus point
                false, // don't include in timetable
                true, // via point
                Optional.of(VIA_NAME),
                Optional.of(VIA_NAME_SWEDISH),
                Optional.empty());
        final RouteLinksAndAttributes linksAndAttributes = RouteLinksAndAttributes.of(
                Vector.of(link), LastLinkAttributes.of(NodeType.CROSSROADS, false, Optional.empty()));

        final Jore3RoutePointsAndLinks result = PROCESSOR.process(linksAndAttributes);

        final Vector<Jore3RoutePoint> routePoints = result.routePoints();

        assertThat(
                routePoints,
                is(Vector.of(
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(0)
                                .externalId(ExternalId.of("1234-a"))
                                .node(ExternalId.of("a"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build(),
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(1)
                                .externalId(ExternalId.of("1234-b"))
                                .node(ExternalId.of("b"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build())));

        final Vector<Jore3RouteStopPoint> stopPoints = result.stopPoints();

        assertThat("There are no bus stops in this route => no stop points", stopPoints.isEmpty(), is(true));
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
        // Node E is a bus stop as well as a regulated timing point (with load time)
        final NodeId nodeE = NodeId.of("e");
        // Node F is a crossroads junction
        final NodeId nodeF = NodeId.of("f");
        // Node G is a bus stop
        final NodeId nodeG = NodeId.of("g");

        final Vector<JrRouteLink> links = Vector.of(
                // A->B
                JrRouteLink.of(
                        RouteLinkId.of(10000),
                        1,
                        ROUTE,
                        DIR,
                        VALID_FROM,
                        TRANSIT,
                        nodeA,
                        nodeB,
                        NodeType.BUS_STOP,
                        RegulatedTimingPointStatus.NO,
                        StopPointPurpose.BOARDING,
                        true, // is a Hastus point
                        true, // include in timetable
                        true, // via point
                        Optional.of(VIA_NAME),
                        Optional.of(VIA_NAME_SWEDISH),
                        Optional.of(1)), // The column number of the timetable column
                // B->C
                JrRouteLink.of(
                        RouteLinkId.of(10001),
                        2,
                        ROUTE,
                        DIR,
                        VALID_FROM,
                        TRANSIT,
                        nodeB,
                        nodeC,
                        NodeType.CROSSROADS,
                        RegulatedTimingPointStatus.NO,
                        StopPointPurpose.UNKNOWN,
                        false, // not a Hastus point
                        false, // do not include in timetable
                        true, // via point
                        Optional.of(VIA_NAME),
                        Optional.of(VIA_NAME_SWEDISH),
                        Optional.empty()), // The column number isn't given because link isn't included in timetable
                // C->D
                JrRouteLink.of(
                        RouteLinkId.of(10002),
                        3,
                        ROUTE,
                        DIR,
                        VALID_FROM,
                        TRANSIT,
                        nodeC,
                        nodeD,
                        NodeType.CROSSROADS,
                        RegulatedTimingPointStatus.NO,
                        StopPointPurpose.UNKNOWN,
                        false, // not a Hastus point
                        false, // do not include in timetable,
                        true, // via point
                        Optional.of(VIA_NAME),
                        Optional.of(VIA_NAME_SWEDISH),
                        Optional.empty()), // The column number isn't given because link isn't included in timetable
                // D->E
                JrRouteLink.of(
                        RouteLinkId.of(10003),
                        4,
                        ROUTE,
                        DIR,
                        VALID_FROM,
                        TRANSIT,
                        nodeD,
                        nodeF,
                        NodeType.BUS_STOP_NOT_IN_USE,
                        RegulatedTimingPointStatus.NO,
                        StopPointPurpose.NOT_IN_USE,
                        false, // not a Hastus point
                        false, // do not include in timetable
                        true, // via point
                        Optional.of(VIA_NAME),
                        Optional.of(VIA_NAME_SWEDISH),
                        Optional.empty()), // The column number isn't given because link isn't included in timetable
                // E->F
                JrRouteLink.of(
                        RouteLinkId.of(10004),
                        5,
                        ROUTE,
                        DIR,
                        VALID_FROM,
                        TRANSIT,
                        nodeE,
                        nodeF,
                        NodeType.BUS_STOP,
                        RegulatedTimingPointStatus.YES_LOAD_TIME,
                        StopPointPurpose.BOARDING,
                        true, // is a Hastus point
                        true, // include in timetable
                        false, // via point
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()), // The column number isn't given because link isn't included in timetable
                // F->G
                JrRouteLink.of(
                        RouteLinkId.of(10005),
                        6,
                        ROUTE,
                        DIR,
                        VALID_FROM,
                        TRANSIT,
                        nodeF,
                        nodeG,
                        NodeType.CROSSROADS,
                        RegulatedTimingPointStatus.NO,
                        StopPointPurpose.UNKNOWN,
                        false, // not a Hastus point
                        false, // do not include in timetable
                        false, // via point,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()) // The column number isn't given because link isn't included in timetable
                );

        final LastLinkAttributes attributes = LastLinkAttributes.of(NodeType.BUS_STOP, true, Optional.of(7));

        final RouteLinksAndAttributes linksAndAttributes = RouteLinksAndAttributes.of(links, attributes);

        final Jore3RoutePointsAndLinks result = PROCESSOR.process(linksAndAttributes);

        final Vector<Jore3RoutePoint> routePoints = result.routePoints();

        assertThat(
                routePoints,
                is(Vector.of(
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(0)
                                .externalId(ExternalId.of("10000-a"))
                                .node(ExternalId.of("a"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build(),
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(1)
                                .externalId(ExternalId.of("10001-b"))
                                .node(ExternalId.of("b"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build(),
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(2)
                                .externalId(ExternalId.of("10002-c"))
                                .node(ExternalId.of("c"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build(),
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(3)
                                .externalId(ExternalId.of("10003-d"))
                                .node(ExternalId.of("d"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build(),
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(4)
                                .externalId(ExternalId.of("10004-e"))
                                .node(ExternalId.of("e"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build(),
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(5)
                                .externalId(ExternalId.of("10005-f"))
                                .node(ExternalId.of("f"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build(),
                        // Note how the last route point "borrows" the link id from the final link
                        ImmutableJore3RoutePoint.builder()
                                .orderNumber(6)
                                .externalId(ExternalId.of("10005-g"))
                                .node(ExternalId.of("g"))
                                .routeDirection(ExternalId.of("1001-1-20200101"))
                                .build())));

        final Vector<Jore3RouteStopPoint> stopPoints = result.stopPoints();

        assertThat(
                stopPoints,
                is(Vector.of(
                        ImmutableJore3RouteStopPoint.builder()
                                .orderNumber(0)
                                .externalId(ExternalId.of("10000-a"))
                                .hastusStopPoint(true)
                                .regulatedTimingPointStatus(RegulatedTimingPointStatus.NO)
                                .viaPoint(true)
                                .viaName(JoreLocaleUtil.createMultilingualString(VIA_NAME, VIA_NAME_SWEDISH))
                                .timetableColumn(1)
                                .build(),
                        // Note how node D is absent, as it's a BUS_STOP_NOT_IN_USE
                        ImmutableJore3RouteStopPoint.builder()
                                .orderNumber(1)
                                .externalId(ExternalId.of("10004-e"))
                                .hastusStopPoint(true)
                                .regulatedTimingPointStatus(RegulatedTimingPointStatus.YES_LOAD_TIME)
                                .viaPoint(false)
                                .build(),
                        ImmutableJore3RouteStopPoint.builder()
                                .orderNumber(2)
                                .externalId(ExternalId.of("10005-g"))
                                .hastusStopPoint(true)
                                .regulatedTimingPointStatus(RegulatedTimingPointStatus.NO)
                                .viaPoint(false)
                                .timetableColumn(7)
                                .build())));
    }
}
