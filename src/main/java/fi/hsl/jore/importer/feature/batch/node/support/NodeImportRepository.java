package fi.hsl.jore.importer.feature.batch.node.support;

import static fi.hsl.jore.importer.util.PostgisUtil.geometryEquals;
import static org.jooq.impl.DSL.selectOne;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Jore3Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodesStaging;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NodeImportRepository extends AbstractImportRepository<Jore3Node, NodePK> implements INodeImportRepository {

    private static final InfrastructureNodesStaging STAGING_TABLE =
            InfrastructureNodesStaging.INFRASTRUCTURE_NODES_STAGING;
    private static final InfrastructureNodes TARGET_TABLE = InfrastructureNodes.INFRASTRUCTURE_NODES;

    private final DSLContext db;

    @Autowired
    public NodeImportRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE).execute();
    }

    @Override
    @Transactional
    public void submitToStaging(final Iterable<? extends Jore3Node> nodes) {
        final BatchBindStep batch = db.batch(db.insertInto(
                        STAGING_TABLE,
                        STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID,
                        STAGING_TABLE.INFRASTRUCTURE_NODE_TYPE,
                        STAGING_TABLE.INFRASTRUCTURE_NODE_LOCATION,
                        STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)
                .values((String) null, null, null, null));

        nodes.forEach(node -> batch.bind(
                node.externalId().value(),
                node.nodeType().value(),
                node.location(),
                node.projectedLocation().orElse(null)));

        if (batch.size() > 0) {
            batch.execute();
        }
    }

    protected Set<NodePK> delete() {
        return db
                .deleteFrom(TARGET_TABLE)
                // Find rows which are missing from the latest dataset
                .whereNotExists(selectOne()
                        .from(STAGING_TABLE)
                        .where(STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID.eq(TARGET_TABLE.INFRASTRUCTURE_NODE_EXT_ID)))
                .returningResult(TARGET_TABLE.INFRASTRUCTURE_NODE_ID)
                .fetch()
                .stream()
                .map(row -> NodePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    protected Set<NodePK> update() {
        return db
                .update(TARGET_TABLE)
                // What fields to update
                .set(TARGET_TABLE.INFRASTRUCTURE_NODE_LOCATION, STAGING_TABLE.INFRASTRUCTURE_NODE_LOCATION)
                .set(
                        TARGET_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION,
                        STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)
                .from(STAGING_TABLE)
                // Find source rows
                .where(TARGET_TABLE.INFRASTRUCTURE_NODE_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID))
                // .. with different geometries
                .and( // Locations are different
                        (geometryEquals(
                                                TARGET_TABLE.INFRASTRUCTURE_NODE_LOCATION,
                                                STAGING_TABLE.INFRASTRUCTURE_NODE_LOCATION)
                                        .not())

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
                                        .and(geometryEquals(
                                                        TARGET_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION,
                                                        STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)
                                                .not())))
                .returningResult(TARGET_TABLE.INFRASTRUCTURE_NODE_ID)
                .fetch()
                .stream()
                .map(row -> NodePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    protected Set<NodePK> insert() {
        return db
                .insertInto(TARGET_TABLE)
                .columns(
                        TARGET_TABLE.INFRASTRUCTURE_NODE_EXT_ID,
                        TARGET_TABLE.INFRASTRUCTURE_NODE_TYPE,
                        TARGET_TABLE.INFRASTRUCTURE_NODE_LOCATION,
                        TARGET_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)
                .select(db.select(
                                STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID,
                                STAGING_TABLE.INFRASTRUCTURE_NODE_TYPE,
                                STAGING_TABLE.INFRASTRUCTURE_NODE_LOCATION,
                                STAGING_TABLE.INFRASTRUCTURE_NODE_PROJECTED_LOCATION)
                        .from(STAGING_TABLE)
                        .whereNotExists(selectOne()
                                .from(TARGET_TABLE)
                                .where(TARGET_TABLE.INFRASTRUCTURE_NODE_EXT_ID.eq(
                                        STAGING_TABLE.INFRASTRUCTURE_NODE_EXT_ID))))
                .returningResult(TARGET_TABLE.INFRASTRUCTURE_NODE_ID)
                .fetch()
                .stream()
                .map(row -> NodePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }
}
