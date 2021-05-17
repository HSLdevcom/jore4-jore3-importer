package fi.hsl.jore.importer.feature.batch.point.support;

import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinkPointsStaging;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinks;
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

@Repository
public class LinkPointImportRepository implements ILinkPointImportRepository {

    private static final InfrastructureLinkPointsStaging STAGING_TABLE = InfrastructureLinkPointsStaging.INFRASTRUCTURE_LINK_POINTS_STAGING;
    private static final InfrastructureLinks TARGET_TABLE = InfrastructureLinks.INFRASTRUCTURE_LINKS;

    private final DSLContext db;

    private final ResultQuery<Record2<String, UUID>> commitQuery;

    @Autowired
    public LinkPointImportRepository(final DSLContext db) {
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
    public void submitToStaging(final Iterable<? extends LinkGeometry> links) {
        final BatchBindStep batch = db.batch(db.insertInto(STAGING_TABLE,
                                                           STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                                                           STAGING_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                                                           STAGING_TABLE.INFRASTRUCTURE_LINK_POINTS)
                                               .values((String) null, null, null));

        links.forEach(link -> batch.bind(link.externalId().value(),
                                         link.networkType().label(),
                                         link.geometry()));

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

        final Name updated = DSL.name("updated");
        final Field<String> updateStatusField = DSL.inline(RowStatus.UPDATED.value())
                                                   .as(statusName);

        // Update existing rows
        final String updateSql = db.update(TARGET_TABLE)
                                   // What fields to update
                                   .set(TARGET_TABLE.INFRASTRUCTURE_LINK_POINTS,
                                        STAGING_TABLE.INFRASTRUCTURE_LINK_POINTS)
                                   .from(STAGING_TABLE)
                                   // Find source rows..
                                   .where(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID
                                                  .eq(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID))
                                   // .. with different points
                                   .and(// Target table has no points at all
                                        (TARGET_TABLE.INFRASTRUCTURE_LINK_POINTS.isNull())
                                                // or the points do not match
                                                .orNot(geometryEquals(TARGET_TABLE.INFRASTRUCTURE_LINK_POINTS,
                                                                      STAGING_TABLE.INFRASTRUCTURE_LINK_POINTS)))
                                   .returning(TARGET_TABLE.INFRASTRUCTURE_LINK_ID.as(idField))
                                   .getSQL();

        final SelectForStep<Record2<String, UUID>> updateQuery =
                db.select(updateStatusField, idField)
                  .from(updated);

        // Due to https://github.com/jOOQ/jOOQ/issues/4474 we must manually combine the final SQL
        final String actualSql = String.format("with %s as (%s) %s",
                                               updated.getName()[0],
                                               updateSql,
                                               updateQuery.getSQL());

        return db.resultQuery(actualSql)
                 .coerce(statusField, idField);
    }
}
