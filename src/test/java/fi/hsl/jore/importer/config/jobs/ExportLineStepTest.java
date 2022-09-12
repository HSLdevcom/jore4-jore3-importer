package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import fi.hsl.jore.importer.feature.jore4.repository.Jore4ValidityPeriodTestRepository;
import fi.hsl.jore.importer.feature.jore4.repository.ValidityPeriodTargetTable;
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static fi.hsl.jore.importer.TestJsonUtil.equalJson;
import static fi.hsl.jore.importer.feature.jore4.util.TimestampFactory.offsetDateTimeFromLocalDateTime;
import static org.assertj.db.api.Assertions.assertThat;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/destination/drop_tables.sql",
        "/sql/destination/populate_lines.sql",
        "/sql/destination/populate_line_headers.sql"
})
@Sql(
        scripts = {
                "/sql/transmodel/drop_tables.sql"
        },
        config = @SqlConfig(dataSource = "jore4DataSource")
)
class ExportLineStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("exportLinesStep");

    private static final String EXPECTED_LABEL = "1";
    private static final String EXPECTED_NAME = "{\"fi_FI\":\"Eira - Töölö - Sörnäinen (M) - Käpylä\",\"sv_SE\":\"Eira - Tölö - Sörnäs (M) - Kottby\"}";
    private static final String EXPECTED_SHORT_NAME = "{\"fi_FI\":\"Eira-Töölö-Käpylä\",\"sv_SE\":\"Eira-Tölö-Kottby\"}";

    private static final VehicleMode EXPECTED_PRIMARY_VEHICLE_MODE = VehicleMode.TRAM;
    private static final int EXPECTED_PRIORITY = 10;

    private static final OffsetDateTime EXPECTED_VALIDITY_PERIOD_START_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                    LocalDate.of(2021, 10, 4),
                    OPERATING_DAY_START_TIME
            )
    );
    private static final OffsetDateTime EXPECTED_VALIDITY_PERIOD_END_AT_FINNISH_TIME_ZONE = offsetDateTimeFromLocalDateTime(
            LocalDateTime.of(
                    LocalDate.of(2051, 1, 1),
                    OPERATING_DAY_END_TIME
            )
    );

    private static final fi.hsl.jore.importer.jooq.network.tables.NetworkLines IMPORTER_LINE = fi.hsl.jore.importer.jooq.network.Tables.NETWORK_LINES;
    private static final fi.hsl.jore.jore4.jooq.route.tables.Line JORE4_LINE = fi.hsl.jore.jore4.jooq.route.Tables.LINE;

    private final Table importerTargetTable;
    private final Table jore4TargetTable;
    private final Jore4ValidityPeriodTestRepository testRepository;

    @Autowired
    ExportLineStepTest(final @Qualifier("importerDataSource") DataSource importerDataSource,
                       final @Qualifier("jore4DataSource") DataSource jore4DataSource) {
        this.importerTargetTable = new Table(importerDataSource, "network.network_lines");
        this.jore4TargetTable = new Table(jore4DataSource, "route.line");
        this.testRepository = new Jore4ValidityPeriodTestRepository(jore4DataSource,
                ValidityPeriodTargetTable.LINE
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
                .value(JORE4_LINE.LINE_ID.getName())
                .isNotNull();
    }

    @Test
    @DisplayName("Should save the exported line with the correct information")
    void shouldSaveExportedLineWithCorrectInformation() {
        runSteps(STEPS);

        final SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(jore4TargetTable)
                .row()
                .value(JORE4_LINE.NAME_I18N.getName())
                .is(equalJson(EXPECTED_NAME));

        softAssertions.assertThat(jore4TargetTable)
                .row()
                .value(JORE4_LINE.LABEL.getName())
                .isEqualTo(EXPECTED_LABEL);

        softAssertions.assertThat(jore4TargetTable)
                .row()
                .value(JORE4_LINE.SHORT_NAME_I18N.getName())
                .is(equalJson(EXPECTED_SHORT_NAME));

        softAssertions.assertThat(jore4TargetTable)
                .row()
                .value(JORE4_LINE.PRIMARY_VEHICLE_MODE.getName())
                .isEqualTo(EXPECTED_PRIMARY_VEHICLE_MODE.getValue());
        softAssertions.assertThat(jore4TargetTable)
                .row()
                .value(JORE4_LINE.PRIORITY.getName())
                .isEqualTo(EXPECTED_PRIORITY);

        softAssertions.assertAll();

        final OffsetDateTime validityStart = testRepository.findValidityPeriodStartTimestampAtFinnishTimeZone();
        Assertions.assertThat(validityStart)
                .as(JORE4_LINE.VALIDITY_START.getName())
                .isEqualTo(EXPECTED_VALIDITY_PERIOD_START_AT_FINNISH_TIME_ZONE);

        final OffsetDateTime validityEnd = testRepository.findValidityPeriodEndTimestampAtFinnishTimeZone();
        Assertions.assertThat(validityEnd)
                .as(JORE4_LINE.VALIDITY_END.getName())
                .isEqualTo(EXPECTED_VALIDITY_PERIOD_END_AT_FINNISH_TIME_ZONE);
    }

    @Test
    @DisplayName("Should update the transmodel id of the line found from the importer's database")
    void shouldUpdateTransmodelIdOfLineFoundFromImportersDatabase() {
        runSteps(STEPS);

        assertThat(importerTargetTable)
                .row()
                .value(IMPORTER_LINE.NETWORK_LINE_TRANSMODEL_ID.getName())
                .isNotNull();
    }
}
