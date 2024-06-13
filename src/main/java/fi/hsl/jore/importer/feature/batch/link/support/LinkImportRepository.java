package fi.hsl.jore.importer.feature.batch.link.support;

import static fi.hsl.jore.importer.util.PostgisUtil.geometryEquals;
import static org.jooq.impl.DSL.selectOne;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Jore3Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinks;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinksStaging;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LinkImportRepository extends AbstractImportRepository<Jore3Link, LinkPK> implements ILinkImportRepository {

    private static final InfrastructureLinksStaging STAGING_TABLE =
            InfrastructureLinksStaging.INFRASTRUCTURE_LINKS_STAGING;
    private static final InfrastructureLinks TARGET_TABLE = InfrastructureLinks.INFRASTRUCTURE_LINKS;
    private static final InfrastructureNodes NODES_TABLE = InfrastructureNodes.INFRASTRUCTURE_NODES;

    private final DSLContext db;

    @Autowired
    public LinkImportRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE).execute();
    }

    @Override
    @Transactional
    public void submitToStaging(final Iterable<? extends Jore3Link> links) {
        final BatchBindStep batch = db.batch(db.insertInto(
                        STAGING_TABLE,
                        STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                        STAGING_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                        STAGING_TABLE.INFRASTRUCTURE_LINK_GEOG,
                        STAGING_TABLE.INFRASTRUCTURE_LINK_START_NODE_EXT_ID,
                        STAGING_TABLE.INFRASTRUCTURE_LINK_END_NODE_EXT_ID)
                .values((String) null, null, null, null, null));

        links.forEach(link -> batch.bind(
                link.externalId().value(),
                link.networkType().label(),
                link.geometry(),
                link.fromNode().value(),
                link.toNode().value()));

        if (batch.size() > 0) {
            batch.execute();
        }
    }

    protected Set<LinkPK> delete() {
        return db
                .deleteFrom(TARGET_TABLE)
                // Find rows which are missing from the latest dataset
                .whereNotExists(selectOne()
                        .from(STAGING_TABLE)
                        .where(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID.eq(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID)))
                .returningResult(TARGET_TABLE.INFRASTRUCTURE_LINK_ID)
                .fetch()
                .stream()
                .map(row -> LinkPK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    protected Set<LinkPK> update() {
        return db
                .update(TARGET_TABLE)
                // What fields to update
                .set(TARGET_TABLE.INFRASTRUCTURE_LINK_GEOG, STAGING_TABLE.INFRASTRUCTURE_LINK_GEOG)
                .from(STAGING_TABLE)
                // Find source rows..
                .where(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID))
                // .. with updated geometries
                .andNot(geometryEquals(TARGET_TABLE.INFRASTRUCTURE_LINK_GEOG, STAGING_TABLE.INFRASTRUCTURE_LINK_GEOG))
                .returningResult(TARGET_TABLE.INFRASTRUCTURE_LINK_ID)
                .fetch()
                .stream()
                .map(row -> LinkPK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    protected Set<LinkPK> insert() {
        final InfrastructureNodes nodeFromTable = NODES_TABLE.as("node_from");
        final InfrastructureNodes nodeToTable = NODES_TABLE.as("node_to");
        return db
                .insertInto(TARGET_TABLE)
                .columns(
                        TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                        TARGET_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                        TARGET_TABLE.INFRASTRUCTURE_LINK_GEOG,
                        TARGET_TABLE.INFRASTRUCTURE_LINK_START_NODE,
                        TARGET_TABLE.INFRASTRUCTURE_LINK_END_NODE)
                .select(db.select(
                                STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                                STAGING_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                                STAGING_TABLE.INFRASTRUCTURE_LINK_GEOG,
                                nodeFromTable.INFRASTRUCTURE_NODE_ID,
                                nodeToTable.INFRASTRUCTURE_NODE_ID)
                        .from(STAGING_TABLE)
                        .innerJoin(nodeFromTable)
                        .on(nodeFromTable.INFRASTRUCTURE_NODE_EXT_ID.eq(
                                STAGING_TABLE.INFRASTRUCTURE_LINK_START_NODE_EXT_ID))
                        .innerJoin(nodeToTable)
                        .on(nodeToTable.INFRASTRUCTURE_NODE_EXT_ID.eq(
                                STAGING_TABLE.INFRASTRUCTURE_LINK_END_NODE_EXT_ID))
                        .whereNotExists(selectOne()
                                .from(TARGET_TABLE)
                                .where(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID.eq(
                                        STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID))))
                .returningResult(TARGET_TABLE.INFRASTRUCTURE_LINK_ID)
                .fetch()
                .stream()
                .map(row -> LinkPK.of(row.value1()))
                .collect(Collectors.toSet());
    }
}
