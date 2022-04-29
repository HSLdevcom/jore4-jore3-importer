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

    String digiroadStopId();

    String digiroadLinkId();

    long nationalId();

    Point location();

    Optional<String> nameFinnish();

    Optional<String> nameSwedish();

    static ImmutableDigiroadStop of (String digiroadStopId,
                                     String digiroadLinkId,
                                     DigiroadStopDirection directionOnInfralink,
                                     long nationalId,
                                     Point location,
                                     Optional<String> nameFinnish,
                                     Optional<String> nameSwedish) {
        return ImmutableDigiroadStop.builder()
                .directionOnInfraLink(directionOnInfralink)
                .digiroadStopId(digiroadStopId)
                .digiroadLinkId(digiroadLinkId)
                .nationalId(nationalId)
                .location(location)
                .nameFinnish(nameFinnish)
                .nameSwedish(nameSwedish)
                .build();
    }
}
