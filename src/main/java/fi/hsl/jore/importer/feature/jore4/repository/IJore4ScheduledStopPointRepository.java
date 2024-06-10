package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPoint;

import java.util.List;

/**
 * Declares CRUD operations for scheduled stop points which are
 * found from the Jore 4 database.
 */
public interface IJore4ScheduledStopPointRepository {

    /**
     * Inserts new scheduled stop points to the database.
     * @param stopPoints    The information of the stop points.
     */
    void insert(Iterable<? extends Jore4ScheduledStopPoint> stopPoints);
}
