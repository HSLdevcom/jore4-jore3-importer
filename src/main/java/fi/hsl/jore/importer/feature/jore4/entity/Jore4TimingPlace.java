package fi.hsl.jore.importer.feature.jore4.entity;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains information of a timing place which can be written to the Jore 4
 * database.
 */
@Value.Immutable
public interface Jore4TimingPlace {

    UUID timingPlaceId();

    String timingPlaceLabel();

    MultilingualString timingPlaceName();

    static ImmutableJore4TimingPlace of(final UUID timingPlaceId,
                                        final String timingPlaceLabel,
                                        final MultilingualString timingPlaceName) {

        return ImmutableJore4TimingPlace.builder()
                .timingPlaceId(timingPlaceId)
                .timingPlaceLabel(timingPlaceLabel)
                .timingPlaceName(timingPlaceName)
                .build();
    }
}
