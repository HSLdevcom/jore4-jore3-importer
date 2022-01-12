package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import io.vavr.collection.List;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;
import java.util.UUID;

import static org.assertj.db.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/destination/drop_tables.sql",
        "/sql/destination/populate_infrastructure_nodes.sql",
        "/sql/destination/populate_lines_with_transmodel_ids.sql",
        "/sql/destination/populate_routes_with_transmodel_ids.sql"
})
@Sql(
        scripts = {
                "/sql/transmodel/drop_tables.sql",
                "/sql/transmodel/populate_vehicle_modes.sql",
                "/sql/transmodel/populate_infrastructure_links.sql",
                "/sql/transmodel/populate_lines.sql",
                "/sql/transmodel/populate_scheduled_stop_points.sql",
                "/sql/transmodel/populate_routes.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
public class ExportJourneyPatternsStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("exportJourneyPatternsStep");
    private static final UUID EXPECTED_ROUTE_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");

    private static final fi.hsl.jore.importer.jooq.network.tables.NetworkRoutes IMPORTER_ROUTE = fi.hsl.jore.importer.jooq.network.Tables.NETWORK_ROUTES;
    private static final fi.hsl.jore.jore4.jooq.journey_pattern.tables.JourneyPattern JORE4_JOURNEY_PATTERN = fi.hsl.jore.jore4.jooq.journey_pattern.Tables.JOURNEY_PATTERN_;

    private final Table importerTargetTable;
    private final Table jore4TargetTable;

    @Autowired
    public ExportJourneyPatternsStepTest(final @Qualifier("importerDataSource") DataSource importerDataSource,
                                         final @Qualifier("jore4DataSource") DataSource jore4DataSource) {
        this.importerTargetTable = new Table(importerDataSource, "network.network_routes");
        this.jore4TargetTable = new Table(jore4DataSource, "journey_pattern.journey_pattern");
    }

    @Test
    @DisplayName("Should insert one journey pattern into the Jore 4 database")
    void shouldInsertOneJourneyPatternIntoJoreDatabase() {
        runSteps(STEPS);

        assertThat(jore4TargetTable).hasNumberOfRows(1);
    }

    @Test
    @DisplayName("Should generate a new id for the exported journey pattern")
    void shouldGenerateNewIdForExportedJourneyPattern() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_JOURNEY_PATTERN.JOURNEY_PATTERN_ID.getName())
                .isNotNull();
    }

    @Test
    @DisplayName("Should save the exported journey pattern with the correct route id")
    void shouldSaveExportedJourneyPatternWithCorrectRouteId() {
        runSteps(STEPS);

        assertThat(jore4TargetTable)
                .row()
                .value(JORE4_JOURNEY_PATTERN.ON_ROUTE_ID.getName())
                .isEqualTo(EXPECTED_ROUTE_ID);
    }

    @Test
    @DisplayName("Should update the journey pattern transmodel id of a route found from the importer's database")
    void shouldUpdateJourneyPatternTransmodelIdOfRouteFoundFromImporterDatabase() {
        runSteps(STEPS);

        assertThat(importerTargetTable)
                .row()
                .value(IMPORTER_ROUTE.JOURNEY_PATTERN_TRANSMODEL_ID.getName())
                .isNotNull();
    }
}
