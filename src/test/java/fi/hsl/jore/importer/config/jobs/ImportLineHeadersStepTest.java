package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.line_header.dto.LineHeader;
import fi.hsl.jore.importer.feature.network.line_header.repository.ILineHeaderTestRepository;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.time.LocalDate;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/jore3/drop_tables.sql",
        "/sql/jore3/populate_lines.sql",
        "/sql/jore3/populate_line_headers.sql"
},
     config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = "/sql/importer/drop_tables.sql")
public class ImportLineHeadersStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareLinesStep",
                                                      "importLinesStep",
                                                      "commitLinesStep",
                                                      "prepareLineHeadersStep",
                                                      "importLineHeadersStep",
                                                      "commitLineHeadersStep");

    public static final Locale FINNISH = Locale.forLanguageTag("fi-FI");

    public static final Locale SWEDISH = Locale.forLanguageTag("sv-SE");

    @Autowired
    private ILineHeaderTestRepository lineHeaderRepository;

    @Test
    public void whenImportingLinesToEmptyDb_thenInsertsExpectedLine() {
        assertThat(lineHeaderRepository.empty(),
                   is(true));

        runSteps(STEPS);

        assertThat(lineHeaderRepository.count(),
                   is(1));

        final LineHeader header = lineHeaderRepository.findAll().get(0);

        assertThat(header.alive(),
                   is(true));
        assertThat(header.externalId(),
                   is(ExternalId.of("1001-20200603")));
        assertThat(header.name(),
                   is(MultilingualString.empty()
                                        .with(FINNISH, "Keskustori - Etelä-Hervanta")
                                        .with(SWEDISH, "Central torget - Södra Hervanta")));
        assertThat(header.nameShort(),
                   is(MultilingualString.empty()
                                        .with(FINNISH, "Ktori-EteläHervanta")
                                        .with(SWEDISH, "CentralT-S.Hervanta")));
        assertThat(header.origin1(),
                   is(MultilingualString.empty()
                                        .with(FINNISH, "Keskustori")
                                        .with(SWEDISH, "Central torget")));
        assertThat(header.origin2(),
                   is(MultilingualString.empty()
                                        .with(FINNISH, "Etelä-Hervanta")
                                        .with(SWEDISH, "Södra Hervanta")));
        assertThat(header.validTime(),
                   is(DateRange.between(LocalDate.of(2020, 6, 3),
                                        LocalDate.of(2021, 7, 5))));
        assertThat(header.jore4IdOfLine().isEmpty(),
                is(true));
    }
}
