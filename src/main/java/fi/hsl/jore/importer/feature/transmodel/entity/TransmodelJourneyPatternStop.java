package fi.hsl.jore.importer.feature.transmodel.entity;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information of a journey pattern stop which can
 * inserted into the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface TransmodelJourneyPatternStop {

    boolean isTimingPoint();

    boolean isViaPoint();

    UUID journeyPatternId();

    String scheduledStopPointLabel();

    int scheduledStopPointSequence();

    static TransmodelJourneyPatternStop of(final boolean isTimingPoint,
                                           final boolean isViaPoint,
                                           final UUID journeyPatternId,
                                           final String scheduledStopPointLabel,
                                           final int scheduledStopPointSequence) {
        return ImmutableTransmodelJourneyPatternStop.builder()
                .isTimingPoint(isTimingPoint)
                .isViaPoint(isViaPoint)
                .journeyPatternId(journeyPatternId)
                .scheduledStopPointLabel(scheduledStopPointLabel)
                .scheduledStopPointSequence(scheduledStopPointSequence)
                .build();
    }
}
