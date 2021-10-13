package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

@Value.Immutable
public interface PersistableScheduledStopPoint
        extends CommonFields<PersistableScheduledStopPoint>
{

    static PersistableScheduledStopPoint of(final ExternalId externalId,
                                            final MultilingualString name) {
        return ImmutablePersistableScheduledStopPoint.builder()
                .externalId(externalId)
                .name(name)
                .build();
    }
}
