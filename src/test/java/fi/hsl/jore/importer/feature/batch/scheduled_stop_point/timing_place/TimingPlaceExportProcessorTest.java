package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4TimingPlace;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.timing_place.ImporterTimingPlace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimingPlaceExportProcessorTest {

    private static final ImporterTimingPlace INPUT = ImporterTimingPlace.of("1ELIEL", "Elielinaukio");

    private static final TimingPlaceExportProcessor INSTANCE = new TimingPlaceExportProcessor();

    @Test
    @DisplayName("Should return a timing place with a generated ID")
    void shouldReturnScheduledStopPointWithGeneratedId() throws Exception {
        final Jore4TimingPlace output = INSTANCE.process(INPUT);
        assertThat(output.timingPlaceId()).isNotNull();
    }

    @Test
    @DisplayName("Should return correct timing place label")
    public void shouldReturnCorrectTimingPlaceLabel() throws Exception {
        final Jore4TimingPlace output = INSTANCE.process(INPUT);

        assertThat(output).isNotNull();
        assertThat(output.timingPlaceLabel()).isEqualTo(INPUT.timingPlaceLabel());
    }

    @Test
    @DisplayName("Should return correct timing place name")
    public void shouldReturnCorrectTimingPlaceName() throws Exception {
        final Jore4TimingPlace output = INSTANCE.process(INPUT);

        assertThat(output).isNotNull();
        assertThat(output.timingPlaceName())
                .isEqualTo(MultilingualString
                        .empty()
                        .with(JoreLocaleUtil.FINNISH, INPUT.timingPlaceName()));
    }
}
