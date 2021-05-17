package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
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
public class ImportLinksStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareLinksStep",
                                                      "importLinksStep",
                                                      "commitLinksStep");

    @Autowired
    private ILinkRepository linkRepository;

    @Test
    public void testWhenDbEmpty() {
        assertThat(linkRepository.empty(),
                   is(true));

        runSteps(STEPS);

        assertThat(linkRepository.count(),
                   is(1));
    }
}
