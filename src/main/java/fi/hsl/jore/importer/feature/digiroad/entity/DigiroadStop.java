package fi.hsl.jore.importer.feature.digiroad.entity;

import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

/**
 * Contains the information of a stop point which is read from
 * the data imported from Digiroad.
 */
@Value.Immutable
public interface DigiroadStop {

    DigiroadStopDirection directionOnInfraLink();

    String externalStopId();

    String externalLinkId();

    String elyNumber();

    Point location();

    Optional<String> nameFinnish();

    Optional<String> nameSwedish();

    static ImmutableDigiroadStop of (String externalStopId,
                                     String externalLinkId,
                                     DigiroadStopDirection directionOnInfralink,
                                     String elyNumber,
                                     Point location,
                                     Optional<String> nameFinnish,
                                     Optional<String> nameSwedish) {
        return ImmutableDigiroadStop.builder()
                .directionOnInfraLink(directionOnInfralink)
                .externalStopId(externalStopId)
                .externalLinkId(externalLinkId)
                .elyNumber(elyNumber)
                .location(location)
                .nameFinnish(nameFinnish)
                .nameSwedish(nameSwedish)
                .build();
    }
}
