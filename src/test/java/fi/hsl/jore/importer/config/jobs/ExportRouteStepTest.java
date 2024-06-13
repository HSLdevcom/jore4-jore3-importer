package fi.hsl.jore.importer.config.jobs;

import static fi.hsl.jore.importer.TestJsonUtil.equalJson;
import static fi.hsl.jore.jore4.jooq.route.Tables.ROUTE_;
import static org.assertj.db.api.Assertions.assertThat;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteDirection;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.repository.Jore4ValidityPeriodTestRepository;
import fi.hsl.jore.importer.feature.jore4.repository.ValidityPeriodTargetTable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@ContextConfiguration(classes = JobConfig.class)
@Sql(
        scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_infrastructure_nodes.sql",
            "/sql/importer/populate_lines.sql",
            "/sql/importer/populate_line_headers_with_jore4_ids.sql",
            "/sql/importer/populate_routes.sql",
            "/sql/importer/populate_route_directions.sql",
            "/sql/importer/populate_route_points_for_jore4_export.sql",
            "/sql/importer/populate_route_stop_points_for_jore4_export.sql",
            "/sql/importer/populate_places.sql",
            "/sql/importer/populate_scheduled_stop_points_for_jore4_export.sql"
        })
@Sql(
        scripts = {
            "/sql/jore4/drop_tables.sql",
            "/sql/jore4/populate_infrastructure_links.sql",
            "/sql/jore4/populate_timing_places.sql",
            "/sql/jore4/populate_scheduled_stop_points.sql",
            "/sql/jore4/populate_lines.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource", transactionManager = "jore4TransactionManager"))
public class ExportRouteStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("exportRoutesStep");

    private final String EXPECTED_DIRECTION = Jore4RouteDirection.INBOUND.getValue();
    private final UUID EXPECTED_JORE4_ID_OF_LINE = UUID.fromString("5aa7d9fc-2cf9-466d-8ac0-f442d60c261f");
    private static final String EXPECTED_LABEL = "1";
    private static final String EXPECTED_DESCRIPTION =
            "{\"fi_FI\":\"Keskustori - Kaleva - Etelä-Hervanta vanha\",\"sv_SE\":\"Central torget - Kaleva - Södra Hervanta gamla\"}";
    private static final int EXPECTED_PRIORITY = 10;
    private static final LegacyHslMunicipalityCode EXPECTED_LEGACY_HSL_MUNICIPALITY_CODE =
            LegacyHslMunicipalityCode.HELSINKI;

    private static final LocalDate VALIDITY_PERIOD_START = LocalDate.of(2021, 1, 1);
    // validity period end is specified with an open upper boundary
    private static final LocalDate VALIDITY_PERIOD_END =
            LocalDate.of(2022, 1, 1).minusDays(1);

    private static final fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections IMPORTER_ROUTE_DIRECTION =
            fi.hsl.jore.importer.jooq.network.Tables.NETWORK_ROUTE_DIRECTIONS;
    private static final fi.hsl.jore.jore4.jooq.route.tables.Route JORE4_ROUTE =
            fi.hsl.jore.jore4.jooq.route.Tables.ROUTE_;

    private final Table importerTargetTable;
    private final Table jore4TargetTable;
    private final Jore4ValidityPeriodTestRepository testRepository;

    @Autowired
    public ExportRouteStepTest(
            final @Qualifier("importerDataSource") DataSource importerDataSource,
            final @Qualifier("jore4DataSource") DataSource jore4DataSource) {
        this.importerTargetTable = new Table(importerDataSource, "network.network_route_directions");
        this.jore4TargetTable = new Table(jore4DataSource, "route.route");
        this.testRepository = new Jore4ValidityPeriodTestRepository(jore4DataSource, ValidityPeriodTargetTable.ROUTE);
    }

    @Test
    @DisplayName("Should insert one line into the Jore 4 database")
    void shouldInsertOneLineIntoJoreDatabase() {
        runSteps(STEPS);

        assertThat(jore4TargetTable).hasNumberOfRows(1);
    }

    @Test
    @DisplayName("Should generate a new id for the exported line")
    void shouldGenerateNewIdForExportedLine() {
        runSteps(STEPS);

        assertThat(jore4TargetTable).row().value(JORE4_ROUTE.ROUTE_ID.getName()).isNotNull();
    }

    @Test
    @DisplayName("Should save the exported route with the correct description")
    void shouldSaveExportLineWithCorrectDescription() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.DESCRIPTION_I18N.getName())
                .is(equalJson(EXPECTED_DESCRIPTION));
    }

    @Test
    @DisplayName("Should save the exported route with the correct direction")
    void shouldSaveExportedRouteWithCorrectDirection() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.DIRECTION.getName())
                .isEqualTo(EXPECTED_DIRECTION);
    }

    @Test
    @DisplayName("Should save the exported route with the correct label")
    void shouldSaveExportedRouteWithCorrectLabel() {
        runSteps(STEPS);

        assertThat(jore4TargetTable).row().value(JORE4_ROUTE.LABEL.getName()).isEqualTo(EXPECTED_LABEL);
    }

    @Test
    @DisplayName("Should save the exported route with the correct line ID")
    void shouldSaveExportedRouteWithCorrectLineId() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.ON_LINE_ID.getName())
                .isEqualTo(EXPECTED_JORE4_ID_OF_LINE);
    }

    @Test
    @DisplayName("Should save the exported route with the correct priority")
    void shouldSaveExportedRouteWithCorrectPriority() {
        runSteps(STEPS);

        assertThat(jore4TargetTable).row().value(JORE4_ROUTE.PRIORITY.getName()).isEqualTo(EXPECTED_PRIORITY);
    }

    @Test
    @DisplayName("Should save the exported route with the correct validity period start time")
    void shouldSaveExportedRouteWithCorrectValidityPeriodStartTime() {
        runSteps(STEPS);

        final LocalDate validityPeriodStart = testRepository.findValidityPeriodStartDate();
        Assertions.assertThat(validityPeriodStart)
                .as(ROUTE_.VALIDITY_START.getName())
                .isEqualTo(VALIDITY_PERIOD_START);
    }

    @Test
    @DisplayName("Should save the exported route with the correct validity period end time")
    void shouldSaveExportedRouteWithCorrectValidityPeriodEndTime() {
        runSteps(STEPS);

        final LocalDate validityPeriodEnd = testRepository.findValidityPeriodEndDate();
        Assertions.assertThat(validityPeriodEnd)
                .as(ROUTE_.VALIDITY_END.getName())
                .isEqualTo(VALIDITY_PERIOD_END);
    }

    @Test
    @DisplayName("Should save the exported route with the correct legacy HSL municipality code")
    void shouldSaveExportedRouteWithCorrectLegacyHslMunicipalityCode() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.LEGACY_HSL_MUNICIPALITY_CODE.getName())
                .isEqualTo(EXPECTED_LEGACY_HSL_MUNICIPALITY_CODE.getJore4Value());
    }

    @Test
    @DisplayName("Should update the Jore 4 id of the route direction found from the importer's database")
    void shouldUpdateJore4IdOfRouteDirectionFoundFromImportersDatabase() {
        runSteps(STEPS);

        assertThat(importerTargetTable)
                .row()
                .value(IMPORTER_ROUTE_DIRECTION.NETWORK_ROUTE_JORE4_ID.getName())
                .isNotNull();
    }
}
