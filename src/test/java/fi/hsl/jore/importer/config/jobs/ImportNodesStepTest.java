package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeTestRepository;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/source/drop_tables.sql",
        "/sql/source/populate_nodes.sql"
},
     config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = "/sql/destination/drop_tables.sql")
public class ImportNodesStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareNodesStep",
                                                      "importNodesStep",
                                                      "commitNodesStep");

    @Autowired
    private INodeTestRepository nodeRepository;

    @Test
    public void givenSampleDataInSourceDatabase_andAnEmptyTargetDatabase_whenImportNodeStepsAreRun_thenNodesCAndDAreImported() {
        assertThat(nodeRepository.empty(),
                   is(true));

        runSteps(STEPS);

        assertThat(nodeRepository.count(),
                   is(2));

        final Node nodeC = nodeRepository.findByExternalId(ExternalId.of("c"))
                                         .orElseThrow();

        assertThat(nodeC.location(),
                   is(GeometryUtil.toPoint(GeometryUtil.SRID_WGS84,
                                           new Coordinate(13, 12))));
        assertThat(nodeC.nodeType(),
                   is(NodeType.CROSSROADS));

        final Node nodeD = nodeRepository.findByExternalId(ExternalId.of("d"))
                                         .orElseThrow();

        assertThat(nodeD.location(),
                   is(GeometryUtil.toPoint(GeometryUtil.SRID_WGS84,
                                           new Coordinate(17, 16))));
        assertThat(nodeD.nodeType(),
                   is(NodeType.CROSSROADS));
    }
}
