package fi.hsl.jore.importer.feature.batch.link_context;

import com.google.common.base.Preconditions;
import fi.hsl.jore.importer.feature.jore.entity.JrLink;
import fi.hsl.jore.importer.feature.jore.entity.JrNode;
import fi.hsl.jore.importer.feature.jore.style.JoreDtoStyle;
import fi.hsl.jore.importer.feature.util.GeometryUtil;
import org.immutables.value.Value;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;


/**
 * JrNodes don't know their network (e.g. bus/metro/train), instead this information
 * is stored in the JrLink between nodes. Downstream consumers most likely want to
 * assign nodes to networks
 */
@JoreDtoStyle
@Value.Immutable
public interface LinkContext {

    JrLink link();

    JrNode from();

    JrNode to();

    @Value.Check
    default void checkLinkContext() {
        Preconditions.checkState(from().location().getSRID() == from().projectedLocation().getSRID(),
                                 "location and projected location must have same SRID!");
        Preconditions.checkState(from().location().getSRID() == to().location().getSRID(),
                                 "endpoints must have same SRID!");
        Preconditions.checkState(from().projectedLocation().getSRID() == to().projectedLocation().getSRID(),
                                 "endpoints must have same SRID!");
    }

    default LineString geometry() {
        final GeometryFactory factory = GeometryUtil.FACTORIES_BY_SRID.get(from().location().getSRID())
                                                                      .get();
        return GeometryUtil.toLineString(factory,
                                         from().location(),
                                         from().projectedLocation(),
                                         to().projectedLocation(),
                                         to().location());
    }

    static LinkContext of(final JrLink link,
                          final JrNode from,
                          final JrNode to) {
        return ImmutableLinkContext.builder()
                                   .link(link)
                                   .from(from)
                                   .to(to)
                                   .build();
    }
}
