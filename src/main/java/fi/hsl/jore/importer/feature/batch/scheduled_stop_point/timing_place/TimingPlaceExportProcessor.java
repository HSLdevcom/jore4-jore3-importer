package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4TimingPlace;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.timing_place.ImporterTimingPlace;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class TimingPlaceExportProcessor
        implements ItemProcessor<ImporterTimingPlace, Jore4TimingPlace> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimingPlaceExportProcessor.class);

    @Override
    public Jore4TimingPlace process(final ImporterTimingPlace timingPlace) throws Exception {
        LOGGER.debug("Processing timing place input: {}", timingPlace);

        final Jore4TimingPlace jore4TimingPlace =
                Jore4TimingPlace.of(
                        UUID.randomUUID(),
                        timingPlace.timingPlaceLabel(),
                        // Jore3 does not have localised timing place names.
                        MultilingualString.empty()
                                .with(JoreLocaleUtil.FINNISH, timingPlace.timingPlaceName()));

        LOGGER.debug("Created timing place output: {}", jore4TimingPlace);

        return jore4TimingPlace;
    }
}
