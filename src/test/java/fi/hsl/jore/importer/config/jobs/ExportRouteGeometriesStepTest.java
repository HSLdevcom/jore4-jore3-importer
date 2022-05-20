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

import static fi.hsl.jore.jore4.jooq.route.Tables.INFRASTRUCTURE_LINK_ALONG_ROUTE;
import static org.assertj.db.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/destination/drop_tables.sql",
        "/sql/destination/populate_infrastructure_nodes.sql",
        "/sql/destination/populate_infrastructure_links.sql",
        "/sql/destination/populate_infrastructure_link_shapes.sql",
        "/sql/destination/populate_lines_with_transmodel_ids.sql",
        "/sql/destination/populate_routes.sql",
        "/sql/destination/populate_route_directions_with_transmodel_ids.sql",
        "/sql/destination/populate_route_links.sql",
        "/sql/destination/populate_route_points_for_jore4_export.sql",
        "/sql/destination/populate_route_stop_points_for_jore4_export.sql",
        "/sql/destination/populate_scheduled_stop_points_for_jore4_export.sql"
})
@Sql(
        scripts = {
                "/sql/transmodel/drop_tables.sql",
                "/sql/transmodel/populate_infrastructure_links.sql",
                "/sql/transmodel/populate_lines.sql",
                "/sql/transmodel/populate_scheduled_stop_points.sql",
                "/sql/transmodel/populate_routes_without_infrastructure_links.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
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
