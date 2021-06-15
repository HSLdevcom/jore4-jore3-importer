package fi.hsl.jore.importer.feature.batch.route_link.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImportableRoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.generated.RoutePointPK;

public interface IRoutePointImportRepository extends IImportRepository<ImportableRoutePoint, RoutePointPK> {
}
