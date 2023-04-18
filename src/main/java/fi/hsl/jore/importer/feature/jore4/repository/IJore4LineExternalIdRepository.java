package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4LineExternalId;

import java.util.List;

/**
 * Declares CRUD operations for line external id table
 * which are found from the Jore 4 database.
 */
public interface IJore4LineExternalIdRepository {

    /**
     * Inserts the line external ids into the database.
     * @param lineExternalIds The information of the inserted line external ids.
     */
    void insert(List<? extends Jore4LineExternalId> lineExternalIds);
}
