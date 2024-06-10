package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;

/**
 * Declares CRUD operations for lines which are
 * found from the Jore 4 database.
 */
public interface IJore4LineRepository {

    /**
     * Inserts the lines into the database.
     * @param lines The information of the inserted lines.
     */
    void insert(Iterable<? extends Jore4Line> lines);
}
