package fi.hsl.jore.importer.feature.infrastructure.link.repository;

import com.google.common.collect.Lists;
import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringConverter;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinks;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinksRecord;
import io.vavr.collection.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Deque;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LinkRepository
        implements ILinkRepository {

    private static final InfrastructureLinks LINKS = InfrastructureLinks.INFRASTRUCTURE_LINKS;

    private final DSLContext db;

    @Autowired
    public LinkRepository(final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public LinkPK insertLink(final PersistableLink link) {
        final InfrastructureLinksRecord record = db.newRecord(LINKS);

        record.setInfrastructureNetworkTypeId(link.networkTypePk().value());
        record.setInfrastructureLinkGeog(link.geometry());

        record.store();

        return LinkPK.of(record.getInfrastructureLinkId());
    }

    @Override
    @Transactional
    public List<LinkPK> insertLinks(final Iterable<? extends PersistableLink> links) {
        final String sql = db.insertInto(LINKS,
                                         LINKS.INFRASTRUCTURE_NETWORK_TYPE_ID,
                                         LINKS.INFRASTRUCTURE_LINK_GEOG)
                             .values((UUID) null, null)
                             .returningResult(LINKS.INFRASTRUCTURE_LINK_ID)
                             .getSQL();

        final Deque<LinkPK> keys = Lists.newLinkedList();

        // jOOQ doesn't support returning keys from batch operations (https://github.com/jOOQ/jOOQ/issues/3327),
        // so for now we have to resort to JDBC
        db.connection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (final PersistableLink link : links) {
                    stmt.setObject(1, link.networkTypePk().value());
                    stmt.setObject(2, LineStringConverter.INSTANCE.to(link.geometry()), Types.OTHER);
                    stmt.addBatch();
                }

                stmt.executeBatch();

                try (final ResultSet rs = stmt.getGeneratedKeys()) {
                    while (rs.next()) {
                        keys.add(LinkPK.of(rs.getObject(1, UUID.class)));
                    }
                }
            }
        });

        return List.ofAll(keys);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Link> findById(final LinkPK linkId) {
        return db.selectFrom(LINKS)
                 .where(LINKS.INFRASTRUCTURE_LINK_ID.eq(linkId.value()))
                 .fetchStream()
                 .map(Link::of)
                 .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> findAll() {
        return db.selectFrom(LINKS)
                 .fetchStream()
                 .map(Link::of)
                 .collect(List.collector());
    }
}
