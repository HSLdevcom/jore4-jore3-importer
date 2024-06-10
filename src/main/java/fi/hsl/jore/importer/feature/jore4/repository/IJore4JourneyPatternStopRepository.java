package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPatternStop;

/**
 * Declares CRUD operations for journey pattern stops which
 * are found from the Jore 4 database.
 */
public interface IJore4JourneyPatternStopRepository {

    /**
     * Inserts the journey pattern stops into the database.
     * @param journeyPatternStops   The information of the inserted journey pattern stops.
     */
    void insert(Iterable<? extends Jore4JourneyPatternStop> journeyPatternStops);
}
