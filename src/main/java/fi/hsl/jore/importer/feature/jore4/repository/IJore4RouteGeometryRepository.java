package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteGeometry;

import java.util.List;

/**
 * Declares CRUD operations for route geometries which
 * are found from the Jore 4 database.
 */
public interface IJore4RouteGeometryRepository {

    /**
     * Insert the route geometries into the database.
     * @param routeGeometries   The inserted route geometries.
     */
    void insert(List<? extends Jore4RouteGeometry> routeGeometries);
}
