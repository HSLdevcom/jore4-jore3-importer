package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
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

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;
import static org.assertj.db.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/destination/drop_tables.sql",
        "/sql/destination/populate_infrastructure_nodes.sql",
        "/sql/destination/populate_lines_with_transmodel_ids.sql",
        "/sql/destination/populate_routes.sql",
        "/sql/destination/populate_route_directions_with_journey_pattern_transmodel_ids.sql",
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
                "/sql/transmodel/populate_routes.sql",
                "/sql/transmodel/populate_journey_patterns.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
public class ExportJourneyPatternStopsStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("exportJourneyPatternStopsStep");

    private static final String EXPECTED_JOURNEY_PATTERN_ID = "ec564137-f30c-4689-9322-4ef650768af3";

    private static final String EXPECTED_FIRST_SCHEDULED_STOP_POINT_LABEL = "H1234";
    private static final int EXPECTED_FIRST_SCHEDULED_STOP_POINT_SEQUENCE = 1;
    private static final boolean EXPECTED_FIRST_IS_USED_AS_TIMING_POINT = false;
    private static final boolean EXPECTED_FIRST_IS_VIA_POINT = false;

    private static final String EXPECTED_SECOND_SCHEDULED_STOP_POINT_LABEL = "H4321";
    private static final int EXPECTED_SECOND_SCHEDULED_STOP_POINT_SEQUENCE = 2;
    private static final boolean EXPECTED_SECOND_IS_USED_AS_TIMING_POINT = true;
    private static final boolean EXPECTED_SECOND_IS_VIA_POINT = true;

    private final Table targetTable;

    @Autowired
    ExportJourneyPatternStopsStepTest(final @Qualifier("jore4DataSource") DataSource jore4DataSource) {
        targetTable = new Table(jore4DataSource, "journey_pattern.scheduled_stop_point_in_journey_pattern");
    }

    @Test
    @DisplayName("Should insert two journey pattern stops into the Jore4 database")
    void shouldInsertTwoJourneyPatternStopsIntoJore4Database() {
        runSteps(STEPS);

        assertThat(targetTable).hasNumberOfRows(2);
    }

    @Test
    @DisplayName("Should insert the correct information of the first journey pattern stop into the Jore 4 database")
    void shouldInsertCorrectInformationOfFirstJourneyPatternStopIntoJore4Database() {
        runSteps(STEPS);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(targetTable)
                .row(0)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID.getName())
                .as("journeyPatternId")
                .isEqualTo(EXPECTED_JOURNEY_PATTERN_ID);
        softAssertions.assertThat(targetTable)
                .row(0)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL.getName())
                .as("scheduledStopPointLabel")
                .isEqualTo(EXPECTED_FIRST_SCHEDULED_STOP_POINT_LABEL);
        softAssertions.assertThat(targetTable)
                .row(0)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE.getName())
                .as("scheduledStopPointSequence")
                .isEqualTo(EXPECTED_FIRST_SCHEDULED_STOP_POINT_SEQUENCE);
        softAssertions.assertThat(targetTable)
                .row(0)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_USED_AS_TIMING_POINT.getName())
                .as("isTimingPoint")
                .isEqualTo(EXPECTED_FIRST_IS_USED_AS_TIMING_POINT);
        softAssertions.assertThat(targetTable)
                .row(0)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_VIA_POINT.getName())
                .as("isViaPoint")
                .isEqualTo(EXPECTED_FIRST_IS_VIA_POINT);
        softAssertions.assertAll();
    }

    @Test
    @DisplayName("Should insert the correct information of the second journey pattern stop into the Jore 4 database")
    void shouldInsertCorrectInformationOfSecondJourneyPatternStopIntoJore4Database() {
        runSteps(STEPS);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID.getName())
                .as("journeyPatternId")
                .isEqualTo(EXPECTED_JOURNEY_PATTERN_ID);
        softAssertions.assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL.getName())
                .as("scheduledStopPointLabel")
                .isEqualTo(EXPECTED_SECOND_SCHEDULED_STOP_POINT_LABEL);
        softAssertions.assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE.getName())
                .as("scheduledStopPointSequence")
                .isEqualTo(EXPECTED_SECOND_SCHEDULED_STOP_POINT_SEQUENCE);
        softAssertions.assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_USED_AS_TIMING_POINT.getName())
                .as("isTimingPoint")
                .isEqualTo(EXPECTED_SECOND_IS_USED_AS_TIMING_POINT);
        softAssertions.assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_VIA_POINT.getName())
                .as("isViaPoint")
                .isEqualTo(EXPECTED_SECOND_IS_VIA_POINT);
        softAssertions.assertAll();
    }
}
