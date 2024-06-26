package fi.hsl.jore.importer.config.jobs;

import static fi.hsl.jore.importer.TestJsonUtil.equalJson;
import static org.assertj.db.api.Assertions.assertThat;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import fi.hsl.jore.importer.feature.jore4.repository.Jore4ValidityPeriodTestRepository;
import fi.hsl.jore.importer.feature.jore4.repository.ValidityPeriodTargetTable;
import fi.hsl.jore.importer.jooq.network.Tables;
import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
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

@ContextConfiguration(classes = JobConfig.class)
@Sql(
        scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_lines.sql",
            "/sql/importer/populate_line_headers.sql"
        })
@Sql(
        scripts = {"/sql/jore4/drop_tables.sql"},
        config = @SqlConfig(dataSource = "jore4DataSource", transactionManager = "jore4TransactionManager"))
class ExportLineStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("exportLinesStep");

    private static final String EXPECTED_LABEL = "1";
    private static final String EXPECTED_NAME =
            "{\"fi_FI\":\"Eira - Töölö - Sörnäinen (M) - Käpylä\",\"sv_SE\":\"Eira - Tölö - Sörnäs (M) - Kottby\"}";
    private static final String EXPECTED_SHORT_NAME =
            "{\"fi_FI\":\"Eira-Töölö-Käpylä\",\"sv_SE\":\"Eira-Tölö-Kottby\"}";

    private static final VehicleMode EXPECTED_PRIMARY_VEHICLE_MODE = VehicleMode.BUS;
    private static final int EXPECTED_PRIORITY = 10;
    private static final LegacyHslMunicipalityCode EXPECTED_LEGACY_HSL_MUNICIPALITY_CODE =
            LegacyHslMunicipalityCode.HELSINKI;

    private static final LocalDate EXPECTED_VALIDITY_PERIOD_START = LocalDate.of(2021, 10, 4);
    // validity period end is specified with an open upper boundary
    private static final LocalDate EXPECTED_VALIDITY_PERIOD_END =
            LocalDate.of(2051, 1, 1).minusDays(1);

    private static final fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeaders IMPORTER_LINE_HEADER =
            Tables.NETWORK_LINE_HEADERS;
    private static final fi.hsl.jore.jore4.jooq.route.tables.Line JORE4_LINE = fi.hsl.jore.jore4.jooq.route.Tables.LINE;

    private final Table importerLineHeaderTable;
    private final Table jore4LineTable;
    private final Jore4ValidityPeriodTestRepository testRepository;

    @Autowired
    ExportLineStepTest(
            final @Qualifier("importerDataSource") DataSource importerDataSource,
            final @Qualifier("jore4DataSource") DataSource jore4DataSource) {
        this.importerLineHeaderTable = new Table(importerDataSource, "network.network_line_headers");
        this.jore4LineTable = new Table(jore4DataSource, "route.line");
        this.testRepository = new Jore4ValidityPeriodTestRepository(jore4DataSource, ValidityPeriodTargetTable.LINE);
    }

    @Test
    @DisplayName("Should insert one line into the Jore 4 database")
    void shouldInsertOneLineIntoJoreDatabase() {
        runSteps(STEPS);

        assertThat(jore4LineTable).hasNumberOfRows(1);
    }

    @Test
    @DisplayName("Should generate a new id for the exported line")
    void shouldGenerateNewIdForExportedLine() {
        runSteps(STEPS);

        assertThat(jore4LineTable).row().value(JORE4_LINE.LINE_ID.getName()).isNotNull();
    }

    @Test
    @DisplayName("Should save the exported line with the correct information")
    void shouldSaveExportedLineWithCorrectInformation() {
        runSteps(STEPS);

        final SoftAssertions softAssertions = new SoftAssertions();

        softAssertions
                .assertThat(jore4LineTable)
                .row()
                .value(JORE4_LINE.NAME_I18N.getName())
                .is(equalJson(EXPECTED_NAME));

        softAssertions
                .assertThat(jore4LineTable)
                .row()
                .value(JORE4_LINE.LABEL.getName())
                .isEqualTo(EXPECTED_LABEL);

        softAssertions
                .assertThat(jore4LineTable)
                .row()
                .value(JORE4_LINE.SHORT_NAME_I18N.getName())
                .is(equalJson(EXPECTED_SHORT_NAME));

        softAssertions
                .assertThat(jore4LineTable)
                .row()
                .value(JORE4_LINE.PRIMARY_VEHICLE_MODE.getName())
                .isEqualTo(EXPECTED_PRIMARY_VEHICLE_MODE.getValue());

        softAssertions
                .assertThat(jore4LineTable)
                .row()
                .value(JORE4_LINE.PRIORITY.getName())
                .isEqualTo(EXPECTED_PRIORITY);

        softAssertions
                .assertThat(jore4LineTable)
                .row()
                .value(JORE4_LINE.LEGACY_HSL_MUNICIPALITY_CODE.getName())
                .isEqualTo(EXPECTED_LEGACY_HSL_MUNICIPALITY_CODE.getJore4Value());

        softAssertions.assertAll();

        final LocalDate validityStart = testRepository.findValidityPeriodStartDate();
        Assertions.assertThat(validityStart)
                .as(JORE4_LINE.VALIDITY_START.getName())
                .isEqualTo(EXPECTED_VALIDITY_PERIOD_START);

        final LocalDate validityEnd = testRepository.findValidityPeriodEndDate();
        Assertions.assertThat(validityEnd)
                .as(JORE4_LINE.VALIDITY_END.getName())
                .isEqualTo(EXPECTED_VALIDITY_PERIOD_END);
    }

    @Test
    @DisplayName("Should update the Jore 4 ID to the line header found from the importer's database")
    void shouldUpdateJore4IdOfLineFoundFromImportersDatabase() {
        runSteps(STEPS);

        assertThat(importerLineHeaderTable)
                .row()
                .value(IMPORTER_LINE_HEADER.JORE4_LINE_ID.getName())
                .isNotNull();
    }
}
