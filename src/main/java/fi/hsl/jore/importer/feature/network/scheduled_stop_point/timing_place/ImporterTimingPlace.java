package fi.hsl.jore.importer.feature.network.scheduled_stop_point.timing_place;

import org.immutables.value.Value;

/**
 * Contains information about Hastus places which is required when we want to
 * export timing places to the Jore 4 schema.
 */
@Value.Immutable
public interface ImporterTimingPlace {

    String hastusPlaceId();

    static ImmutableImporterTimingPlace of(final String hastusPlaceId) {
        return ImmutableImporterTimingPlace.builder()
                .hastusPlaceId(hastusPlaceId)
                .build();
    }
}
