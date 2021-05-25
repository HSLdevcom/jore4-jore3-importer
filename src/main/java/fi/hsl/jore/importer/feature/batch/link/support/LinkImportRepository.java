package fi.hsl.jore.importer.feature.batch.link.support;

import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.ImportableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinks;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinksStaging;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;
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
public class LinkImportRepository implements ILinkImportRepository {

    private static final InfrastructureLinksStaging STAGING_TABLE = InfrastructureLinksStaging.INFRASTRUCTURE_LINKS_STAGING;
    private static final InfrastructureLinks TARGET_TABLE = InfrastructureLinks.INFRASTRUCTURE_LINKS;
    private static final InfrastructureNodes NODES_TABLE = InfrastructureNodes.INFRASTRUCTURE_NODES;

    private final DSLContext db;

    private final ResultQuery<Record2<String, UUID>> commitQuery;

    @Autowired
    public LinkImportRepository(final DSLContext db) {
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
    public void submitToStaging(final Iterable<? extends ImportableLink> links) {
        final BatchBindStep batch = db.batch(db.insertInto(STAGING_TABLE,
                                                           STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                                                           STAGING_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                                                           STAGING_TABLE.INFRASTRUCTURE_LINK_GEOG,
                                                           STAGING_TABLE.INFRASTRUCTURE_LINK_START_NODE_EXT_ID,
                                                           STAGING_TABLE.INFRASTRUCTURE_LINK_END_NODE_EXT_ID)
                                               .values((String) null, null, null, null, null));

        links.forEach(link -> batch.bind(link.externalId().value(),
                                         link.networkType().label(),
                                         link.geometry(),
                                         link.fromNode().value(),
                                         link.toNode().value()));

        batch.execute();
    }

    @Override
    @Transactional
    public Map<RowStatus, Set<LinkPK>> commitStagingToTarget() {
        return groupKeys(commitQuery.fetchStream(),
                         LinkPK::of);
    }

    private ResultQuery<Record2<String, UUID>> constructQuery() {

        final Name statusName = DSL.name("status");
        final Field<String> statusField = DSL.field(statusName, String.class);
        final Field<UUID> idField = DSL.field("id", UUID.class);

        final Name inserted = DSL.name("inserted");
        final Field<String> insertStatusField = DSL.inline(RowStatus.INSERTED.value())
                                                   .as(statusName);

        final InfrastructureNodes nodeFromTable = NODES_TABLE.as("node_from");
        final InfrastructureNodes nodeToTable = NODES_TABLE.as("node_to");
        // A basic insert from one table to another
        // Ignores rows which already exist (e.g. same ext id & network type)
        final String insertSql = db.insertInto(TARGET_TABLE)
                                   .columns(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                                            TARGET_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                                            TARGET_TABLE.INFRASTRUCTURE_LINK_GEOG,
                                            TARGET_TABLE.INFRASTRUCTURE_LINK_START_NODE,
                                            TARGET_TABLE.INFRASTRUCTURE_LINK_END_NODE)
                                   .select(db.select(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                                                     STAGING_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                                                     STAGING_TABLE.INFRASTRUCTURE_LINK_GEOG,
                                                     nodeFromTable.INFRASTRUCTURE_NODE_ID,
                                                     nodeToTable.INFRASTRUCTURE_NODE_ID)
                                             .from(STAGING_TABLE)
                                             .leftJoin(nodeFromTable).on(nodeFromTable.INFRASTRUCTURE_NODE_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_LINK_START_NODE_EXT_ID))
                                             .leftJoin(nodeToTable).on(nodeToTable.INFRASTRUCTURE_NODE_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_LINK_END_NODE_EXT_ID))
                                             .whereNotExists(selectOne()
                                                                     .from(TARGET_TABLE)
                                                                     .where(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID))))
                                   .returning(TARGET_TABLE.INFRASTRUCTURE_LINK_ID.as(idField))
                                   .getSQL();

        final Name updated = DSL.name("updated");
        final Field<String> updateStatusField = DSL.inline(RowStatus.UPDATED.value())
                                                   .as(statusName);

        // Update existing rows
        final String updateSql = db.update(TARGET_TABLE)
                                   // What fields to update
                                   .set(TARGET_TABLE.INFRASTRUCTURE_LINK_GEOG,
                                        STAGING_TABLE.INFRASTRUCTURE_LINK_GEOG)
                                   .from(STAGING_TABLE)
                                   // Find source rows..
                                   .where(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID
                                                  .eq(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID))
                                   // .. with updated geometries
                                   .andNot(geometryEquals(TARGET_TABLE.INFRASTRUCTURE_LINK_GEOG,
                                                          STAGING_TABLE.INFRASTRUCTURE_LINK_GEOG))
                                   .returning(TARGET_TABLE.INFRASTRUCTURE_LINK_ID.as(idField))
                                   .getSQL();

        final Name deleted = DSL.name("deleted");
        final Field<String> deleteStatusField = DSL.inline(RowStatus.DELETED.value())
                                                   .as(statusName);

        // Delete old stale rows
        final String deleteSql = db.deleteFrom(TARGET_TABLE)
                                   // Find rows which are missing from the latest dataset
                                   .whereNotExists(selectOne()
                                                           .from(STAGING_TABLE)
                                                           .where(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID.eq(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID)))
                                   .returning(TARGET_TABLE.INFRASTRUCTURE_LINK_ID.as(idField))
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
