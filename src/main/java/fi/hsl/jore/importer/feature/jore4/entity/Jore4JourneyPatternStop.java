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

    UUID journeyPatternId();

    int scheduledStopPointSequence();

    String scheduledStopPointLabel();

    boolean isUsedAsTimingPoint();

    boolean isViaPoint();

    Optional<MultilingualString> viaPointNames();

    static Jore4JourneyPatternStop of(final UUID journeyPatternId,
                                      final int scheduledStopPointSequence,
                                      final String scheduledStopPointLabel,
                                      final boolean isUsedAsTimingPoint,
                                      final boolean isViaPoint,
                                      final Optional<MultilingualString> viaPointNames) {

        return ImmutableJore4JourneyPatternStop
                .builder()
                .journeyPatternId(journeyPatternId)
                .scheduledStopPointSequence(scheduledStopPointSequence)
                .scheduledStopPointLabel(scheduledStopPointLabel)
                .isUsedAsTimingPoint(isUsedAsTimingPoint)
                .isViaPoint(isViaPoint)
                .viaPointNames(viaPointNames)
                .build();
    }
}
