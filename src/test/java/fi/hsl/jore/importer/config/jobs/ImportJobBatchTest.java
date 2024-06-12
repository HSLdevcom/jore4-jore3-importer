package fi.hsl.jore.importer.config.jobs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.BatchIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@ContextConfiguration(classes = JobConfig.class)
@Sql(
        scripts = {
            "/sql/jore3/drop_tables.sql",
            "/sql/jore3/populate_nodes.sql",
            "/sql/jore3/populate_links.sql",
            "/sql/jore3/populate_points.sql",
            "/sql/jore3/populate_lines.sql",
            "/sql/jore3/populate_line_headers.sql",
            "/sql/jore3/populate_routes.sql",
            "/sql/jore3/populate_route_directions.sql",
            "/sql/jore3/populate_route_links.sql",
            "/sql/jore3/populate_via_names.sql",
            "/sql/jore3/populate_places.sql",
            "/sql/jore3/populate_scheduled_stop_points.sql"
        },
        config = @SqlConfig(dataSource = "sourceDataSource", transactionManager = "sourceTransactionManager"))
@Sql(scripts = "/sql/importer/drop_tables.sql")
@Sql(
        scripts = {"/sql/jore4/drop_tables.sql", "/sql/jore4/populate_infrastructure_links.sql"},
        config = @SqlConfig(dataSource = "jore4DataSource", transactionManager = "jore4TransactionManager"))
public class ImportJobBatchTest extends BatchIntegrationTest {

    @Test
    public void testCompleteJob() throws Exception {
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getExitStatus().getExitCode(), is("COMPLETED"));
    }
}
