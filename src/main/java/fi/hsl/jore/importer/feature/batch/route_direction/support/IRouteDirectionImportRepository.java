package fi.hsl.jore.importer.feature.batch.route_direction.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.route_direction.dto.ImportableRouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;

public interface IRouteDirectionImportRepository extends IImportRepository<ImportableRouteDirection, RouteDirectionPK> {
}
