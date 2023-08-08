package fi.hsl.jore.importer.feature.network.route.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.enumerated.RegulatedTimingPointStatus;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.UUID;

/**
 * Contains the information of a journey pattern's scheduled stop point which is
 * exported to the Jore 4 database.
 */
@Value.Immutable
public interface ImporterJourneyPatternStop {

    UUID journeyPatternJore4Id();

    String routeDirectionJore3Id();

    int orderNumber();

    String scheduledStopPointJore4Label();

    boolean isUsedAsTimingPoint();

    Optional<String> timingPlaceLabel();

    RegulatedTimingPointStatus regulatedTimingPointStatus();

    boolean isViaPoint();

    Optional<MultilingualString> viaPointNames();

    static ImporterJourneyPatternStop of(final UUID journeyPatternJore4Id,
                                         final String routeDirectionJore3Id,
                                         final int orderNumber,
                                         final String scheduledStopPointJore4Label,
                                         final boolean isUsedAsTimingPoint,
                                         final Optional<String> timingPlaceLabel,
                                         final RegulatedTimingPointStatus regulatedTimingPointStatus,
                                         final boolean isViaPoint,
                                         final Optional<MultilingualString> viaPointNames) {
        return ImmutableImporterJourneyPatternStop
                .builder()
                .journeyPatternJore4Id(journeyPatternJore4Id)
                .routeDirectionJore3Id(routeDirectionJore3Id)
                .orderNumber(orderNumber)
                .scheduledStopPointJore4Label(scheduledStopPointJore4Label)
                .isUsedAsTimingPoint(isUsedAsTimingPoint)
                .timingPlaceLabel(timingPlaceLabel)
                .regulatedTimingPointStatus(regulatedTimingPointStatus)
                .isViaPoint(isViaPoint)
                .viaPointNames(viaPointNames)
                .build();
    }
}
