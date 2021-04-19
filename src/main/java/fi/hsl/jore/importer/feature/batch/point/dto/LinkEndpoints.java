package fi.hsl.jore.importer.feature.batch.point.dto;

import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

@Value.Immutable
public interface LinkEndpoints {

    double linkStartLatitude();

    double linkStartLongitude();

    double linkEndLatitude();

    double linkEndLongitude();

    default Point startLocation() {
        return JoreGeometryUtil.fromDbCoordinates(linkStartLatitude(), linkStartLongitude());
    }

    default Point endLocation() {
        return JoreGeometryUtil.fromDbCoordinates(linkEndLatitude(), linkEndLongitude());
    }

    static LinkEndpoints of(final double linkStartLatitude,
                            final double linkStartLongitude,
                            final double linkEndLatitude,
                            final double linkEndLongitude) {
        return ImmutableLinkEndpoints.builder()
                                     .linkStartLatitude(linkStartLatitude)
                                     .linkStartLongitude(linkStartLongitude)
                                     .linkEndLatitude(linkEndLatitude)
                                     .linkEndLongitude(linkEndLongitude)
                                     .build();
    }
}
