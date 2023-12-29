package fi.hsl.jore.importer.feature.batch.line;

import com.google.common.collect.BoundType;
import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import fi.hsl.jore.importer.feature.network.line.dto.ImporterLine;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntTest
class LineExportReaderTest {

    private static final String EXPECTED_EXTERNAL_ID_OF_LINE_HEADER = "1001-20211004";

    private static final String EXPECTED_LINE_NUMBER = "1";

    private static final String EXPECTED_FINNISH_NAME = "Eira - Töölö - Sörnäinen (M) - Käpylä";
    private static final String EXPECTED_FINNISH_SHORT_NAME = "Eira-Töölö-Käpylä";
    private static final String EXPECTED_SWEDISH_NAME = "Eira - Tölö - Sörnäs (M) - Kottby";
    private static final String EXPECTED_SWEDISH_SHORT_NAME = "Eira-Tölö-Kottby";

    private static final NetworkType EXPECTED_NETWORK_TYPE = NetworkType.ROAD;
    private static final TypeOfLine EXPECTED_TYPE_OF_LINE = TypeOfLine.STOPPING_BUS_SERVICE;
    private static final LegacyHslMunicipalityCode EXPECTED_LEGACY_HSL_MUNICIPALITY_CODE = LegacyHslMunicipalityCode.HELSINKI;

    private static final LocalDate EXPECTED_VALID_DATE_RANGE_START = LocalDate.of(2021, 10, 4);
    private static final LocalDate EXPECTED_VALID_DATE_RANGE_END  = LocalDate.of(2051, 1, 1);

    private final JdbcCursorItemReader<ImporterLine> reader;

    @Autowired
    LineExportReaderTest(final LineExportReader reader) {
        this.reader = reader.build();
    }

    @BeforeEach
    void openReader() {
        this.reader.open(new ExecutionContext());
    }

    @AfterEach
    void closeReader() {
        this.reader.close();
    }

    @Nested
    @DisplayName("When the source tables are empty")
    @Sql(scripts = "/sql/importer/drop_tables.sql")
    class WhenSourceTablesAreEmpty {

        @Test
        @DisplayName("The first invocation of the read() method must return null")
        void firstInvocationOfReadMethodMustReturnNull() throws Exception {
            final ImporterLine found = reader.read();
            assertThat(found).isNull();
        }
    }

    @Nested
    @DisplayName("When the source tables have one line")
    @Sql(scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_lines.sql",
            "/sql/importer/populate_line_headers.sql"
    })
    @ExtendWith(SoftAssertionsExtension.class)
    class WhenSourceTablesHaveOneLine {

        @Test
        @DisplayName("The first invocation of the read() method must return the found line")
        void firstInvocationOfReadMethodMustReturnFoundLine(final SoftAssertions softAssertions) throws Exception {
            final ImporterLine line = reader.read();

            softAssertions.assertThat(line.externalIdOfLineHeader().value())
                    .as("externalIdOfLineHeader")
                    .isEqualTo(EXPECTED_EXTERNAL_ID_OF_LINE_HEADER);

            softAssertions.assertThat(line.lineNumber())
                    .as("lineNumber")
                    .isEqualTo(EXPECTED_LINE_NUMBER);

            softAssertions.assertThat(line.validDateRange().range().lowerEndpoint())
                    .as("validDateRangeStartDate")
                    .isEqualTo(EXPECTED_VALID_DATE_RANGE_START);

            softAssertions.assertThat(line.validDateRange().range().lowerBoundType())
                    .as("validDateRangeStartBoundType")
                    .isEqualTo(BoundType.CLOSED);

            softAssertions.assertThat(line.validDateRange().range().upperEndpoint())
                    .as("validDateRangeEndDate")
                    .isEqualTo(EXPECTED_VALID_DATE_RANGE_END);

            softAssertions.assertThat(line.validDateRange().range().upperBoundType())
                    .as("validDateRangeEndBoundType")
                    .isEqualTo(BoundType.OPEN);

            final String finnishName = JoreLocaleUtil.getI18nString(line.name(), JoreLocaleUtil.FINNISH);
            softAssertions.assertThat(finnishName)
                    .as("Finnish name")
                    .isEqualTo(EXPECTED_FINNISH_NAME);

            final String swedishName = JoreLocaleUtil.getI18nString(line.name(), JoreLocaleUtil.SWEDISH);
            softAssertions.assertThat(swedishName)
                    .as("Swedish name")
                    .isEqualTo(EXPECTED_SWEDISH_NAME);

            final String finnishShortName = JoreLocaleUtil.getI18nString(line.shortName(), JoreLocaleUtil.FINNISH);
            softAssertions.assertThat(finnishShortName)
                    .as("Finnish short name")
                    .isEqualTo(EXPECTED_FINNISH_SHORT_NAME);

            final String swedishShortName = JoreLocaleUtil.getI18nString(line.shortName(), JoreLocaleUtil.SWEDISH);
            softAssertions.assertThat(swedishShortName)
                    .as("Swedish short name")
                    .isEqualTo(EXPECTED_SWEDISH_SHORT_NAME);

            softAssertions.assertThat(line.networkType())
                    .as("Network type")
                    .isEqualTo(EXPECTED_NETWORK_TYPE);

            softAssertions.assertThat(line.typeOfLine())
                .as("Type of line")
                .isEqualTo(EXPECTED_TYPE_OF_LINE);

            softAssertions.assertThat(line.legacyHslMunicipalityCode())
                .as("Legacy HSL municipality code")
                .isEqualTo(EXPECTED_LEGACY_HSL_MUNICIPALITY_CODE);
        }

        @Test
        @DisplayName("The second invocation of the read() method must return null")
        void secondInvocationOfReadMethodMustReturnNull() throws Exception {
            //The first invocation returns the scheduled stop found from the database.
            final ImporterLine first = reader.read();
            assertThat(first).isNotNull();

            //Because there are no more scheduled stop points, this invocation must return null.
            final ImporterLine second = reader.read();
            assertThat(second).isNull();
        }
    }
}
