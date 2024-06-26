package fi.hsl.jore.importer.feature.batch.route_direction.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.route_direction.dto.Jore3RouteDirection;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableJourneyPatternIdMapping;
import fi.hsl.jore.importer.feature.network.route_direction.dto.PersistableRouteIdMapping;
import fi.hsl.jore.importer.feature.network.route_direction.dto.generated.RouteDirectionPK;

public interface IRouteDirectionImportRepository extends IImportRepository<Jore3RouteDirection, RouteDirectionPK> {

    /**
     * Sets the ids which identifies the journey patterns found from the Jore 4 database.
     *
     * @param idMappings The information that's required to set the Jore 4 ids of journey patterns.
     */
    void setJourneyPatternJore4Ids(Iterable<PersistableJourneyPatternIdMapping> idMappings);

    /**
     * Sets the ids which identifies the routes found from the Jore 4 database.
     *
     * @param idMappings The information that's required to set the Jore 4 ids of routes.
     */
    void setRouteJore4Ids(Iterable<PersistableRouteIdMapping> idMappings);
}
