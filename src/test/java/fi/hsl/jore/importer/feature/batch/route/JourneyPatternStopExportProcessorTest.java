package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPatternStop;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPatternStop;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JourneyPatternStopExportProcessorTest {

    private static final UUID JOURNEY_PATTERN_JORE4_ID = UUID.fromString("55171cba-d8b7-4d6a-bbd4-cec0501c88f8");
    private static final String ROUTE_DIRECTION_JORE3_ID = "1001-2-20211004";
    private static final int ORDER_NUMBER = 1;
    private static final String SCHEDULED_STOP_POINT_LABEL = "stop1";
    private static final boolean IS_USED_AS_TIMING_POINT = true;
    private static final String TIMING_PLACE_ID = "1ELIEL";
    private static final boolean IS_VIA_POINT = true;
    private static final Map<Locale, String> VIA_POINT_NAME_MAP = Map.of(
            JoreLocaleUtil.FINNISH, "ViaSuomi",
            JoreLocaleUtil.SWEDISH, "ViaSverige"
    );
    private static final Optional<MultilingualString> VIA_POINT_NAMES = Optional.of(MultilingualString.ofLocaleMap(VIA_POINT_NAME_MAP));

    private static final ImporterJourneyPatternStop DEFAULT_INPUT = ImporterJourneyPatternStop.of(
            JOURNEY_PATTERN_JORE4_ID,
            ROUTE_DIRECTION_JORE3_ID,
            ORDER_NUMBER,
            SCHEDULED_STOP_POINT_LABEL,
            IS_USED_AS_TIMING_POINT,
            Optional.of(TIMING_PLACE_ID),
            IS_VIA_POINT,
            VIA_POINT_NAMES
    );

    private final JourneyPatternStopExportProcessor processor = new JourneyPatternStopExportProcessor();

    @Test
    @DisplayName("Should return a journey pattern stop with the correct journey pattern id")
    void shouldReturnJourneyPatternStopWithCorrectJourneyPatternId() throws Exception {
        final Jore4JourneyPatternStop output = processor.process(DEFAULT_INPUT);
        assertThat(output.journeyPatternId()).isEqualTo(JOURNEY_PATTERN_JORE4_ID);
    }

    @Test
    @DisplayName("Should return a journey pattern stop with the correct scheduled stop point sequence")
    void shouldReturnJourneyPatternStopWithCorrectScheduledStopPointSequence() throws Exception {
        final Jore4JourneyPatternStop output = processor.process(DEFAULT_INPUT);
        assertThat(output.scheduledStopPointSequence()).isEqualTo(ORDER_NUMBER);
    }

    @Test
    @DisplayName("Should return a journey pattern stop with the correct scheduled stop point id")
    void shouldReturnJourneyPatternStopWithCorrectScheduledStopPointId() throws Exception {
        final Jore4JourneyPatternStop output = processor.process(DEFAULT_INPUT);
        assertThat(output.scheduledStopPointLabel()).isEqualTo(SCHEDULED_STOP_POINT_LABEL);
    }

    @Test
    @DisplayName("Should return a journey pattern stop with the correct is-used-as-timing-point information")
    void shouldReturnJourneyPatternStopWithCorrectIsUsedAsTimingPointInformation() throws Exception {
        final Jore4JourneyPatternStop output = processor.process(DEFAULT_INPUT);
        assertThat(output.isUsedAsTimingPoint()).isEqualTo(IS_USED_AS_TIMING_POINT);
    }

    @Test
    @DisplayName("Should return a journey pattern stop with the correct is-via-point information")
    void shouldReturnJourneyPatternStopWithCorrectIsViaPointInformation() throws Exception {
        final Jore4JourneyPatternStop output = processor.process(DEFAULT_INPUT);
        assertThat(output.isViaPoint()).isEqualTo(IS_VIA_POINT);
    }

    @Test
    @DisplayName("Should return a journey pattern stop with the correct via names")
    void shouldReturnJourneyPatternStopWithCorrectViaPointNames() throws Exception {
        final Jore4JourneyPatternStop output = processor.process(DEFAULT_INPUT);
        assertThat(output.viaPointNames().isPresent()).isTrue();
        final String finnishName = JoreLocaleUtil.getI18nString(output.viaPointNames().get(), JoreLocaleUtil.FINNISH);
        assertThat(finnishName).isEqualTo(VIA_POINT_NAME_MAP.get(JoreLocaleUtil.FINNISH));
        final String swedishName = JoreLocaleUtil.getI18nString(output.viaPointNames().get(), JoreLocaleUtil.SWEDISH);
        assertThat(swedishName).isEqualTo(VIA_POINT_NAME_MAP.get(JoreLocaleUtil.SWEDISH));
    }

    @Test
    @DisplayName("When used as timing point and timing place ID absent")
    void whenUsedAsTimingPointAndTimingPlaceIdAbsent() throws Exception {
        final ImporterJourneyPatternStop INPUT = ImporterJourneyPatternStop.of(
                JOURNEY_PATTERN_JORE4_ID,
                ROUTE_DIRECTION_JORE3_ID,
                ORDER_NUMBER,
                SCHEDULED_STOP_POINT_LABEL,
                true, // isUsedAsTimingPoint
                Optional.empty(), // timing place ID absent
                false,
                Optional.empty()
        );

        final Jore4JourneyPatternStop output = processor.process(INPUT);
        assertThat(output.isUsedAsTimingPoint()).isEqualTo(false);
    }
}
