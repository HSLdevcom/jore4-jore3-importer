package fi.hsl.jore.importer.feature.transmodel.entity;

import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

/**
 * Contains the information of a scheduled stop point which
 * can be written to the Jore 4 transmodel schema.
 */
@Value.Immutable
public interface TransmodelScheduledStopPoint {

    String externalInfrastructureLinkId();

    String externalScheduledStopPointId();

    TransmodelScheduledStopPointDirection directionOnInfraLink();

    String label();

    Point measuredLocation();

    static ImmutableTransmodelScheduledStopPoint of (final String externalScheduledStopPointId,
                                                     final String externalInfrastructureLinkId,
                                                     final TransmodelScheduledStopPointDirection directionOnInfraLink,
                                                     final String label,
                                                     final Point measuredLocation) {
        return ImmutableTransmodelScheduledStopPoint.builder()
                .externalScheduledStopPointId(externalScheduledStopPointId)
                .externalInfrastructureLinkId(externalInfrastructureLinkId)
                .directionOnInfraLink(directionOnInfraLink)
                .label(label)
                .measuredLocation(measuredLocation)
                .build();
    }
}
