package fi.hsl.jore.importer.feature.batch.route_link.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.route_point.dto.Jore3RoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.generated.RoutePointPK;

public interface IRoutePointImportRepository
        extends IImportRepository<Jore3RoutePoint, RoutePointPK> {}
