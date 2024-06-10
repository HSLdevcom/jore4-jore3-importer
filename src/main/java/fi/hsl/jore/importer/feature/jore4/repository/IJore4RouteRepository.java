package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4Route;

import java.util.List;

/**
 * Declares CRUD operations for routes which are found
 * from the Jore 4 database.
 */
public interface IJore4RouteRepository {

    /**
     * Insert new routes into the database.
     * @param routes    The information of the inserted routes.
     */
    void insert(Iterable<? extends Jore4Route> routes);
}
