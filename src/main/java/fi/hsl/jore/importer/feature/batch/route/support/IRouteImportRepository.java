package fi.hsl.jore.importer.feature.batch.route.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.route.dto.ImportableRoute;
import fi.hsl.jore.importer.feature.network.route.dto.PersistableJourneyPatternIdMapping;
import fi.hsl.jore.importer.feature.network.route.dto.PersistableRouteIdMapping;
import fi.hsl.jore.importer.feature.network.route.dto.generated.RoutePK;
import io.vavr.collection.List;

public interface IRouteImportRepository extends IImportRepository<ImportableRoute, RoutePK> {

    /**
     * Sets the ids which identifies the journey patterns of routes
     * in the Jore 4 database.
     *
     * @param idsMappings   The information that's required to set the transmodel ids
     *                      of journey patterns.
     */
    void setJourneyPatternTransmodelIds(List<PersistableJourneyPatternIdMapping> idsMappings);

    /**
     * Sets the ids which identifies the routes in the Jore 4 database.
     *
     * @param   idMappings   The information that's required to set the transmodel ids
     *                       of routes.
     */
    void setRouteTransmodelIds(List<PersistableRouteIdMapping> idMappings);
}
