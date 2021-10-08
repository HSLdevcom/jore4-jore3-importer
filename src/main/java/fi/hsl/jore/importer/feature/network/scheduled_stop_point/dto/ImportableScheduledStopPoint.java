package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

/**
 * Contains the information of a one scheduled stop point which can
 * imported into the target database.
 */
@Value.Immutable
public interface ImportableScheduledStopPoint
        extends CommonFields<ImportableScheduledStopPoint>
{

    static ImportableScheduledStopPoint of(final ExternalId externalId,
                                           final Point location,
                                           final MultilingualString name) {
        return ImmutableImportableScheduledStopPoint.builder()
                .externalId(externalId)
                .location(location)
                .name(name)
                .build();
    }
}
