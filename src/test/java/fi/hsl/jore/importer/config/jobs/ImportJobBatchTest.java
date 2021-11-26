package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
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
        "/sql/source/populate_points.sql",
        "/sql/source/populate_lines.sql",
        "/sql/source/populate_line_headers.sql",
        "/sql/source/populate_routes.sql",
        "/sql/source/populate_route_directions.sql",
        "/sql/source/populate_route_links.sql",
        "/sql/source/populate_scheduled_stop_points.sql"
},
     config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = "/sql/destination/drop_tables.sql")
@Sql(
        scripts = {
                "/sql/transmodel/drop_tables.sql",
                "/sql/transmodel/populate_vehicle_modes.sql",
                "/sql/transmodel/populate_infrastructure_links.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
public class ImportJobBatchTest extends BatchIntegrationTest {

    @Test
    public void testCompleteJob() throws Exception {
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getExitStatus().getExitCode(),
                   is("COMPLETED"));
    }

}
