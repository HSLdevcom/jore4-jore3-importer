package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.UUID;

@Value.Immutable
public interface PersistableScheduledStopPoint
        extends CommonFields<PersistableScheduledStopPoint>
{
    Optional<UUID> placeId();

    static PersistableScheduledStopPoint of(final ExternalId externalId,
                                            final Optional<Long> elyNumber,
                                            final MultilingualString name,
                                            final Optional<UUID> placeId) {
        return ImmutablePersistableScheduledStopPoint.builder()
                .externalId(externalId)
                .elyNumber(elyNumber)
                .name(name)
                .placeId(placeId)
                .build();
    }
}
