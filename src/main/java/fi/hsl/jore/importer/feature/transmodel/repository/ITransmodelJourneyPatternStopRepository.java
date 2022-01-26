package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPatternStop;

import java.util.List;

/**
 * Declares CRUD operations for journey pattern stops which
 * are found from the Jore 4 database.
 */
public interface ITransmodelJourneyPatternStopRepository {

    /**
     * Inserts the journey pattern stops into the database.
     * @param journeyPatternStops   The information of the inserted journey pattern stops.
     */
    void insert(List<? extends TransmodelJourneyPatternStop> journeyPatternStops);
}
