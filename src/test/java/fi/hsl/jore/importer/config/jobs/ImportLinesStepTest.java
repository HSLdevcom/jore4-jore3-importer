package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.network.line.dto.Line;
import fi.hsl.jore.importer.feature.network.line.repository.ILineTestRepository;
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
        "/sql/source/populate_lines.sql"
},
     config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = "/sql/destination/drop_tables.sql")
public class ImportLinesStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareLinesStep",
                                                      "importLinesStep",
                                                      "commitLinesStep");

    @Autowired
    private ILineTestRepository lineRepository;

    @Test
    public void whenImportingLinesToEmptyDb_thenInsertsExpectedLine() {
        assertThat(lineRepository.empty(),
                   is(true));

        runSteps(STEPS);

        assertThat(lineRepository.count(),
                   is(1));

        final Line line = lineRepository.findAll().get(0);

        assertThat(line.alive(),
                   is(true));
        assertThat(line.externalId(),
                   is(ExternalId.of("1001")));
        assertThat(line.lineNumber(),
                   is("1"));
        assertThat(line.networkType(),
                   is(NetworkType.ROAD));
    }
}
