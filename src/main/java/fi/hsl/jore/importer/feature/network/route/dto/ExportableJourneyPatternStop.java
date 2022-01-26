package fi.hsl.jore.importer.feature.network.route.dto;

import org.immutables.value.Value;

import java.util.UUID;

/**
 * Contains the information of a journey pattern's scheduled
 * stop point which is exported to the Jore 4 database.
 */
@Value.Immutable
public interface ExportableJourneyPatternStop {

    boolean isHastusPoint();

    boolean isViaPoint();

    UUID journeyPatternTransmodelId();

    int orderNumber();

    UUID scheduledStopPointTransmodelId();

    static ExportableJourneyPatternStop of(final boolean isHastusPoint,
                                           final boolean isViaPoint,
                                           final UUID journeyPatternTransmodelId,
                                           final int orderNumber,
                                           final UUID scheduledStopPointTransmodelId) {
        return ImmutableExportableJourneyPatternStop.builder()
                .isHastusPoint(isHastusPoint)
                .isViaPoint(isViaPoint)
                .journeyPatternTransmodelId(journeyPatternTransmodelId)
                .orderNumber(orderNumber)
                .scheduledStopPointTransmodelId(scheduledStopPointTransmodelId)
                .build();
    }
}
