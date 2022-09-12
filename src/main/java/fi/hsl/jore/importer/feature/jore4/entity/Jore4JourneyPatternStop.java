package fi.hsl.jore.importer.feature.jore4.entity;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.UUID;

/**
 * Contains the information of a journey pattern stop which can
 * inserted into the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface Jore4JourneyPatternStop {

    boolean isTimingPoint();

    boolean isViaPoint();

    UUID journeyPatternId();

    String scheduledStopPointLabel();

    int scheduledStopPointSequence();

    Optional<MultilingualString> viaPointNames();

    static Jore4JourneyPatternStop of(final boolean isTimingPoint,
                                      final boolean isViaPoint,
        final Optional<MultilingualString> viaPointNames,
                                      final UUID journeyPatternId,
                                      final String scheduledStopPointLabel,
                                      final int scheduledStopPointSequence) {
        return ImmutableJore4JourneyPatternStop.builder()
                .isTimingPoint(isTimingPoint)
                .isViaPoint(isViaPoint)
                .journeyPatternId(journeyPatternId)
                .scheduledStopPointLabel(scheduledStopPointLabel)
                .scheduledStopPointSequence(scheduledStopPointSequence)
                .viaPointNames(viaPointNames)
                .build();
    }
}
