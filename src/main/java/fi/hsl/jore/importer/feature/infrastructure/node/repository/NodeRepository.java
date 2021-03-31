package fi.hsl.jore.importer.feature.infrastructure.node.repository;

import com.google.common.collect.Lists;
import fi.hsl.jore.importer.config.jooq.converter.geometry.PointConverter;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodes;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNodesWithHistory;
import io.vavr.collection.List;
import org.jooq.DSLContext;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Deque;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NodeRepository implements INodeRepository {

    private static final InfrastructureNodes NODE = InfrastructureNodes.INFRASTRUCTURE_NODES;
    private static final InfrastructureNodesWithHistory HISTORY_VIEW = InfrastructureNodesWithHistory.INFRASTRUCTURE_NODES_WITH_HISTORY;

    private final DSLContext db;

    @Autowired
    public NodeRepository(final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public List<NodePK> upsert(final Iterable<? extends PersistableNode> nodes) {
        final String sql = db.insertInto(NODE,
                                         NODE.INFRASTRUCTURE_NODE_EXT_ID,
                                         NODE.INFRASTRUCTURE_NODE_TYPE,
                                         NODE.INFRASTRUCTURE_NODE_LOCATION)
                             // parameters 1-3
                             .values((String) null, null, null)
                             .onConflict(NODE.INFRASTRUCTURE_NODE_EXT_ID,
                                         NODE.INFRASTRUCTURE_NODE_TYPE)
                             .doUpdate()
                             // parameter 4
                             .set(NODE.INFRASTRUCTURE_NODE_LOCATION, (Point) null)
                             // parameter 5
                             .where(NODE.INFRASTRUCTURE_NODE_EXT_ID.eq((String) null)
                                                                   // parameter 6
                                                                   .and(NODE.INFRASTRUCTURE_NODE_TYPE.eq((String) null)))
                             .returningResult(NODE.INFRASTRUCTURE_NODE_ID)
                             .getSQL();

        final Deque<NodePK> keys = Lists.newLinkedList();

        // jOOQ doesn't support returning keys from batch operations (https://github.com/jOOQ/jOOQ/issues/3327),
        // so for now we have to resort to JDBC
        db.connection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (final PersistableNode node : nodes) {
                    final Object location = PointConverter.INSTANCE.to(node.location());
                    // The raw sql statement produced by jOOQ contains '?' placeholders (e.g. "VALUES (?, ?, ?::geometry)"),
                    // which we must populate with the corresponding values. Because they are anonymous and positional,
                    // we may need to submit the same value multiple times (once for each placeholder).
                    stmt.setObject(1, node.externalId().value());
                    stmt.setObject(2, node.nodeType().value());
                    stmt.setObject(3, location);
                    stmt.setObject(4, location);
                    stmt.setObject(5, node.externalId().value());
                    stmt.setObject(6, node.nodeType().value());
                    stmt.addBatch();
                }

                stmt.executeBatch();

                try (final ResultSet rs = stmt.getGeneratedKeys()) {
                    while (rs.next()) {
                        keys.add(NodePK.of(rs.getObject(1, UUID.class)));
                    }
                }
            }
        });

        return List.ofAll(keys);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Node> findById(final NodePK id) {
        return db.selectFrom(NODE)
                 .where(NODE.INFRASTRUCTURE_NODE_ID.eq(id.value()))
                 .fetchStream()
                 .map(Node::of)
                 .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Node> findAll() {
        return db.selectFrom(NODE)
                 .fetchStream()
                 .map(Node::of)
                 .collect(List.collector());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount()
                 .from(NODE)
                 .fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public int countHistory() {
        //noinspection ConstantConditions
        return db.selectCount()
                 .from(HISTORY_VIEW)
                 .fetchOne(0, int.class);
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
                 .map(Node::of)
                 .collect(List.collector());
    }
}
