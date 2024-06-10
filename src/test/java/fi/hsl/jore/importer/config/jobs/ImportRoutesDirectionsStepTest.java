package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.network.route_direction.dto.RouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.repository.IRouteDirectionTestRepository;
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
        "/sql/jore3/populate_routes.sql",
        "/sql/jore3/populate_route_directions.sql"
},
     config = @SqlConfig(dataSource = "sourceDataSource", transactionManager = "sourceTransactionManager"))
@Sql(scripts = "/sql/importer/drop_tables.sql")
public class ImportRoutesDirectionsStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareLinesStep",
                                                      "importLinesStep",
                                                      "commitLinesStep",
                                                      "prepareRoutesStep",
                                                      "importRoutesStep",
                                                      "commitRoutesStep",
                                                      "prepareRouteDirectionsStep",
                                                      "importRouteDirectionsStep",
                                                      "commitRouteDirectionsStep");

    public static final Locale FINNISH = Locale.forLanguageTag("fi-FI");

    public static final Locale SWEDISH = Locale.forLanguageTag("sv-SE");

    @Autowired
    private IRouteDirectionTestRepository routeDirectionRepository;

    @Test
    public void whenImportingLinesToEmptyDb_thenInsertsExpectedLine() {
        assertThat(routeDirectionRepository.empty(),
                   is(true));

        runSteps(STEPS);

        assertThat(routeDirectionRepository.count(),
                   is(1));

        final RouteDirection routeDirection = routeDirectionRepository.findAll().get(0);

        assertThat(routeDirection.alive(),
                   is(true));
        assertThat(routeDirection.externalId(),
                   is(ExternalId.of("1001-1-20200603")));
        assertThat(routeDirection.direction(),
                   is(DirectionType.OUTBOUND));
        assertThat(routeDirection.name(),
                   is(MultilingualString.empty()
                                        .with(FINNISH, "Keskustori - Etelä-Hervanta")
                                        .with(SWEDISH, "Central torget - Södra Hervanta")));
        assertThat(routeDirection.nameShort(),
                   is(MultilingualString.empty()
                                        .with(FINNISH, "Ktori-EteläHervanta")
                                        .with(SWEDISH, "CentralT-S.Hervanta")));
        assertThat(routeDirection.origin(),
                   is(MultilingualString.empty()
                                        .with(FINNISH, "Keskustori")
                                        .with(SWEDISH, "Central torget")));
        assertThat(routeDirection.destination(),
                   is(MultilingualString.empty()
                                        .with(FINNISH, "Etelä-Hervanta")
                                        .with(SWEDISH, "Södra Hervanta")));
        assertThat(routeDirection.lengthMeters().orElseThrow(),
                   is(100));
        assertThat(routeDirection.validTime(),
                   is(DateRange.between(LocalDate.of(2020, 6, 3),
                                        LocalDate.of(2021, 7, 5))));
    }
}
