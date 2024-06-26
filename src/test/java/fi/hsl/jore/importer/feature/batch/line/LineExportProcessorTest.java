package fi.hsl.jore.importer.feature.batch.line;

import static org.assertj.core.api.Assertions.assertThat;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import fi.hsl.jore.importer.feature.network.line.dto.ImporterLine;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LineExportProcessorTest {

    private static final String LINE_NUMBER = "35";
    private static final String EXTERNAL_ID_OF_LINE_HEADER = "1035-20240101";
    private static final String FINNISH_NAME = "Vantaanportti-Lentoasema-Kerava";
    private static final String FINNISH_SHORT_NAME = "Vantaanp-Kerava";
    private static final String SWEDISH_NAME = "Vandaporten-Flygstationen-Kervo";
    private static final String SWEDISH_SHORT_NAME = "Vandap-Kervo";
    private static final LocalDate VALID_DATE_RANGE_START = LocalDate.of(2000, 6, 5);
    private static final LocalDate VALID_DATE_RANGE_END = LocalDate.of(2005, 2, 1);

    private static final NetworkType NETWORK_TYPE_ROAD = NetworkType.ROAD;
    private static final TypeOfLine TYPE_OF_LINE = TypeOfLine.SPECIAL_NEEDS_BUS;
    private static final LegacyHslMunicipalityCode LEGACY_HSL_MUNICIPALITY_CODE = LegacyHslMunicipalityCode.HELSINKI;
    private static final VehicleMode EXPECTED_PRIMARY_VEHICLE_MODE = VehicleMode.BUS;

    private static final int EXPECTED_PRIORITY = 10;

    private static final ImporterLine INPUT = ImporterLine.of(
            ExternalId.of(EXTERNAL_ID_OF_LINE_HEADER),
            LINE_NUMBER,
            JoreLocaleUtil.createMultilingualString(FINNISH_NAME, SWEDISH_NAME),
            NETWORK_TYPE_ROAD,
            JoreLocaleUtil.createMultilingualString(FINNISH_SHORT_NAME, SWEDISH_SHORT_NAME),
            DateRange.between(VALID_DATE_RANGE_START, VALID_DATE_RANGE_END),
            TYPE_OF_LINE,
            LEGACY_HSL_MUNICIPALITY_CODE);

    private final LineExportProcessor processor = new LineExportProcessor();

    @Nested
    @DisplayName("Transform exported line into Jore 4 format")
    class TransformExportedLineIntoJore4Format {

        @Test
        @DisplayName("Should return a line with generated id")
        void shouldReturnLineWithGeneratedId() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.lineId()).isNotNull();
        }

        @Test
        @DisplayName("Should return a line with the correct external ID of line header")
        void shouldReturnLineWithCorrectExternalIdOfLineHeader() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.externalIdOfLineHeader()).isEqualTo(EXTERNAL_ID_OF_LINE_HEADER);
        }

        @Test
        @DisplayName("Should return a line with the correct label")
        void shouldReturnLineWithCorrectLabel() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.label()).isEqualTo(LINE_NUMBER);
        }

        @Test
        @DisplayName("Should return a line with the correct Finnish name")
        void shouldReturnLineWithCorrectFinnishName() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            final String finnishName = JoreLocaleUtil.getI18nString(line.name(), JoreLocaleUtil.FINNISH);
            assertThat(finnishName).isEqualTo(FINNISH_NAME);
        }

        @Test
        @DisplayName("Should return a line with the correct Swedish name")
        void shouldReturnLineWithCorrectSwedishName() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            final String swedishName = JoreLocaleUtil.getI18nString(line.name(), JoreLocaleUtil.SWEDISH);
            assertThat(swedishName).isEqualTo(SWEDISH_NAME);
        }

        @Test
        @DisplayName("Should return a line with the correct Finnish short name")
        void shouldReturnLineWithCorrectFinnishShortName() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            final String finnishName = JoreLocaleUtil.getI18nString(line.shortName(), JoreLocaleUtil.FINNISH);
            assertThat(finnishName).isEqualTo(FINNISH_SHORT_NAME);
        }

        @Test
        @DisplayName("Should return a line with the correct Swedish short name")
        void shouldReturnLineWithCorrectSwedishShortName() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            final String swedishName = JoreLocaleUtil.getI18nString(line.shortName(), JoreLocaleUtil.SWEDISH);
            assertThat(swedishName).isEqualTo(SWEDISH_SHORT_NAME);
        }

        @Test
        @DisplayName("Should return a line with the correct primary vehicle mode")
        void shouldReturnLineWithCorrectPrimaryVehicleMode() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.primaryVehicleMode()).isEqualTo(EXPECTED_PRIMARY_VEHICLE_MODE);
        }

        @Test
        @DisplayName("Should return a line that has validity period start time")
        void shouldReturnLineThatHasValidityPeriodStartTime() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.validityStart()).isNotEmpty();
        }

        @Test
        @DisplayName("Should return a line that has the correct validity period start time")
        void shouldReturnLineThatHasCorrectValidityPeriodStartTime() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.validityStart()).contains(VALID_DATE_RANGE_START);
        }

        @Test
        @DisplayName("Should return a line that has validity period end time")
        void shouldReturnLineThatHasValidityPeriodEndTime() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.validityEnd()).isNotEmpty();
        }

        @Test
        @DisplayName("Should return a line that has the correct validity period end time")
        void shouldReturnLineThatHasCorrectValidityPeriodEndTime() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.validityEnd()).contains(VALID_DATE_RANGE_END);
        }

        @Test
        @DisplayName("Should return a line with the correct priority")
        void shouldReturnLineWithCorrectPriority() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.priority()).isEqualTo(EXPECTED_PRIORITY);
        }

        @Test
        @DisplayName("Should return a line with the correct legacy HSL municipality code")
        void shouldReturnLineWithCorrectLegacyHslMunicipalityCode() throws Exception {
            final Jore4Line line = processor.process(INPUT);
            assertThat(line.legacyHslMunicipalityCode()).isEqualTo(LEGACY_HSL_MUNICIPALITY_CODE);
        }
    }
}
