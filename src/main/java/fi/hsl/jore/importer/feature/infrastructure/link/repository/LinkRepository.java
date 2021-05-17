package fi.hsl.jore.importer.feature.infrastructure.link.repository;

import com.google.common.collect.Lists;
import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringConverter;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinks;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinksWithHistory;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinksRecord;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.locationtech.jts.geom.LineString;
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
public class LinkRepository
        implements ILinkRepository {

    private static final InfrastructureLinks LINKS = InfrastructureLinks.INFRASTRUCTURE_LINKS;
    private static final InfrastructureLinksWithHistory HISTORY_VIEW = InfrastructureLinksWithHistory.INFRASTRUCTURE_LINKS_WITH_HISTORY;

    private final DSLContext db;

    @Autowired
    public LinkRepository(final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public LinkPK insertLink(final PersistableLink link) {
        final InfrastructureLinksRecord record = db.newRecord(LINKS);

        record.setInfrastructureLinkExtId(link.externalId().value());
        record.setInfrastructureNetworkType(link.networkType().label());
        record.setInfrastructureLinkGeog(link.geometry());

        record.store();

        return LinkPK.of(record.getInfrastructureLinkId());
    }

    @Override
    @Transactional
    public List<LinkPK> upsert(final Iterable<? extends PersistableLink> links) {
        final String sql = db.insertInto(LINKS,
                                         LINKS.INFRASTRUCTURE_LINK_EXT_ID,
                                         LINKS.INFRASTRUCTURE_NETWORK_TYPE,
                                         LINKS.INFRASTRUCTURE_LINK_GEOG)
                             // parameters 1-3
                             .values((String) null, null, null)
                             .onConflict(LINKS.INFRASTRUCTURE_LINK_EXT_ID)
                             .doUpdate()
                             // parameter 4
                             .set(LINKS.INFRASTRUCTURE_LINK_GEOG, (LineString) null)
                             // parameter 5
                             .where(LINKS.INFRASTRUCTURE_LINK_EXT_ID.eq((String) null))
                             .returningResult(LINKS.INFRASTRUCTURE_LINK_ID)
                             .getSQL();

        final Deque<LinkPK> keys = Lists.newLinkedList();

        // jOOQ doesn't support returning keys from batch operations (https://github.com/jOOQ/jOOQ/issues/3327),
        // so for now we have to resort to JDBC
        db.connection(conn -> {
            try (final PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (final PersistableLink link : links) {
                    final Object geom = LineStringConverter.INSTANCE.to(link.geometry());
                    // The raw sql statement produced by jOOQ contains '?' placeholders (e.g. "VALUES (?, ?, ?::geometry)"),
                    // which we must populate with the corresponding values. Because they are anonymous and positional,
                    // we may need to submit the same value multiple times (once for each placeholder).
                    stmt.setString(1, link.externalId().value());
                    stmt.setString(2, link.networkType().label());
                    stmt.setObject(3, geom);
                    stmt.setObject(4, geom);
                    stmt.setString(5, link.externalId().value());

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
    public Optional<Link> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(LINKS)
                 .where(LINKS.INFRASTRUCTURE_LINK_EXT_ID.eq(externalId.value()))
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

    @Override
    @Transactional(readOnly = true)
    public Set<LinkPK> findAllIds() {
        return db.select(LINKS.INFRASTRUCTURE_LINK_ID)
                 .from(LINKS)
                 .fetchStream()
                 .map(row -> LinkPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    @Override
    @Transactional
    public void updateLinkPoints(final Iterable<? extends LinkGeometry> geometries) {
        final BatchBindStep batch = db.batch(db.update(LINKS)
                                               .set(LINKS.INFRASTRUCTURE_LINK_POINTS, (LineString) null)
                                               .where(LINKS.INFRASTRUCTURE_LINK_EXT_ID.eq((String) null)));
        geometries.forEach(linkGeometry -> batch.bind(linkGeometry.geometry(),
                                                      linkGeometry.externalId().value()));
        batch.execute();
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount()
                 .from(LINKS)
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
    public List<Link> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                 .orderBy(HISTORY_VIEW.INFRASTRUCTURE_LINK_SYS_PERIOD.asc())
                 .fetchStream()
                 .map(Link::of)
                 .collect(List.collector());
    }
}
