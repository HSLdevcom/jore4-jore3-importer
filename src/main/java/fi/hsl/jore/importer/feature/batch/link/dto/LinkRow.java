package fi.hsl.jore.importer.feature.batch.link.dto;

import com.google.common.base.Preconditions;
import fi.hsl.jore.importer.feature.jore3.entity.JrLink;
import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import fi.hsl.jore.importer.util.GeometryUtil;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;


/**
 * JrNodes don't know their network (e.g. bus/metro/train), instead this information
 * is stored in the JrLink between nodes. Downstream consumers most likely want to
 * assign nodes to networks
 */
@JoreDtoStyle
@Value.Immutable
public interface LinkRow {

    JrLink link();

    JrNode from();

    JrNode to();

    @Value.Derived
    default int srid() {
        return from().location().getSRID();
    }

    @Value.Check
    default void checkLinkContext() {
        Preconditions.checkState(from().location().getSRID() == from().projectedLocation().getSRID(),
                                 "location and projected location must have same SRID!");
        Preconditions.checkState(from().location().getSRID() == to().location().getSRID(),
                                 "endpoints must have same SRID!");
        Preconditions.checkState(from().projectedLocation().getSRID() == to().projectedLocation().getSRID(),
                                 "projected endpoint locations must have same SRID!");
    }

    default LineString geometry() {
        return GeometryUtil.toLineString(srid(),
                                         from().location(),
                                         to().location());
    }

    static LinkRow of(final JrLink link,
                      final JrNode from,
                      final JrNode to) {
        return ImmutableLinkRow.builder()
                               .link(link)
                               .from(from)
                               .to(to)
                               .build();
    }
}
