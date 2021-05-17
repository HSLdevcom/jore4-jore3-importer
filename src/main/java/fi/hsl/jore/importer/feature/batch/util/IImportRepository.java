package fi.hsl.jore.importer.feature.batch.util;

import fi.hsl.jore.importer.feature.common.dto.field.PK;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Traversable;
import org.jooq.Record2;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A repository for importing items in a 2-step process using a temporary staging table:
 * <ul>
 *     <li>Insert imported rows to the staging table
 *     <li>Commit the staging table contents into the target table:
 *     <ul>
 *         <li>Insert new rows (= found only in the staging table)
 *         <li>Update existing rows (= found  in both staging and target table)
 *         <li>Delete stale rows (= found only in the target table)
 *     </ul>
 * </ul>
 *
 * @param <ENTITY> The type of the submitted entities
 * @param <KEY>    The primary key type of the entities
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

    /**
     * Clear out the staging table
     */
    void clearStagingTable();

    /**
     * Given a result from {@link IImportRepository#commitStagingToTarget}, calculate the number of modifications.
     *
     * @param commitStatus The result of calling {@link IImportRepository#commitStagingToTarget}
     * @return A map describing how many rows were inserted/updated/deleted/..
     */
    static <KEY extends PK> Map<RowStatus, Integer> commitCounts(final Map<RowStatus, Set<KEY>> commitStatus) {
        return commitStatus.mapValues(Traversable::size);
    }

    /**
     * Given a stream of statuses (as strings) and keys (as uuids), group the keys by the status.
     * <p>
     * This operation terminates the stream.
     *
     * @param stream      A stream of statuses and keys
     * @param keyFunction Converts raw UUIDs to KEYs
     * @return A map describing which operation (RowStatus) was applied to each key
     */
    default Map<RowStatus, Set<KEY>> groupKeys(final Stream<Record2<String, UUID>> stream,
                                               final Function<UUID, KEY> keyFunction) {
        return stream
                .map(record -> Tuple.of(RowStatus.of(record.value1()).orElseThrow(),
                                        keyFunction.apply(record.value2())))
                .reduce(HashMap.empty(),
                        (result, row) -> result.put(row._1, HashSet.of(row._2),
                                                    Set::addAll),
                        (m1, m2) -> m1.merge(m2, Set::addAll));
    }
}
