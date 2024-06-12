package fi.hsl.jore.importer.feature.digiroad.service;

import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import java.util.Optional;

/** Declares a method which which is used to get the Digiroad information of a stop point. */
public interface DigiroadStopService {

    /**
     * Finds the information of a Digiroad stop by using the national id as search criteria.
     *
     * @param nationalId The national identifier of a stop.
     * @return An {@link Optional} object which contains the found {@link DigiroadStop} object. If no stop is found,
     *     this method returns an empty {@link Optional}.
     */
    Optional<DigiroadStop> findByNationalId(final long nationalId);
}
