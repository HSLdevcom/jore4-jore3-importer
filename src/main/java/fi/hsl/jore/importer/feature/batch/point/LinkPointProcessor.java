package fi.hsl.jore.importer.feature.batch.point;

import fi.hsl.jore.importer.feature.batch.link.support.TransitTypeToNetworkTypeMapper;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkEndpoints;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkPoints;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.entity.JrPoint;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.collection.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.batch.item.ItemProcessor;

public class LinkPointProcessor implements ItemProcessor<LinkPoints, LinkGeometry> {

    private static LineString geometry(final LinkEndpoints endpoints,
                                       final List<JrPoint> items) {
        final int srid = items.get(0).location().getSRID();
        final List<Coordinate> coordinates = List.ofAll(items.map(JrPoint::location))
                                                 // Add the start point to the beginning
                                                 .prepend(endpoints.startLocation())
                                                 // Add the end point to the end
                                                 .append(endpoints.endLocation())
                                                 .map(Point::getCoordinate);
        return GeometryUtil.toLineString(srid, coordinates);
    }

    @Override
    public LinkGeometry process(final LinkPoints linkPoints) {
        final JrLinkPk parentLink = linkPoints.link();
        final ExternalId externalId = ExternalIdUtil.forLink(parentLink);
        final NetworkType networkType = TransitTypeToNetworkTypeMapper.resolveNetworkType(parentLink.transitType());
        return LinkGeometry.of(externalId,
                               networkType,
                               geometry(linkPoints.endpoints(),
                                        linkPoints.points()));
    }
}
