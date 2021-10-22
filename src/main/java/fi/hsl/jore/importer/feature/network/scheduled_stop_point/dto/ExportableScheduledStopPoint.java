package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

/**
 * Contains the information of a scheduled stop point which is
 * required when we want to export scheduled stop points to the
 * Jore 4 transmodel schema.
 */
@Value.Immutable
public interface ExportableScheduledStopPoint extends CommonFields<ExportableScheduledStopPoint> {

    Point location();

    static ImmutableExportableScheduledStopPoint of(final ExternalId externalId,
                                                    final Optional<String> elynumber,
                                                    final Point location,
                                                    final MultilingualString name) {
        return ImmutableExportableScheduledStopPoint.builder()
                .externalId(externalId)
                .elyNumber(elynumber)
                .location(location)
                .name(name)
                .build();
    }
}
