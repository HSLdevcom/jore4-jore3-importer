package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRoute;

import java.util.List;

/**
 * Declares CRUD operations for routes which are found
 * from the Jore 4 database.
 */
public interface ITransmodelRouteRepository {

    /**
     * Insert new routes into the database.
     * @param routes    The information of the inserted routes.
     */
    void insert(List<? extends TransmodelRoute> routes);
}
