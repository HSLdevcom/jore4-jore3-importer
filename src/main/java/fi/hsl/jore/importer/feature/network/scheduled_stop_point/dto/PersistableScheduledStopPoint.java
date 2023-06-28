package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface PersistableScheduledStopPoint
        extends CommonFields<PersistableScheduledStopPoint>
{

    static PersistableScheduledStopPoint of(final ExternalId externalId,
                                            final Optional<Long> elyNumber,
                                            final MultilingualString name,
                                            final String placeExternalId) {
        return ImmutablePersistableScheduledStopPoint.builder()
                .externalId(externalId)
                .elyNumber(elyNumber)
                .name(name)
                .placeExternalId(placeExternalId)
                .build();
    }
}
