package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;

import java.util.List;

/**
 * Declares CRUD operations for scheduled stop points which are
 * found from the Jore 4 database.
 */
public interface ITransmodelScheduledStopPointRepository {

    /**
     * Inserts new scheduled stop points to the database.
     * @param stopPoints    The information of the stop points.
     */
    void insert(List<? extends TransmodelScheduledStopPoint> stopPoints);
}
