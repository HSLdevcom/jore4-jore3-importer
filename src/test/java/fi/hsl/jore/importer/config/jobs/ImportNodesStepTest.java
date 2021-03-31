package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeRepository;
import fi.hsl.jore.importer.feature.jore.util.JoreGeometryUtil;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/source/drop_tables.sql",
        "/sql/source/populate_nodes.sql",
        "/sql/source/populate_links.sql",
        "/sql/source/populate_points.sql"
},
     config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = "/sql/destination/drop_tables.sql")
public class ImportNodesStepTest extends BatchIntegrationTest {

    @Autowired
    private INodeRepository nodeRepository;

    @Test
    public void testWhenDbEmpty() {
        assertThat(nodeRepository.empty(),
                   is(true));

        final JobExecution jobExecution = jobLauncherTestUtils.launchStep("importNodesStep");

        assertThat(jobExecution.getExitStatus().getExitCode(),
                   is("COMPLETED"));

        assertThat(nodeRepository.count(),
                   is(2));
    }

    @Test
    public void testUpdatingExistingNode() {
        assertThat(nodeRepository.empty(),
                   is(true));

        final ExternalId extId = ExternalId.of("d");
        final Point originalPoint = JoreGeometryUtil.fromDbCoordinates(61.4463974,
                                                                       23.8525307);
        final List<NodePK> nodeIds = nodeRepository.upsert(List.of(
                PersistableNode.of(extId,
                                   NodeType.CROSSROADS,
                                   originalPoint)
        ));
        final NodePK targetId = nodeIds.get(0);

        final JobExecution jobExecution = jobLauncherTestUtils.launchStep("importNodesStep");

        assertThat(jobExecution.getExitStatus().getExitCode(),
                   is("COMPLETED"));

        assertThat(nodeRepository.count(),
                   is(2));

        final Node updated = nodeRepository.findById(targetId).orElseThrow();

        assertThat(updated.externalId(),
                   is(extId));
        assertThat("Target node geometry is updated",
                   updated.location(),
                   is(JoreGeometryUtil.fromDbCoordinates(10,
                                                         10)));
        assertThat(updated.alive(),
                   is(true));

        final Option<Node> maybeOldNode = nodeRepository.findFromHistory()
                                                        .find(node -> node.externalId().equals(extId));
        assertThat("Earlier version is still accessible",
                   maybeOldNode.isDefined(),
                   is(true));
        final Node oldNode = maybeOldNode.get();

        assertThat(oldNode.location(),
                   is(originalPoint));
        assertThat("Earlier version is no longer alive",
                   oldNode.alive(),
                   is(false));
    }

}
