package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
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
public class ImportLinkPointsStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareLinksStep",
                                                      "importLinksStep",
                                                      "commitLinksStep",
                                                      "prepareLinkPointsStep",
                                                      "importLinkPointsStep",
                                                      "commitLinkPointsStep");

    @Autowired
    private ILinkRepository linkRepository;

    @Test
    public void givenSampleDataInSourceDatabase_andAnEmptyDatabase_whenImportLinkPointsStepsAreRun_thenLinkPointsForLinkIsImported() {
        assertThat(linkRepository.empty(),
                   is(true));

        runSteps(STEPS);

        assertThat(linkRepository.count(),
                   is(1));

        final Link link = linkRepository.findByExternalId(ExternalId.of("1-c-d")).orElseThrow();

        // FIXME: This is broken due to a bug in LinkPointReader
        assertThat(link.points().isPresent(),
                   is(false));
    }
}
