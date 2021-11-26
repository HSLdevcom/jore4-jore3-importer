package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.line.dto.ExportableLine;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;
import fi.hsl.jore.importer.feature.transmodel.entity.VehicleMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LineExportProcessorTest {

    private static final String FINNISH_NAME = "Vantaanportti-Lentoasema-Kerava";
    private static final String FINNISH_SHORT_NAME = "Vantaanp-Kerava";
    private static final String SWEDISH_NAME = "Vandaporten-Flygstationen-Kervo";
    private static final String SWEDISH_SHORT_NAME = "Vandap-Kervo";

    private static final NetworkType NETWORK_TYPE_ROAD = NetworkType.ROAD;
    private static final VehicleMode EXPECTED_PRIMARY_VEHICLE_MODE = VehicleMode.BUS;

    private static final ExportableLine INPUT = ExportableLine.of(
            JoreLocaleUtil.createMultilingualString(FINNISH_NAME, SWEDISH_NAME),
            NETWORK_TYPE_ROAD,
            JoreLocaleUtil.createMultilingualString(FINNISH_SHORT_NAME, SWEDISH_SHORT_NAME)
    );

    private final LineExportProcessor processor = new LineExportProcessor();


    @Nested
    @DisplayName("Transform exported line into Jore 4 format")
    class TransformExportedLineIntoJore4Format {

        @Test
        @DisplayName("Should return a line with the correct Finnish name")
        void shouldReturnLineWithCorrectFinnishName() throws Exception {
            final TransmodelLine line = processor.process(INPUT);
            final String finnishName = JoreLocaleUtil.getI18nString(line.name(), JoreLocaleUtil.FINNISH);
            assertThat(finnishName).isEqualTo(FINNISH_NAME);
        }

        @Test
        @DisplayName("Should return a line with the correct Swedish name")
        void shouldReturnLineWithCorrectSwedishName() throws Exception {
            final TransmodelLine line = processor.process(INPUT);
            final String swedishName = JoreLocaleUtil.getI18nString(line.name(), JoreLocaleUtil.SWEDISH);
            assertThat(swedishName).isEqualTo(SWEDISH_NAME);
        }

        @Test
        @DisplayName("Should return a line with the correct Finnish short name")
        void shouldReturnLineWithCorrectFinnishShortName() throws Exception {
            final TransmodelLine line = processor.process(INPUT);
            final String finnishName = JoreLocaleUtil.getI18nString(line.shortName(), JoreLocaleUtil.FINNISH);
            assertThat(finnishName).isEqualTo(FINNISH_SHORT_NAME);
        }

        @Test
        @DisplayName("Should return a line with the correct Swedish short name")
        void shouldReturnLineWithCorrectSwedishShortName() throws Exception {
            final TransmodelLine line = processor.process(INPUT);
            final String swedishName = JoreLocaleUtil.getI18nString(line.shortName(), JoreLocaleUtil.SWEDISH);
            assertThat(swedishName).isEqualTo(SWEDISH_SHORT_NAME);
        }

        @Test
        @DisplayName("Should return a line with the correct primary vehicle mode")
        void shouldReturnLineWithCorrectPrimaryVehicleMode() throws Exception {
            final TransmodelLine line = processor.process(INPUT);
            assertThat(line.primaryVehicleMode()).isEqualTo(EXPECTED_PRIMARY_VEHICLE_MODE);
        }
    }
}
