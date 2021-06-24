package fi.hsl.jore.importer.feature.infrastructure.link.repository;

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
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class LinkRepository
        implements ILinkTestRepository {

    private static final InfrastructureLinks LINKS = InfrastructureLinks.INFRASTRUCTURE_LINKS;
    private static final InfrastructureLinksWithHistory HISTORY_VIEW = InfrastructureLinksWithHistory.INFRASTRUCTURE_LINKS_WITH_HISTORY;
    private static final TableField<InfrastructureLinksRecord, UUID> PRIMARY_KEY = LINKS.INFRASTRUCTURE_LINK_ID;

    private final DSLContext db;

    @Autowired
    public LinkRepository(final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public LinkPK insert(final PersistableLink entity) {
        final InfrastructureLinksRecord record = db.newRecord(LINKS);

        record.setInfrastructureLinkExtId(entity.externalId().value());
        record.setInfrastructureNetworkType(entity.networkType().label());
        record.setInfrastructureLinkGeog(entity.geometry());
        record.setInfrastructureLinkStartNode(entity.fromNode().value());
        record.setInfrastructureLinkEndNode(entity.toNode().value());

        record.store();

        return LinkPK.of(record.getInfrastructureLinkId());
    }

    @Override
    @Transactional
    public List<LinkPK> insert(final List<PersistableLink> entities) {
        return entities.map(this::insert);
    }

    @Override
    @Transactional
    public List<LinkPK> insert(final PersistableLink... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public LinkPK update(final Link link) {
        final InfrastructureLinksRecord r =
                Optional.ofNullable(db.selectFrom(LINKS)
                                      .where(PRIMARY_KEY.eq(link.pk().value()))
                                      .fetchAny())
                        .orElseThrow();

        r.setInfrastructureLinkGeog(link.geometry());
        r.setInfrastructureLinkStartNode(link.startNode().value());
        r.setInfrastructureLinkEndNode(link.endNode().value());

        r.store();

        return LinkPK.of(r.getInfrastructureLinkId());
    }

    @Override
    @Transactional
    public List<LinkPK> update(final List<Link> entities) {
        return entities.map(this::update);
    }

    @Override
    @Transactional
    public List<LinkPK> update(final Link... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Link> findById(final LinkPK linkId) {
        return db.selectFrom(LINKS)
                 .where(PRIMARY_KEY.eq(linkId.value()))
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
        return db.select(PRIMARY_KEY)
                 .from(LINKS)
                 .fetchStream()
                 .map(row -> LinkPK.of(row.value1()))
                 .collect(HashSet.collector());
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
