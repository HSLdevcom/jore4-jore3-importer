package fi.hsl.jore.importer.feature.batch.node.support;

import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.ImportableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodesStaging;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record2;
import org.jooq.ResultQuery;
import org.jooq.SelectForStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static fi.hsl.jore.importer.util.PostgisUtil.geometryEquals;
import static org.jooq.impl.DSL.selectOne;

@Repository
public class NodeImportRepository implements INodeImportRepository {

    private static final InfrastructureNodesStaging STAGING_TABLE = InfrastructureNodesStaging.INFRASTRUCTURE_NODES_STAGING;
    private static final InfrastructureNodes TARGET_TABLE = InfrastructureNodes.INFRASTRUCTURE_NODES;

    private final DSLContext db;

    private final ResultQuery<Record2<String, UUID>> commitQuery;

    @Autowired
    public NodeImportRepository(final DSLContext db) {
        this.db = db;
        commitQuery = constructQuery();
    }

    @Override
    @Transactional
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE)
          .execute();
    }

    @Override
    @Transactional
    public void submitToStaging(final Iterable<? extends ImportableNode> nodes) {
        final BatchBindStep batch = db.batch(db.insertInto(STAGING_TABLE,
                                                           STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID,
                                                           STAGING_TABLE.INFRASTRUCTURE_NODE_TYPE,
                                                           STAGING_TABLE.INFRASTRUCTURE_NODE_LOCATION,
                                                           STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)
                                               .values((String) null, null, null, null));

        nodes.forEach(node -> batch.bind(node.externalId().value(),
                                         node.nodeType().value(),
                                         node.location(),
                                         node.projectedLocation().orElse(null)));

        batch.execute();
    }

    @Override
    @Transactional
    public Map<RowStatus, Set<NodePK>> commitStagingToTarget() {
        return groupKeys(commitQuery.fetchStream(),
                         NodePK::of);
    }

    private ResultQuery<Record2<String, UUID>> constructQuery() {

        final Name statusName = DSL.name("status");
        final Field<String> statusField = DSL.field(statusName, String.class);
        final Field<UUID> idField = DSL.field("id", UUID.class);

        final Name inserted = DSL.name("inserted");
        final Field<String> insertStatusField = DSL.inline(RowStatus.INSERTED.value())
                                                   .as(statusName);

        // A basic insert from one table to another
        // Ignores rows which already exist (e.g. same ext id & type)
        final String insertSql = db.insertInto(TARGET_TABLE)
                                   .columns(TARGET_TABLE.INFRASTRUCTURE_NODE_EXT_ID,
                                            TARGET_TABLE.INFRASTRUCTURE_NODE_TYPE,
                                            TARGET_TABLE.INFRASTRUCTURE_NODE_LOCATION,
                                            TARGET_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)
                                   .select(db.select(STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID,
                                                     STAGING_TABLE.INFRASTRUCTURE_NODE_TYPE,
                                                     STAGING_TABLE.INFRASTRUCTURE_NODE_LOCATION,
                                                     STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)
                                             .from(STAGING_TABLE)
                                             .whereNotExists(selectOne()
                                                                     .from(TARGET_TABLE)
                                                                     .where(TARGET_TABLE.INFRASTRUCTURE_NODE_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID))))
                                   .returning(TARGET_TABLE.INFRASTRUCTURE_NODE_ID.as(idField))
                                   .getSQL();

        final Name updated = DSL.name("updated");
        final Field<String> updateStatusField = DSL.inline(RowStatus.UPDATED.value())
                                                   .as(statusName);

        // Update existing rows
        final String updateSql = db.update(TARGET_TABLE)
                                   // What fields to update
                                   .set(TARGET_TABLE.INFRASTRUCTURE_NODE_LOCATION,
                                        STAGING_TABLE.INFRASTRUCTURE_NODE_LOCATION)
                                   .set(TARGET_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION,
                                        STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)
                                   .from(STAGING_TABLE)
                                   // Find source rows
                                   .where(TARGET_TABLE.INFRASTRUCTURE_NODE_EXT_ID
                                                  .eq(STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID))
                                   // .. with different geometries
                                   .and(// Locations are different
                                        (geometryEquals(TARGET_TABLE.INFRASTRUCTURE_NODE_LOCATION,
                                                        STAGING_TABLE.INFRASTRUCTURE_NODE_LOCATION).not())

                                                // NOTE: Projected location may be null, so we can't just compare it.

                                                // It might be a bit too pedantic to consider all these NULL cases,
                                                // but at least this shouldn't break if we encounter NULLs in either table,
                                                // regardless of how likely it is.

                                                // or projected location is empty in target but not in staging
                                                .or((TARGET_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION.isNull())
                                                            .and(STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION.isNotNull()))
                                                // or projected location is not empty in target but is empty in staging
                                                .or((TARGET_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION.isNotNull())
                                                            .and(STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION.isNull()))
                                                // or projected location is present in both tables and not equal
                                                .or((TARGET_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION.isNotNull())
                                                            .and(STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION.isNotNull())
                                                            .and(geometryEquals(TARGET_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION,
                                                                                STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION).not()))
                                   )
                                   .returning(TARGET_TABLE.INFRASTRUCTURE_NODE_ID.as(idField))
                                   .getSQL();

        final Name deleted = DSL.name("deleted");
        final Field<String> deleteStatusField = DSL.inline(RowStatus.DELETED.value())
                                                   .as(statusName);

        // Delete old stale rows
        final String deleteSql = db.deleteFrom(TARGET_TABLE)
                                   // Find rows which are missing from the latest dataset
                                   .whereNotExists(selectOne()
                                                           .from(STAGING_TABLE)
                                                           .where(STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID.eq(TARGET_TABLE.INFRASTRUCTURE_NODE_EXT_ID)))
                                   .returning(TARGET_TABLE.INFRASTRUCTURE_NODE_ID.as(idField))
                                   .getSQL();

        final SelectForStep<Record2<String, UUID>> insertQuery =
                db.select(insertStatusField, idField)
                  .from(inserted);

        final SelectForStep<Record2<String, UUID>> updateQuery =
                db.select(updateStatusField, idField)
                  .from(updated);

        final SelectForStep<Record2<String, UUID>> deleteQuery =
                db.select(deleteStatusField, idField)
                  .from(deleted);

        final SelectForStep<Record2<String, UUID>> unionQuery =
                insertQuery
                        .union(updateQuery)
                        .union(deleteQuery);

        // Due to https://github.com/jOOQ/jOOQ/issues/4474 we must manually combine the final SQL
        final String actualSql = String.format("with %s as (%s), %s as (%s), %s as (%s) %s",
                                               inserted.getName()[0],
                                               insertSql,
                                               updated.getName()[0],
                                               updateSql,
                                               deleted.getName()[0],
                                               deleteSql,
                                               unionQuery.getSQL());

        return db.resultQuery(actualSql)
                 .coerce(statusField, idField);
    }
}
