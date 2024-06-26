package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPattern;

/** Declares CRUD operations for journey patterns which are found from the Jore 4 database. */
public interface IJore4JourneyPatternRepository {

    /**
     * Inserts the journey patterns into the database.
     *
     * @param journeyPatterns The information of the inserted journey patterns.
     */
    void insert(Iterable<? extends Jore4JourneyPattern> journeyPatterns);
}
