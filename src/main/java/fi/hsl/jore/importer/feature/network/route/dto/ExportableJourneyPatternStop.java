package fi.hsl.jore.importer.feature.network.route.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.immutables.value.Value;

import java.util.Optional;
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

    String scheduledStopPointTransmodelLabel();

    Optional<MultilingualString> viaPointNames();

    static ExportableJourneyPatternStop of(final boolean isHastusPoint,
                                           final boolean isViaPoint,
                                           final Optional<MultilingualString> viaPointNames,
                                           final UUID journeyPatternTransmodelId,
                                           final int orderNumber,
                                           final String scheduledStopPointTransmodelLabel) {
        return ImmutableExportableJourneyPatternStop.builder()
                .isHastusPoint(isHastusPoint)
                .isViaPoint(isViaPoint)
                .viaPointNames(viaPointNames)
                .journeyPatternTransmodelId(journeyPatternTransmodelId)
                .orderNumber(orderNumber)
                .scheduledStopPointTransmodelLabel(scheduledStopPointTransmodelLabel)
                .build();
    }
}
