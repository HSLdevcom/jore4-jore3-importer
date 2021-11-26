package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;

import java.util.List;

/**
 * Declares CRUD operations for lines which are
 * found from the Jore 4 database.
 */
public interface ITransmodelLineRepository {

    /**
     * Inserts the lines into the database.
     * @param lines The information of the inserted lines.
     */
    void insert(List<? extends TransmodelLine> lines);
}
