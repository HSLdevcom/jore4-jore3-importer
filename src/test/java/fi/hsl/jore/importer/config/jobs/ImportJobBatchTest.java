package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = JobConfig.class)
public class ImportJobBatchTest extends BatchIntegrationTest {

    @Autowired
    private ILinkRepository linkRepository;

    @Test
    @Sql(scripts = {
            "/sql/source/drop_tables.sql",
            "/sql/source/populate_nodes.sql",
            "/sql/source/populate_links.sql"
    },
         config = @SqlConfig(dataSource = "sourceDataSource"))
    @Sql(scripts = "/sql/destination/drop_tables.sql")
    public void testCompleteJob() throws Exception {

        assertThat(linkRepository.findAll().isEmpty(),
                   is(true));

        final JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getExitStatus().getExitCode(),
                   is("COMPLETED"));

        assertThat(linkRepository.findAll().size(),
                   is(1));
    }

}
