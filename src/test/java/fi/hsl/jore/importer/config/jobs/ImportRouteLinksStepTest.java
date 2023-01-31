package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkTestRepository;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeTestRepository;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.route_direction.dto.RouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.repository.IRouteDirectionTestRepository;
import fi.hsl.jore.importer.feature.network.route_link.dto.RouteLink;
import fi.hsl.jore.importer.feature.network.route_link.repository.IRouteLinkTestRepository;
import fi.hsl.jore.importer.feature.network.route_point.dto.RoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.repository.IRoutePointTestRepository;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.RouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.repository.IRouteStopPointTestRepository;
import io.vavr.Tuple;
import io.vavr.Tuple4;
import io.vavr.Tuple7;
import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/jore3/drop_tables.sql",
        "/sql/jore3/populate_nodes.sql",
        "/sql/jore3/populate_links.sql",
        "/sql/jore3/populate_lines.sql",
        "/sql/jore3/populate_routes.sql",
        "/sql/jore3/populate_route_directions.sql",
        "/sql/jore3/populate_route_links.sql",
        "/sql/jore3/populate_via_names.sql"
},
        config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = "/sql/importer/drop_tables.sql")
public class ImportRouteLinksStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareNodesStep",
            "importNodesStep",
            "commitNodesStep",
            "prepareLinksStep",
            "importLinksStep",
            "commitLinksStep",
            "prepareLinesStep",
            "importLinesStep",
            "commitLinesStep",
            "prepareRoutesStep",
            "importRoutesStep",
            "commitRoutesStep",
            "prepareRouteDirectionsStep",
            "importRouteDirectionsStep",
            "commitRouteDirectionsStep",
            "prepareRoutePointsStep",
            "prepareRouteStopPointsStep",
            "prepareRouteLinksStep",
            "importRouteLinksStep",
            "commitRoutePointsStep",
            "commitRouteStopPointsStep",
            "commitRouteLinksStep");

    // The external id of the route point
    // The order number of the route point
    // The external id of the node
    // and the external id of the route direction
    private static final List<Tuple4<ExternalId, Integer, ExternalId, ExternalId>> ROUTE_POINTS = List.of(
            Tuple.of(ExternalId.of("1337-c"),
                    0,
                    ExternalId.of("c"),
                    ExternalId.of("1001-1-20200603")),
            Tuple.of(ExternalId.of("1338-d"),
                    1,
                    ExternalId.of("d"),
                    ExternalId.of("1001-1-20200603")),
            Tuple.of(ExternalId.of("1339-e"),
                    2,
                    ExternalId.of("e"),
                    ExternalId.of("1001-1-20200603")),
            // Note how the last route point borrows the jore3 route link id!
            Tuple.of(ExternalId.of("1339-f"),
                    3,
                    ExternalId.of("f"),
                    ExternalId.of("1001-1-20200603"))
    );

    // The external id of the route stop point
    // The order number of the route stop point
    // The hastus point flag
    // The via point flag
    // and the timetable column (if any)
    private static final List<Tuple7<ExternalId, Integer, Boolean, Boolean, String, String, Optional<Integer>>> ROUTE_STOP_POINTS = List.of(
            Tuple.of(ExternalId.of("1337-c"),
                    0,
                    true,
                    false,
                    "Määränpää 2",
                    "Mål 2",
                    Optional.of(5)),
            Tuple.of(ExternalId.of("1339-f"),
                    1,
                    true,
                    false,
                    null,
                    null,
                    Optional.of(7))
    );

    // The external id of the route link
    // The order number of the route link
    // The external id of the infrastructure link
    // and the external id of the route direction
    private static final List<Tuple4<ExternalId, Integer, ExternalId, ExternalId>> ROUTE_LINKS = List.of(
            Tuple.of(ExternalId.of("1337"),
                    0,
                    ExternalId.of("1-c-d"),
                    ExternalId.of("1001-1-20200603")),
            Tuple.of(ExternalId.of("1338"),
                    1,
                    ExternalId.of("1-d-e"),
                    ExternalId.of("1001-1-20200603")),
            Tuple.of(ExternalId.of("1339"),
                    2,
                    ExternalId.of("1-e-f"),
                    ExternalId.of("1001-1-20200603"))
    );

    @Autowired
    private IRoutePointTestRepository routePointRepository;

    @Autowired
    private IRouteStopPointTestRepository routeStopPointTestRepository;

    @Autowired
    private IRouteLinkTestRepository routeLinkImportRepository;

    @Autowired
    private INodeTestRepository nodeRepository;

    @Autowired
    private ILinkTestRepository linkRepository;

    @Autowired
    private IRouteDirectionTestRepository routeDirectionRepository;

    @BeforeEach
    public void setup() {
        assertThat(routePointRepository.empty(),
                is(true));
        assertThat(routeStopPointTestRepository.empty(),
                is(true));
        assertThat(routeLinkImportRepository.empty(),
                is(true));
    }

    @Test
    public void whenImportingLinesToEmptyDb_thenInsertsExpectedLine() {
        runSteps(STEPS);

        assertThat(routePointRepository.count(),
                is(ROUTE_POINTS.size()));

        assertRoutePoints();

        assertThat(routeStopPointTestRepository.count(),
                is(ROUTE_STOP_POINTS.size()));

        assertRouteStopPoints();

        assertThat(routeLinkImportRepository.count(),
                is(ROUTE_LINKS.size()));

        assertRouteLinks();
    }

    private void assertRoutePoints() {
        ROUTE_POINTS.forEach(expectedRoutePointParams -> {
            final ExternalId externalId = expectedRoutePointParams._1;
            final RoutePoint routePoint = routePointRepository.findByExternalId(externalId)
                    .orElseThrow();

            assertThat(String.format("route point %s should have correct order number", externalId),
                    routePoint.orderNumber(),
                    is(expectedRoutePointParams._2));

            final Node node = nodeRepository.findById(routePoint.node())
                    .orElseThrow();

            final ExternalId nodeExternalId = expectedRoutePointParams._3;
            assertThat(String.format("route point %s should refer to node %s", externalId, nodeExternalId),
                    node.externalId(),
                    is(nodeExternalId));

            final RouteDirection routeDirection = routeDirectionRepository.findById(routePoint.routeDirection())
                    .orElseThrow();

            final ExternalId routeDirectionExternalId = expectedRoutePointParams._4;
            assertThat(String.format("route point %s should refer to route direction %s", externalId, routeDirectionExternalId),
                    routeDirection.externalId(),
                    is(routeDirectionExternalId));
        });
    }

    private void assertRouteStopPoints() {
        ROUTE_STOP_POINTS.forEach(expectedStopPointParams -> {
            final ExternalId externalId = expectedStopPointParams._1;
            final RouteStopPoint stopPoint = routeStopPointTestRepository.findByExternalId(externalId)
                    .orElseThrow();

            assertThat(String.format("stop point %s should have correct order number", externalId),
                    stopPoint.orderNumber(),
                    is(expectedStopPointParams._2));

            assertThat(String.format("stop point %s should have correct hastus point flag", externalId),
                    stopPoint.hastusStopPoint(),
                    is(expectedStopPointParams._3));

            assertThat(String.format("stop point %s should have correct via point flag", externalId),
                    stopPoint.viaPoint(),
                    is(expectedStopPointParams._4));

            if (expectedStopPointParams._5 != null) {
                final MultilingualString viaName = stopPoint.viaName().get();
                assertThat(String.format("stop point %s should have correct Finnish via name", externalId),
                        JoreLocaleUtil.getI18nString(viaName, JoreLocaleUtil.FINNISH),
                        is(expectedStopPointParams._5));

                assertThat(String.format("stop point %s should have correct Swedish via name", externalId),
                        JoreLocaleUtil.getI18nString(viaName, JoreLocaleUtil.SWEDISH),
                        is(expectedStopPointParams._6));
            }
            else {
                assertThat("Stop point should not have via name",
                        stopPoint.viaName().isPresent(),
                        is(false)
                );
            }

            assertThat(String.format("stop point %s should have correct timetable column", externalId),
                    stopPoint.timetableColumn(),
                    is(expectedStopPointParams._7));
        });
    }

    private void assertRouteLinks() {
        ROUTE_LINKS.forEach(expectedLinksParams -> {
            final ExternalId externalId = expectedLinksParams._1;
            final RouteLink routeLink = routeLinkImportRepository.findByExternalId(externalId)
                    .orElseThrow();

            assertThat(String.format("route link %s should have correct order number", externalId),
                    routeLink.orderNumber(),
                    is(expectedLinksParams._2));

            final Link link = linkRepository.findById(routeLink.link())
                    .orElseThrow();

            final ExternalId linkExternalId = expectedLinksParams._3;
            assertThat(String.format("route link %s should refer to link %s", externalId, linkExternalId),
                    link.externalId(),
                    is(linkExternalId));

            final RouteDirection routeDirection = routeDirectionRepository.findById(routeLink.routeDirection())
                    .orElseThrow();

            final ExternalId routeDirectionExternalId = expectedLinksParams._4;
            assertThat(String.format("route link %s should refer to route direction %s", externalId, routeDirectionExternalId),
                    routeDirection.externalId(),
                    is(routeDirectionExternalId));
        });
    }
}
