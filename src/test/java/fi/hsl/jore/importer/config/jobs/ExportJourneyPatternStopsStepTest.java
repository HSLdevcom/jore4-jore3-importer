package fi.hsl.jore.importer.config.jobs;

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;
import static org.assertj.db.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import io.vavr.collection.List;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.assertj.core.api.Condition;
import org.assertj.db.api.SoftAssertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;
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
            "/sql/importer/populate_routes.sql",
            "/sql/importer/populate_route_directions_with_journey_pattern_jore4_ids.sql",
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
            "/sql/jore4/populate_routes.sql",
            "/sql/jore4/populate_journey_patterns.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource", transactionManager = "jore4TransactionManager"))
public class ExportJourneyPatternStopsStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("exportJourneyPatternStopsStep");

    private static final String EXPECTED_JOURNEY_PATTERN_ID = "ec564137-f30c-4689-9322-4ef650768af3";

    private static final String EXPECTED_FIRST_SCHEDULED_STOP_POINT_LABEL = "H1234";
    private static final int EXPECTED_FIRST_SCHEDULED_STOP_POINT_SEQUENCE = 1;
    private static final boolean EXPECTED_FIRST_IS_USED_AS_TIMING_POINT = true;
    private static final boolean EXPECTED_FIRST_IS_VIA_POINT = false;

    private static final String EXPECTED_SECOND_SCHEDULED_STOP_POINT_LABEL = "H4321";
    private static final int EXPECTED_SECOND_SCHEDULED_STOP_POINT_SEQUENCE = 2;
    private static final boolean EXPECTED_SECOND_IS_USED_AS_TIMING_POINT = false;
    private static final boolean EXPECTED_SECOND_IS_VIA_POINT = true;
    private static final Map<String, String> EXPECTED_SECOND_VIA_POINT_NAMES = ImmutableMap.<String, String>builder()
            .put("fi_FI", "ViaSuomi")
            .put("sv_SE", "ViaSverige")
            .build();

    private static final String EXPECTED_THIRD_SCHEDULED_STOP_POINT_LABEL = "H5678";
    private static final int EXPECTED_THIRD_SCHEDULED_STOP_POINT_SEQUENCE = 3;
    private static final boolean EXPECTED_THIRD_IS_USED_AS_TIMING_POINT = true;
    private static final boolean EXPECTED_THIRD_IS_VIA_POINT = false;

    private final Table targetTable;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    ExportJourneyPatternStopsStepTest(
            final @Qualifier("jore4DataSource") DataSource jore4DataSource, final IJsonbConverter jsonbConverter) {

        this.targetTable = new Table(jore4DataSource, "journey_pattern.scheduled_stop_point_in_journey_pattern");
        this.jsonbConverter = jsonbConverter;
    }

    @Test
    @DisplayName("Should insert three journey pattern stops into the Jore4 database")
    void shouldInsertTwoJourneyPatternStopsIntoJore4Database() {
        runSteps(STEPS);

        assertThat(targetTable).hasNumberOfRows(3);
    }

    @Test
    @DisplayName("Should insert the correct information of the first journey pattern stop into the Jore 4 database")
    void shouldInsertCorrectInformationOfFirstJourneyPatternStopIntoJore4Database() {
        runSteps(STEPS);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions
                .assertThat(targetTable)
                .row(0)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID.getName())
                .as("journeyPatternId")
                .isEqualTo(EXPECTED_JOURNEY_PATTERN_ID);
        softAssertions
                .assertThat(targetTable)
                .row(0)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL.getName())
                .as("scheduledStopPointLabel")
                .isEqualTo(EXPECTED_FIRST_SCHEDULED_STOP_POINT_LABEL);
        softAssertions
                .assertThat(targetTable)
                .row(0)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE.getName())
                .as("scheduledStopPointSequence")
                .isEqualTo(EXPECTED_FIRST_SCHEDULED_STOP_POINT_SEQUENCE);
        softAssertions
                .assertThat(targetTable)
                .row(0)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_USED_AS_TIMING_POINT.getName())
                .as("isTimingPoint")
                .isEqualTo(EXPECTED_FIRST_IS_USED_AS_TIMING_POINT);
        softAssertions
                .assertThat(targetTable)
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
        softAssertions
                .assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID.getName())
                .as("journeyPatternId")
                .isEqualTo(EXPECTED_JOURNEY_PATTERN_ID);
        softAssertions
                .assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL.getName())
                .as("scheduledStopPointLabel")
                .isEqualTo(EXPECTED_SECOND_SCHEDULED_STOP_POINT_LABEL);
        softAssertions
                .assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE.getName())
                .as("scheduledStopPointSequence")
                .isEqualTo(EXPECTED_SECOND_SCHEDULED_STOP_POINT_SEQUENCE);
        softAssertions
                .assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_USED_AS_TIMING_POINT.getName())
                .as("isTimingPoint")
                .isEqualTo(EXPECTED_SECOND_IS_USED_AS_TIMING_POINT);
        softAssertions
                .assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_VIA_POINT.getName())
                .as("isViaPoint")
                .isEqualTo(EXPECTED_SECOND_IS_VIA_POINT);
        softAssertions
                .assertThat(targetTable)
                .row(1)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.VIA_POINT_NAME_I18N.getName())
                .as("viaPointName")
                .satisfies(new Condition<PGobject>(
                        pgObject -> {
                            final String jsonbAsString = pgObject.getValue();

                            // Assert that conversion from PostgreSQL JSONB data type to a hash map
                            // yields correct result.
                            return jsonbConverter
                                    .fromJson(jsonbAsString, HashMap.class)
                                    .equals(EXPECTED_SECOND_VIA_POINT_NAMES);
                        },
                        "via name conversion"));
        softAssertions.assertAll();
    }

    @Test
    @DisplayName("Should insert the correct information of the third journey pattern stop into the Jore 4 database")
    void shouldInsertCorrectInformationOfThirdJourneyPatternStopIntoJore4Database() {
        runSteps(STEPS);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions
                .assertThat(targetTable)
                .row(2)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID.getName())
                .as("journeyPatternId")
                .isEqualTo(EXPECTED_JOURNEY_PATTERN_ID);
        softAssertions
                .assertThat(targetTable)
                .row(2)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL.getName())
                .as("scheduledStopPointLabel")
                .isEqualTo(EXPECTED_THIRD_SCHEDULED_STOP_POINT_LABEL);
        softAssertions
                .assertThat(targetTable)
                .row(2)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE.getName())
                .as("scheduledStopPointSequence")
                .isEqualTo(EXPECTED_THIRD_SCHEDULED_STOP_POINT_SEQUENCE);
        softAssertions
                .assertThat(targetTable)
                .row(2)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_USED_AS_TIMING_POINT.getName())
                .as("isTimingPoint")
                .isEqualTo(EXPECTED_THIRD_IS_USED_AS_TIMING_POINT);
        softAssertions
                .assertThat(targetTable)
                .row(2)
                .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_VIA_POINT.getName())
                .as("isViaPoint")
                .isEqualTo(EXPECTED_THIRD_IS_VIA_POINT);
        softAssertions.assertAll();
    }
}
