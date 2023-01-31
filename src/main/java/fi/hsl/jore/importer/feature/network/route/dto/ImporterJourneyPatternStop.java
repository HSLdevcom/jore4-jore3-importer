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
public interface ImporterJourneyPatternStop {

    boolean isHastusPoint();

    boolean isViaPoint();

    UUID journeyPatternJore4Id();

    int orderNumber();

    String scheduledStopPointJore4Label();

    Optional<MultilingualString> viaPointNames();

    static ImporterJourneyPatternStop of(final boolean isHastusPoint,
                                         final boolean isViaPoint,
                                         final Optional<MultilingualString> viaPointNames,
                                         final UUID journeyPatternJore4Id,
                                         final int orderNumber,
                                         final String scheduledStopPointJore4Label) {
        return ImmutableImporterJourneyPatternStop.builder()
                .isHastusPoint(isHastusPoint)
                .isViaPoint(isViaPoint)
                .viaPointNames(viaPointNames)
                .journeyPatternJore4Id(journeyPatternJore4Id)
                .orderNumber(orderNumber)
                .scheduledStopPointJore4Label(scheduledStopPointJore4Label)
                .build();
    }
}
