package fi.hsl.jore.importer.config.jobs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.network.route.dto.Route;
import fi.hsl.jore.importer.feature.network.route.repository.IRouteTestRepository;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@ContextConfiguration(classes = JobConfig.class)
@Sql(
        scripts = {"/sql/jore3/drop_tables.sql", "/sql/jore3/populate_lines.sql", "/sql/jore3/populate_routes.sql"},
        config = @SqlConfig(dataSource = "sourceDataSource", transactionManager = "sourceTransactionManager"))
@Sql(scripts = "/sql/importer/drop_tables.sql")
public class ImportRoutesStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of(
            "prepareLinesStep",
            "importLinesStep",
            "commitLinesStep",
            "prepareRoutesStep",
            "importRoutesStep",
            "commitRoutesStep");

    public static final Locale FINNISH = Locale.forLanguageTag("fi-FI");

    public static final Locale SWEDISH = Locale.forLanguageTag("sv-SE");

    @Autowired
    private IRouteTestRepository routeRepository;

    @Test
    public void whenImportingLinesToEmptyDb_thenInsertsExpectedLine() {
        assertThat(routeRepository.empty(), is(true));

        runSteps(STEPS);

        assertThat(routeRepository.count(), is(1));

        final Route route = routeRepository.findAll().get(0);

        assertThat(route.alive(), is(true));
        assertThat(route.externalId(), is(ExternalId.of("1001")));
        assertThat(route.routeNumber(), is("1"));
        assertThat(route.hiddenVariant(), is(Optional.empty()));
        assertThat(route.legacyHslMunicipalityCode(), is(LegacyHslMunicipalityCode.HELSINKI));
        assertThat(
                route.name(),
                is(MultilingualString.empty()
                        .with(FINNISH, "Keskustori - Etelä-Hervanta")
                        .with(SWEDISH, "Central torget - Södra Hervanta")));
    }
}
