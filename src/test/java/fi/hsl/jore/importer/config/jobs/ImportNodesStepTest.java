package fi.hsl.jore.importer.config.jobs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeTestRepository;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.Tuple;
import io.vavr.Tuple4;
import io.vavr.collection.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@ContextConfiguration(classes = JobConfig.class)
@Sql(
        scripts = {"/sql/jore3/drop_tables.sql", "/sql/jore3/populate_nodes.sql"},
        config = @SqlConfig(dataSource = "sourceDataSource", transactionManager = "sourceTransactionManager"))
@Sql(scripts = "/sql/importer/drop_tables.sql")
public class ImportNodesStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareNodesStep", "importNodesStep", "commitNodesStep");

    // External ID of the node,
    // The node location
    // The node projected location,
    // and the node type
    private static final List<Tuple4<ExternalId, Point, Optional<Point>, NodeType>> NODES = List.of(
            Tuple.of(
                    ExternalId.of("c"),
                    GeometryUtil.toPoint(GeometryUtil.SRID_WGS84, new Coordinate(6, 5)),
                    Optional.of(GeometryUtil.toPoint(GeometryUtil.SRID_WGS84, new Coordinate(13, 12))),
                    NodeType.STOP),
            Tuple.of(
                    ExternalId.of("d"),
                    GeometryUtil.toPoint(GeometryUtil.SRID_WGS84, new Coordinate(17, 16)),
                    Optional.empty(),
                    NodeType.CROSSROADS),
            Tuple.of(
                    ExternalId.of("e"),
                    GeometryUtil.toPoint(GeometryUtil.SRID_WGS84, new Coordinate(19, 18)),
                    Optional.empty(),
                    NodeType.CROSSROADS),
            Tuple.of(
                    ExternalId.of("f"),
                    GeometryUtil.toPoint(GeometryUtil.SRID_WGS84, new Coordinate(8, 7)),
                    Optional.of(GeometryUtil.toPoint(GeometryUtil.SRID_WGS84, new Coordinate(21, 20))),
                    NodeType.STOP));

    @Autowired
    private INodeTestRepository nodeRepository;

    @BeforeEach
    public void setup() {
        assertThat(nodeRepository.empty(), is(true));
    }

    @Test
    public void
            givenSampleDataInSourceDatabase_andAnEmptyTargetDatabase_whenImportNodeStepsAreRun_thenNodesCAndDAndEAndFAreImported() {
        runSteps(STEPS);

        assertThat(nodeRepository.count(), is(NODES.size()));

        NODES.forEach(expectedNodeParams -> {
            final ExternalId externalId = expectedNodeParams._1;
            final Node node = nodeRepository.findByExternalId(externalId).orElseThrow();

            assertThat(
                    String.format("node %s should have correct location", externalId),
                    node.location(),
                    is(expectedNodeParams._2));
            // This asserts that:
            // 1. A node of type NodeType.CROSSROADS doesn't have a projected location.
            // 2. A node of type NodeType.STOP must have a projected location
            // See the NODES constant for more details
            assertThat(
                    String.format("node %s should have correct projected location", externalId),
                    node.projectedLocation(),
                    is(expectedNodeParams._3));
            assertThat(
                    String.format("node %s should have correct node type", externalId),
                    node.nodeType(),
                    is(expectedNodeParams._4));
        });
    }
}
