package fi.hsl.jore.importer.feature.batch.point.dto;

import com.google.common.base.Preconditions;
import fi.hsl.jore.importer.feature.jore.entity.JrPoint;
import fi.hsl.jore.importer.feature.jore.field.TransitType;
import fi.hsl.jore.importer.feature.jore.key.JrLinkPk;
import io.vavr.collection.List;
import org.immutables.value.Value;

@Value.Immutable
public interface LinkPoints {

    JrLinkPk link();

    LinkEndpoints endpoints();

    List<JrPoint> points();

    @Value.Derived
    default TransitType transitType() {
        return points().get(0).transitType();
    }

    @Value.Check
    default void checkLinkPoints() {
        Preconditions.checkState(!points().isEmpty(),
                                 "LinkPoints should always contain at least one point!");
    }

    static LinkPoints of(final JrLinkPk link,
                         final LinkEndpoints endpoints,
                         final List<JrPoint> points) {
        return ImmutableLinkPoints.builder()
                                  .link(link)
                                  .endpoints(endpoints)
                                  .points(points)
                                  .build();
    }
}
