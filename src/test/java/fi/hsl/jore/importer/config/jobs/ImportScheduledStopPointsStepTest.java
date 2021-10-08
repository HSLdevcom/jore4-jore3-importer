package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.repository.IScheduledStopPointTestRepository;
import io.vavr.collection.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/source/drop_tables.sql",
        "/sql/source/populate_nodes.sql",
        "/sql/source/populate_scheduled_stop_points.sql"
},
        config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = {
        "/sql/destination/drop_tables.sql",
        "/sql/destination/populate_infrastructure_nodes.sql"
})
@ExtendWith(SoftAssertionsExtension.class)
class ImportScheduledStopPointsStepTest extends BatchIntegrationTest {

    private static final String EXPECTED_EXTERNAL_ID = "c";
    private static final UUID EXPECTED_INFRASTRUCTURE_NODE_ID = UUID.fromString("cc11a5db-2ae7-4220-adfe-aca5d6620909");
    private static final double EXPECTED_X = 6.0;
    private static final double EXPECTED_Y = 5.0;
    private static final String EXPECTED_FINNISH_NAME = "Yliopisto";
    private static final String EXPECTED_SWEDISH_NAME = "Universitetet";

    public static final String LOCALE_FINNISH = "fi_FI";
    public static final String LOCALE_SWEDISH = "sv_SE";

    private static final List<String> STEPS = List.of("prepareScheduledStopPointsStep",
            "importScheduledStopPointsStep",
            "commitScheduledStopPointsStep");

    private final IScheduledStopPointTestRepository repository;

    @Autowired
    ImportScheduledStopPointsStepTest(final IScheduledStopPointTestRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void ensureThatTargetDbIsEmpty() {
        assertThat(repository.empty()).isTrue();
    }

    @Test
    void shouldImportScheduledStopPointFromSourceDatabase(SoftAssertions softAssertions) {
        runSteps(STEPS);

        softAssertions.assertThat(repository.count())
                .as("imported scheduled stop count")
                .isEqualTo(1);

        ScheduledStopPoint imported = repository.findByExternalId(ExternalId.of(EXPECTED_EXTERNAL_ID)).get();

        softAssertions.assertThat(imported.externalId().value())
                .as("external id")
                .isEqualTo(EXPECTED_EXTERNAL_ID);

        softAssertions.assertThat(imported.node().value())
                .as("infrastructure node id")
                .isEqualTo(EXPECTED_INFRASTRUCTURE_NODE_ID);

        softAssertions.assertThat(imported.location().getX())
                .as("X")
                .isEqualTo(EXPECTED_X);

        softAssertions.assertThat(imported.location().getY())
                .as("Y")
                .isEqualTo(EXPECTED_Y);

        String finnishName = imported.name().values().getOrElse(LOCALE_FINNISH, "No string found with the given locale");
        softAssertions.assertThat(finnishName)
                .as("finnish name")
                .isEqualTo(EXPECTED_FINNISH_NAME);

        String swedishName = imported.name().values().getOrElse(LOCALE_SWEDISH, "No string found with the given locale");
        softAssertions.assertThat(swedishName)
                .as("swedish name")
                .isEqualTo(EXPECTED_SWEDISH_NAME);
    }
}
