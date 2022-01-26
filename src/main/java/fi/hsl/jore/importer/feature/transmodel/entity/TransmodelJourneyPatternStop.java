package fi.hsl.jore.importer.feature.transmodel.entity;

import fi.hsl.jore.importer.feature.network.route.dto.ImmutableExportableJourneyPatternStop;
import org.immutables.value.Value;

/**
 * Contains the information of a journey pattern stop which can
 * inserted into the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface TransmodelJourneyPatternStop {

    boolean isTimingPoint();

    boolean isViaPoint();

    String journeyPatternId();

    String scheduledStopPointId();

    int scheduledStopPointSequence();

    static TransmodelJourneyPatternStop of(final boolean isTimingPoint,
                                           final boolean isViaPoint,
                                           final String journeyPatternId,
                                           final String scheduledStopPointId,
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
