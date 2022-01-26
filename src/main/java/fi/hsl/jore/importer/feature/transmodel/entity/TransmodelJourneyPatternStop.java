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

    UUID scheduledStopPointId();

    int scheduledStopPointSequence();

    static TransmodelJourneyPatternStop of(final boolean isTimingPoint,
                                           final boolean isViaPoint,
                                           final UUID journeyPatternId,
                                           final UUID scheduledStopPointId,
                                           final int scheduledStopPointSequence) {
        return ImmutableTransmodelJourneyPatternStop.builder()
                .isTimingPoint(isTimingPoint)
                .isViaPoint(isViaPoint)
                .journeyPatternId(journeyPatternId)
                .scheduledStopPointId(scheduledStopPointId)
                .scheduledStopPointSequence(scheduledStopPointSequence)
                .build();
    }
}
