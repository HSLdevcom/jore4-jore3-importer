package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4TimingPlace;

import java.util.List;

/**
 * Declares CRUD operations for timing places which are found from the Jore 4
 * database.
 */
public interface IJore4TimingPlaceRepository {

    /**
     * Inserts new timing places to the database.
     * @param timingPlaces    The information of the timing places.
     */
    void insert(List<? extends Jore4TimingPlace> timingPlaces);
}
