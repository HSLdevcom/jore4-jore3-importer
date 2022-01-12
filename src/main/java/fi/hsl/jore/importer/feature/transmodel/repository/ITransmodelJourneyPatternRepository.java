package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPattern;

import java.util.List;

/**
 * Declares CRUD operations for journey patterns which are found
 * from the Jore 4 database.
 */
public interface ITransmodelJourneyPatternRepository {

    /**
     * Inserts the journey patterns into the database.
     * @param journeyPatterns   The information of the inserted journey patterns.
     */
    void insert(List<? extends TransmodelJourneyPattern> journeyPatterns);
}
