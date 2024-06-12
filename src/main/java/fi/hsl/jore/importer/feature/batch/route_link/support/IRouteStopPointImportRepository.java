package fi.hsl.jore.importer.feature.batch.route_link.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.Jore3RouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.generated.RouteStopPointPK;

public interface IRouteStopPointImportRepository extends IImportRepository<Jore3RouteStopPoint, RouteStopPointPK> {}
