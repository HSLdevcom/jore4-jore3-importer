package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPointDirection;
import fi.hsl.jore.importer.feature.transmodel.repository.TransmodelScheduledStopPointRepository;
import io.vavr.collection.List;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;

import static fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPointDirection.BACKWARD;
import static fi.hsl.jore.jore4.jooq.internal_service_pattern.Tables.SCHEDULED_STOP_POINT;
import static org.assertj.db.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/destination/drop_tables.sql",
        "/sql/destination/populate_infrastructure_nodes.sql",
        "/sql/destination/populate_scheduled_stop_points.sql"
})
@Sql(
        scripts = {
                "/sql/transmodel/drop_tables.sql",
                "/sql/transmodel/populate_infrastructure_links.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
class ExportScheduledStopPointsStepTest extends BatchIntegrationTest {

    private static final String SCHEDULED_STOP_POINT_EXTERNAL_ID = "1234567";
    private static final TransmodelScheduledStopPointDirection DIRECTION_ON_INFRALINK = BACKWARD;
    private static final String INFRASTRUCTURE_LINK_EXTERNAL_ID = "133202";
    private static final String EXPECTED_INFRASTRUCTURE_LINK_ID = "554c63e6-87b2-4dc8-a032-b6b0e2607696";
    private static final String LABEL = "Yliopisto vanha";
    private static final double X_COORDINATE = 25.696376131;
    private static final double Y_COORDINATE = 61.207149801;


    private static final List<String> STEPS = List.of("exportScheduledStopPointsStep");

    private final JdbcTemplate jdbcTemplate;
    private final DataSource targetDataSource;
    private final Table targetTable;

    @Autowired
    ExportScheduledStopPointsStepTest(final @Qualifier("jore4DataSource") DataSource targetDataSource) {
        this.jdbcTemplate = new JdbcTemplate(targetDataSource);
        this.targetDataSource = targetDataSource;
        this.targetTable = new Table(targetDataSource, "internal_service_pattern.scheduled_stop_point");
    }

    @Test
    @DisplayName("Should insert one scheduled stop point to the Jore4 database")
    void shouldInsertOneScheduledStopPointToJore4Database() {
        runSteps(STEPS);

        assertThat(targetTable).hasNumberOfRows(1);
    }

    @Test
    @DisplayName("Should generate a new id for the exported scheduled stop point")
    void shouldGenerateNewIdForInsertedScheduledStopPoint() {
        runSteps(STEPS);

        assertThat(targetTable)
                .row()
                .value(SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID.getName())
                .isNotNull();
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point with the correct direction")
    void shouldSaveExportedScheduledStopPointWithCorrectDirection() {
        runSteps(STEPS);

        assertThat(targetTable)
                .row()
                .value(SCHEDULED_STOP_POINT.DIRECTION.getName())
                .isEqualTo(DIRECTION_ON_INFRALINK.getValue());
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point with the correct infrastructure link id")
    void shouldSaveExportedScheduledStopPointWithCorrectInfrastructureLinkId() {
        runSteps(STEPS);

        assertThat(targetTable)
                .row()
                .value(SCHEDULED_STOP_POINT.LOCATED_ON_INFRASTRUCTURE_LINK_ID.getName())
                .isEqualTo(EXPECTED_INFRASTRUCTURE_LINK_ID);
    }

    @Test
    @DisplayName("Should save the exported scheduled stop point with the correct label")
    void shouldSaveExportedScheduledStopPointWithCorrectLabel() {
        runSteps(STEPS);

        assertThat(targetTable)
                .row()
                .value(SCHEDULED_STOP_POINT.LABEL.getName())
                .isEqualTo(LABEL);
    }
}
