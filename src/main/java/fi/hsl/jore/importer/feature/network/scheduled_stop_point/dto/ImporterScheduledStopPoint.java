package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import io.vavr.collection.List;
import java.util.Optional;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

/**
 * Contains the information of a scheduled stop point which is required when we want to export scheduled stop points to
 * the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface ImporterScheduledStopPoint {

    /**
     * Exported scheduled stop points have multiple ely numbers and external ids because the Jore 3 database can contain
     * multiple instances of the same stop. When we transfer this information to the Jore 4 database, importer must
     * filter duplicate rows and ensure that only one stop is inserted into the Jore 4 database.
     *
     * <p>These lists (elyNumbers and externalIds) contain ely number and external id pairs (list items which have the
     * same index number are a pair) which refer to a single row found from the Jore 4 database. When the importer
     * processes exported scheduled stop points, it exports the scheduled stop point which is found from the Digiroad
     * data and inserts its ely number and external id into the Jore 4 database.
     */
    List<Long> elyNumbers();

    List<ExternalId> externalIds();

    Point location();

    MultilingualString name();

    Optional<String> shortId();

    Optional<String> placeExternalId();

    static ImmutableImporterScheduledStopPoint of(
            final List<ExternalId> externalIds,
            final List<Long> elynumbers,
            final Point location,
            final MultilingualString name,
            final Optional<String> shortId,
            final Optional<String> placeExternalId) {
        return ImmutableImporterScheduledStopPoint.builder()
                .externalIds(externalIds)
                .elyNumbers(elynumbers)
                .location(location)
                .name(name)
                .shortId(shortId)
                .placeExternalId(placeExternalId)
                .build();
    }
}
