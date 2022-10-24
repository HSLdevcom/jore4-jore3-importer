package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteDirection;
import fi.hsl.jore.importer.feature.transmodel.repository.TransmodelValidityPeriodTestRepository;
import fi.hsl.jore.importer.feature.transmodel.repository.ValidityPeriodTargetTable;
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.UUID;

import static fi.hsl.jore.importer.TestJsonUtil.equalJson;
import static fi.hsl.jore.jore4.jooq.route.Tables.ROUTE_;
import static org.assertj.db.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/destination/drop_tables.sql",
        "/sql/destination/populate_infrastructure_nodes.sql",
        "/sql/destination/populate_lines_with_transmodel_ids.sql",
        "/sql/destination/populate_routes.sql",
        "/sql/destination/populate_route_directions.sql",
        "/sql/destination/populate_route_points_for_jore4_export.sql",
        "/sql/destination/populate_route_stop_points_for_jore4_export.sql",
        "/sql/destination/populate_scheduled_stop_points_for_jore4_export.sql"
})
@Sql(
        scripts = {
                "/sql/transmodel/drop_tables.sql",
                "/sql/transmodel/populate_infrastructure_links.sql",
                "/sql/transmodel/populate_lines.sql",
                "/sql/transmodel/populate_scheduled_stop_points.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
public class ExportRouteStepTest  extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("exportRoutesStep");

    private final String EXPECTED_DIRECTION = TransmodelRouteDirection.INBOUND.getValue();
    private final UUID EXPECTED_LINE_TRANSMODEL_ID = UUID.fromString("5aa7d9fc-2cf9-466d-8ac0-f442d60c261f");
    private static final String EXPECTED_LABEL = "1";
    private static final String EXPECTED_DESCRIPTION = "{\"fi_FI\":\"Keskustori - Etelä-Hervanta vanha\",\"sv_SE\":\"Central torget - Södra Hervanta gamla\"}";
    private static final int EXPECTED_PRIORITY = 10;

    private static final LocalDate VALIDITY_PERIOD_START = LocalDate.of(2021, 1, 1);
    // validity period end is specified with an open upper boundary
    private static final LocalDate VALIDITY_PERIOD_END = LocalDate.of(2022, 1, 1).minusDays(1);

    private static final fi.hsl.jore.importer.jooq.network.tables.NetworkRouteDirections IMPORTER_ROUTE_DIRECTION = fi.hsl.jore.importer.jooq.network.Tables.NETWORK_ROUTE_DIRECTIONS;
    private static final fi.hsl.jore.jore4.jooq.route.tables.Route JORE4_ROUTE = fi.hsl.jore.jore4.jooq.route.Tables.ROUTE_;

    private final Table importerTargetTable;
    private final Table jore4TargetTable;
    private final TransmodelValidityPeriodTestRepository testRepository;

    @Autowired
    public ExportRouteStepTest(final @Qualifier("importerDataSource") DataSource importerDataSource,
                               final @Qualifier("jore4DataSource") DataSource jore4DataSource) {
        this.importerTargetTable = new Table(importerDataSource, "network.network_route_directions");
        this.jore4TargetTable = new Table(jore4DataSource, "route.route");
        this.testRepository = new TransmodelValidityPeriodTestRepository(jore4DataSource,
                ValidityPeriodTargetTable.ROUTE
        );
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

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.ROUTE_ID.getName())
                .isNotNull();
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

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.LABEL.getName())
                .isEqualTo(EXPECTED_LABEL);
    }

    @Test
    @DisplayName("Should save the exported route with the correct line id")
    void shouldSaveExportedRouteWithCorrectLineId() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.ON_LINE_ID.getName())
                .isEqualTo(EXPECTED_LINE_TRANSMODEL_ID);
    }

    @Test
    @DisplayName("Should save the exported route with the correct priority")
    void shouldSaveExportedRouteWithCorrectPriority() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_ROUTE.PRIORITY.getName())
                .isEqualTo(EXPECTED_PRIORITY);
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
    @DisplayName("Should update the transmodel id of the route direction found from the importer's database")
    void shouldUpdateTransmodelIdOfRouteDirectionFoundFromImportersDatabase() {
        runSteps(STEPS);

        assertThat(importerTargetTable)
                .row()
                .value(IMPORTER_ROUTE_DIRECTION.NETWORK_ROUTE_TRANSMODEL_ID.getName())
                .isNotNull();
    }
}
