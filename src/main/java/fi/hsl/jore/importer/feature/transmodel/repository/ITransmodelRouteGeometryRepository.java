package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteGeometry;

import java.util.List;

/**
 * Declares CRUD operations for route geometries which
 * are found from the Jore 4 database.
 */
public interface ITransmodelRouteGeometryRepository {

    /**
     * Insert the route geometries into the database.
     * @param routeGeometries   The inserted route geometries.
     */
    void insert(List<? extends TransmodelRouteGeometry> routeGeometries);
}
