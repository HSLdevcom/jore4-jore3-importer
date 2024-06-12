package fi.hsl.jore.importer.feature.network.route.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.network.route.dto.PersistableRoute;
import fi.hsl.jore.importer.feature.network.route.dto.Route;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;

/**
 * Only for testing, use {@link fi.hsl.jore.importer.feature.batch.route.support.IRouteImportRepository this} to
 * insert/update/delete routes.
 */
@VisibleForTesting
public interface IRouteTestRepository
        extends IBasicCrudRepository<RoutePK, Route, PersistableRoute>, IHistoryRepository<Route> {}
