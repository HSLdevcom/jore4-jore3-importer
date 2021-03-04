package fi.hsl.jore.importer.feature.batch.link_context;

import fi.hsl.jore.importer.feature.jore.entity.JrLink;
import fi.hsl.jore.importer.feature.jore.entity.JrNode;
import fi.hsl.jore.importer.feature.jore.style.JoreDtoStyle;
import fi.hsl.jore.importer.feature.util.GeometryUtil;
import org.immutables.value.Value;
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

    default LineString geometry() {
        return GeometryUtil.toLineString(from().location(),
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
