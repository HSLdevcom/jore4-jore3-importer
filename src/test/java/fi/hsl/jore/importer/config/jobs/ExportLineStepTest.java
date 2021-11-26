package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.transmodel.entity.VehicleMode;
import io.vavr.collection.List;
import org.assertj.db.api.SoftAssertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;

import static fi.hsl.jore.jore4.jooq.route.Tables.LINE;
import static org.assertj.db.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/destination/drop_tables.sql",
        "/sql/destination/populate_lines.sql",
        "/sql/destination/populate_line_headers.sql"
})
@Sql(
        scripts = {
                "/sql/transmodel/drop_tables.sql",
                "/sql/transmodel/populate_vehicle_modes.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
class ExportLineStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("exportLinesStep");

    private static final String EXPECTED_NAME = "{\"fi_FI\":\"Eira - Töölö - Sörnäinen (M) - Käpylä\",\"sv_SE\":\"Eira - Tölö - Sörnäs (M) - Kottby\"}";
    private static final String EXPECTED_SHORT_NAME = "{\"fi_FI\":\"Eira-Töölö-Käpylä\",\"sv_SE\":\"Eira-Tölö-Kottby\"}";

    private static final VehicleMode EXPECTED_PRIMARY_VEHICLE_MODE = VehicleMode.TRAM;

    private final Table targetTable;

    @Autowired
    ExportLineStepTest(final @Qualifier("jore4DataSource") DataSource targetDataSource) {
        this.targetTable = new Table(targetDataSource, "route.line");
    }

    @Test
    @DisplayName("Should insert one line into the Jore 4 database")
    void shouldInsertOneLineIntoJoreDatabase() {
        runSteps(STEPS);

        assertThat(targetTable).hasNumberOfRows(1);
    }

    @Test
    @DisplayName("Should generate a new id for the exported line")
    void shouldGenerateNewIdForExportedLine() {
        runSteps(STEPS);

        assertThat(targetTable)
                .row()
                .value(LINE.LINE_ID.getName())
                .isNotNull();
    }

    @Test
    @DisplayName("Should save the exported line with the correct information")
    void shouldSaveExportedLineWithCorrectInformation() {
        runSteps(STEPS);

        final SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(targetTable)
                .row()
                .value(LINE.NAME_I18N.getName())
                .isEqualTo(EXPECTED_NAME);
        softAssertions.assertThat(targetTable)
                .row()
                .value(LINE.SHORT_NAME_I18N.getName())
                .isEqualTo(EXPECTED_SHORT_NAME);
        softAssertions.assertThat(targetTable)
                .row()
                .value(LINE.PRIMARY_VEHICLE_MODE.getName())
                .isEqualTo(EXPECTED_PRIMARY_VEHICLE_MODE.getValue());
        softAssertions.assertThat(targetTable)
                .row()
                .value(LINE.DESCRIPTION_I18N.getName())
                .isNull();

        softAssertions.assertAll();
    }
}
