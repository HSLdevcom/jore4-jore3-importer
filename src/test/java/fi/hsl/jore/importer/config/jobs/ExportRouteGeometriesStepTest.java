package fi.hsl.jore.importer.config.jobs;

import static fi.hsl.jore.jore4.jooq.route.Tables.INFRASTRUCTURE_LINK_ALONG_ROUTE;
import static org.assertj.db.api.Assertions.assertThat;

import fi.hsl.jore.importer.BatchIntegrationTest;
import io.vavr.collection.List;
import java.util.UUID;
import javax.sql.DataSource;
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
            "/sql/importer/populate_infrastructure_links.sql",
            "/sql/importer/populate_infrastructure_link_shapes.sql",
            "/sql/importer/populate_lines.sql",
            "/sql/importer/populate_routes.sql",
            "/sql/importer/populate_route_directions_with_jore4_ids.sql",
            "/sql/importer/populate_route_links.sql",
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
            "/sql/jore4/populate_lines.sql",
            "/sql/jore4/populate_routes_without_infrastructure_links.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource", transactionManager = "jore4TransactionManager"))
public class ExportRouteGeometriesStepTest extends BatchIntegrationTest {

    private static final UUID EXPECTED_INFRASTRUCTURE_LINK_ID = UUID.fromString("554c63e6-87b2-4dc8-a032-b6b0e2607696");
    private static final boolean EXPECTED_INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS = true;
    private static final int EXPECTED_INFRASTRUCTURE_LINK_SEQUENCE = 0;
    private static final UUID EXPECTED_ROUTE_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");

    private static final List<String> STEPS = List.of("exportRouteGeometriesStep");

    private final Table targetTable;

    @Autowired
    public ExportRouteGeometriesStepTest(final @Qualifier("jore4DataSource") DataSource jore4DataSource) {
        this.targetTable = new Table(jore4DataSource, "route.infrastructure_link_along_route");
    }

    @Test
    @DisplayName("Should insert one row into the target table")
    void shouldInsertOneRowIntoTargetTable() {
        runSteps(STEPS);
        assertThat(targetTable).hasNumberOfRows(1);
    }

    @Test
    @DisplayName("Should save a new route infrastructure link with the correct route id")
    void shouldSaveNewRouteInfrastructureLinkWithCorrectRouteId() {
        runSteps(STEPS);
        assertThat(targetTable)
                .row()
                .value(INFRASTRUCTURE_LINK_ALONG_ROUTE.ROUTE_ID.getName())
                .isEqualTo(EXPECTED_ROUTE_ID);
    }

    @Test
    @DisplayName("Should save a new route infrastructure link with the correct infrastructure link id")
    void shouldSaveNewRouteInfrastructureLinkWithCorrectInfrastructureLinkId() {
        runSteps(STEPS);
        assertThat(targetTable)
                .row()
                .value(INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_ID.getName())
                .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ID);
    }

    @Test
    @DisplayName("Should save a new route infrastructure link with the correct infrastructure link sequence")
    void shouldSaveNewRouteInfrastructureLinkWithCorrectInfrastructureLinkSequence() {
        runSteps(STEPS);
        assertThat(targetTable)
                .row()
                .value(INFRASTRUCTURE_LINK_ALONG_ROUTE.INFRASTRUCTURE_LINK_SEQUENCE.getName())
                .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_SEQUENCE);
    }

    @Test
    @DisplayName("Should save a new route infrastructure link with correct traversal direction")
    void shouldSaveNewRouteInfrastructureLinkWithCorrectTraversalDirection() {
        runSteps(STEPS);
        assertThat(targetTable)
                .row()
                .value(INFRASTRUCTURE_LINK_ALONG_ROUTE.IS_TRAVERSAL_FORWARDS.getName())
                .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_IS_TRAVERSAL_FORWARDS);
    }
}
