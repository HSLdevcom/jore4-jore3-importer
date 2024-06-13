package fi.hsl.jore.importer.feature.infrastructure.node.repository;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodesWithHistory;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureNodesRecord;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NodeRepository implements INodeTestRepository {

    private static final InfrastructureNodes NODE = InfrastructureNodes.INFRASTRUCTURE_NODES;
    private static final InfrastructureNodesWithHistory HISTORY_VIEW =
            InfrastructureNodesWithHistory.INFRASTRUCTURE_NODES_WITH_HISTORY;
    private static final TableField<InfrastructureNodesRecord, UUID> PRIMARY_KEY = NODE.INFRASTRUCTURE_NODE_ID;

    private final DSLContext db;

    @Autowired
    public NodeRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public NodePK insert(final PersistableNode node) {
        final InfrastructureNodesRecord r = db.newRecord(NODE);

        r.setInfrastructureNodeExtId(node.externalId().value());
        r.setInfrastructureNodeType(node.nodeType().value());
        r.setInfrastructureNodeLocation(node.location());
        r.setInfrastructureNodeProjectedLocation(node.projectedLocation().orElse(null));

        r.store();

        return NodePK.of(r.getInfrastructureNodeId());
    }

    @Override
    @Transactional
    public List<NodePK> insert(final List<PersistableNode> entities) {
        return entities.stream().map(this::insert).toList();
    }

    @Override
    @Transactional
    public List<NodePK> insert(final PersistableNode... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public NodePK update(final Node node) {
        final InfrastructureNodesRecord r = Optional.ofNullable(db.selectFrom(NODE)
                        .where(PRIMARY_KEY.eq(node.pk().value()))
                        .fetchAny())
                .orElseThrow();

        r.setInfrastructureNodeLocation(node.location());
        r.setInfrastructureNodeProjectedLocation(node.projectedLocation().orElse(null));

        r.store();

        return NodePK.of(r.getInfrastructureNodeId());
    }

    @Override
    @Transactional
    public List<NodePK> update(final List<Node> entities) {
        return entities.stream().map(this::update).toList();
    }

    @Override
    @Transactional
    public List<NodePK> update(final Node... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Node> findById(final NodePK id) {
        return db.selectFrom(NODE)
                .where(PRIMARY_KEY.eq(id.value()))
                .fetchStream()
                .map(Node::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Node> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(NODE)
                .where(NODE.INFRASTRUCTURE_NODE_EXT_ID.eq(externalId.value()))
                .fetchStream()
                .map(Node::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Node> findAll() {
        return db.selectFrom(NODE).fetchStream().map(Node::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<NodePK> findAllIds() {
        return db.select(PRIMARY_KEY)
                .from(NODE)
                .fetchStream()
                .map(row -> NodePK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount().from(NODE).fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public int countHistory() {
        //noinspection ConstantConditions
        return db.selectCount().from(HISTORY_VIEW).fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean empty() {
        return count() == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emptyHistory() {
        return countHistory() == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Node> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                .orderBy(HISTORY_VIEW.INFRASTRUCTURE_NODE_SYS_PERIOD.asc())
                .fetchStream()
                .map(Node::from)
                .toList();
    }
}
