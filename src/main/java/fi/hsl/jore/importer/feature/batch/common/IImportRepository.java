package fi.hsl.jore.importer.feature.batch.common;

import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.PK;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A repository for importing items in a 2-step process using a temporary staging table:
 *
 * <ul>
 *   <li>Insert imported rows to the staging table
 *   <li>Commit the staging table contents into the target table:
 *       <ol>
 *         <li>First, delete stale rows (= found only in the target table)
 *         <li>Then, update existing rows (= found in both staging and target table)
 *         <li>Finally, insert new rows (= found only in the staging table)
 *       </ol>
 * </ul>
 *
 * @param <ENTITY> The type of the submitted entities
 * @param <KEY> The primary key type of the entities
 */
public interface IImportRepository<ENTITY, KEY extends PK> {

    /**
     * Submit new rows to the staging table
     *
     * @param items The items to submit
     */
    void submitToStaging(Iterable<? extends ENTITY> items);

    /**
     * Commit rows from the staging table to the target table
     *
     * @return Sets describing which keys were inserted/updated/deleted/..
     */
    Map<RowStatus, Set<KEY>> commitStagingToTarget();

    /** Clear out the staging table */
    void clearStagingTable();

    /**
     * Given a result from {@link IImportRepository#commitStagingToTarget}, calculate the number of modifications.
     *
     * @param commitStatus The result of calling {@link IImportRepository#commitStagingToTarget}
     * @return A map describing how many rows were inserted/updated/deleted/..
     */
    static <KEY extends PK> Map<RowStatus, Integer> commitCounts(final Map<RowStatus, Set<KEY>> commitStatus) {
        return commitStatus.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, it -> it.getValue().size()));
    }
}
