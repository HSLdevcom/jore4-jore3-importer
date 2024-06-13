package fi.hsl.jore.importer.feature.batch.link_shape.dto;

import com.google.common.base.Preconditions;
import fi.hsl.jore.importer.feature.jore3.entity.JrPoint;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface LinkPoints {

    JrLinkPk link();

    LinkEndpoints endpoints();

    List<JrPoint> points();

    @Value.Check
    default void checkLinkPoints() {
        Preconditions.checkState(!points().isEmpty(), "LinkPoints should always contain at least one point!");
    }

    static LinkPoints of(final JrLinkPk link, final LinkEndpoints endpoints, final List<JrPoint> points) {
        return ImmutableLinkPoints.builder()
                .link(link)
                .endpoints(endpoints)
                .points(points)
                .build();
    }
}
