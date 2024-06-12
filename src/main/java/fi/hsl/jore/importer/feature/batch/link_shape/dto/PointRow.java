package fi.hsl.jore.importer.feature.batch.link_shape.dto;

import fi.hsl.jore.importer.feature.jore3.entity.JrPoint;
import org.immutables.value.Value;

@Value.Immutable
public interface PointRow {
    JrPoint point();

    // Note that the endpoints will be the same for each point on the same link
    LinkEndpoints endpoints();

    static PointRow of(final JrPoint point, final LinkEndpoints endpoints) {
        return ImmutablePointRow.builder().point(point).endpoints(endpoints).build();
    }
}
