package fi.hsl.jore.importer.config.jobs;

import static org.assertj.core.api.Assertions.assertThat;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.repository.IScheduledStopPointTestRepository;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@ContextConfiguration(classes = JobConfig.class)
@Sql(
        scripts = {
            "/sql/jore3/drop_tables.sql",
            "/sql/jore3/populate_nodes.sql",
            "/sql/jore3/populate_routes.sql",
            "/sql/jore3/populate_route_links.sql",
            "/sql/jore3/populate_places.sql",
            "/sql/jore3/populate_scheduled_stop_points.sql"
        },
        config = @SqlConfig(dataSource = "sourceDataSource", transactionManager = "sourceTransactionManager"))
@Sql(
        scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_places.sql",
            "/sql/importer/populate_infrastructure_nodes.sql"
        })
@ExtendWith(SoftAssertionsExtension.class)
class ImportScheduledStopPointsStepTest extends BatchIntegrationTest {

    private static final String EXPECTED_EXTERNAL_ID = "c";
    private static final Long EXPECTED_ELY_NUMBER = 1234567890L;
    private static final UUID EXPECTED_INFRASTRUCTURE_NODE_ID = UUID.fromString("cc11a5db-2ae7-4220-adfe-aca5d6620909");
    private static final String EXPECTED_FINNISH_NAME = "Yliopisto";
    private static final String EXPECTED_SWEDISH_NAME = "Universitetet";
    private static final String EXPECTED_SHORT_ID = "H1234";
    private static final UUID EXPECTED_PLACE_ID = UUID.fromString("dfdf8d1d-8649-43bf-9fda-6c23a500a189");
    private static final int EXPECTED_USAGE_IN_ROUTES = 1;

    private static final List<String> STEPS =
            List.of("prepareScheduledStopPointsStep", "importScheduledStopPointsStep", "commitScheduledStopPointsStep");

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
    void shouldImportScheduledStopPointFromSourceDatabase(final SoftAssertions softAssertions) {
        runSteps(STEPS);

        softAssertions
                .assertThat(repository.count())
                .as("imported scheduled stop count")
                .isEqualTo(1);

        final ScheduledStopPoint imported =
                repository.findByExternalId(ExternalId.of(EXPECTED_EXTERNAL_ID)).get();

        softAssertions
                .assertThat(imported.externalId().value())
                .as("external id")
                .isEqualTo(EXPECTED_EXTERNAL_ID);

        softAssertions
                .assertThat(imported.node().value())
                .as("infrastructure node id")
                .isEqualTo(EXPECTED_INFRASTRUCTURE_NODE_ID);

        softAssertions.assertThat(imported.elyNumber()).as("elyNumber").contains(EXPECTED_ELY_NUMBER);

        final String finnishName = JoreLocaleUtil.getI18nString(imported.name(), JoreLocaleUtil.FINNISH);
        softAssertions.assertThat(finnishName).as("finnish name").isEqualTo(EXPECTED_FINNISH_NAME);

        final String swedishName = JoreLocaleUtil.getI18nString(imported.name(), JoreLocaleUtil.SWEDISH);
        softAssertions.assertThat(swedishName).as("swedish name").isEqualTo(EXPECTED_SWEDISH_NAME);

        softAssertions.assertThat(imported.shortId()).as("shortId").contains(EXPECTED_SHORT_ID);

        softAssertions.assertThat(imported.jore4Id()).as("Jore 4 id").isEmpty();

        softAssertions.assertThat(imported.placeId()).as("placeId").contains(EXPECTED_PLACE_ID);

        softAssertions
                .assertThat(imported.usageInRoutes())
                .as("usage in routes")
                .isEqualTo(EXPECTED_USAGE_IN_ROUTES);
    }
}
