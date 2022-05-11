package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import io.vavr.collection.List;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

/**
 * Contains the information of a scheduled stop point which is
 * required when we want to export scheduled stop points to the
 * Jore 4 transmodel schema.
 */
@Value.Immutable
public interface ExportableScheduledStopPoint {

    List<Long> elyNumber();

    List<ExternalId> externalId();

    Point location();

    MultilingualString name();

    Optional<String> shortId();

    static ImmutableExportableScheduledStopPoint of(final List<ExternalId> externalId,
                                                    final List<Long> elynumber,
                                                    final Point location,
                                                    final MultilingualString name,
                                                    final Optional<String> shortId) {
        return ImmutableExportableScheduledStopPoint.builder()
                .externalId(externalId)
                .elyNumber(elynumber)
                .location(location)
                .name(name)
                .shortId(shortId)
                .build();
    }
}
