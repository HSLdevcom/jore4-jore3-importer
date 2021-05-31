package fi.hsl.jore.importer.feature.batch.route.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.route.dto.ImportableRoute;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;

public interface IRouteImportRepository extends IImportRepository<ImportableRoute, RoutePK> {
}
