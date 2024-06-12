package fi.hsl.jore.importer.feature.network.scheduled_stop_point.timing_place;

import org.immutables.value.Value;

/** Source information for timing places to be exported to the Jore 4 database. */
@Value.Immutable
public interface ImporterTimingPlace {

    String timingPlaceLabel();

    String timingPlaceName();

    static ImmutableImporterTimingPlace of(
            final String timingPlaceLabel, final String timingPlaceName) {

        return ImmutableImporterTimingPlace.builder()
                .timingPlaceLabel(timingPlaceLabel)
                .timingPlaceName(timingPlaceName)
                .build();
    }
}
