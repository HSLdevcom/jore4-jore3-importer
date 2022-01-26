package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.network.route.dto.ExportableJourneyPatternStop;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPatternStop;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JourneyPatternStopExportProcessorTest {

    private static final boolean IS_HASTUS_POINT = true;
    private static final boolean IS_VIA_POINT = true;
    private static final UUID JOURNEY_PATTERN_TRANSMODEL_ID = UUID.fromString("55171cba-d8b7-4d6a-bbd4-cec0501c88f8");
    private static final int ORDER_NUMBER = 1;
    private static final UUID SCHEDULED_STOP_POINT_TRANSMODEL_ID = UUID.fromString("2ba4162a-d30d-49ec-9ec7-9317be3083ca");

    private static final ExportableJourneyPatternStop INPUT = ExportableJourneyPatternStop.of(
            IS_HASTUS_POINT,
            IS_VIA_POINT,
            JOURNEY_PATTERN_TRANSMODEL_ID,
            ORDER_NUMBER,
            SCHEDULED_STOP_POINT_TRANSMODEL_ID
    );

    private final JourneyPatternStopExportProcessor processor = new JourneyPatternStopExportProcessor();

    @Test
    @DisplayName("Should return a journey pattern stop with the correct journey pattern id")
    void shouldReturnJourneyPatternStopWithCorrectJourneyPatternId() throws Exception {
        final TransmodelJourneyPatternStop output = processor.process(INPUT);
        assertThat(output.journeyPatternId()).isEqualTo(JOURNEY_PATTERN_TRANSMODEL_ID);
    }

    @Test
    @DisplayName("Should return a journey pattern stop with the correct scheduled stop point id")
    void shouldReturnJourneyPatternStopWithCorrectScheduledStopPointId() throws Exception {
        final TransmodelJourneyPatternStop output = processor.process(INPUT);
        assertThat(output.scheduledStopPointId()).isEqualTo(SCHEDULED_STOP_POINT_TRANSMODEL_ID);
    }

    @Test
    @DisplayName("Should return a journey pattern stop with the correct scheduled stop point sequence")
    void shouldReturnJourneyPatternStopWithCorrectScheduledStopPointSequence() throws Exception {
        final TransmodelJourneyPatternStop output = processor.process(INPUT);
        assertThat(output.scheduledStopPointSequence()).isEqualTo(ORDER_NUMBER);
    }

    @Test
    @DisplayName("Should return a journey pattern stop with the correct timing point information")
    void shouldReturnJourneyPatternStopWithCorrectTimingPointInformation() throws Exception {
        final TransmodelJourneyPatternStop output = processor.process(INPUT);
        assertThat(output.isTimingPoint()).isEqualTo(IS_HASTUS_POINT);
    }

    @Test
    @DisplayName("Should return a journey pattern stop with the correct via point information")
    void shouldReturnJourneyPatternStopWithCorrectViaPointInformation() throws Exception {
        final TransmodelJourneyPatternStop output = processor.process(INPUT);
        assertThat(output.isViaPoint()).isEqualTo(IS_VIA_POINT);
    }
}
