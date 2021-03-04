package fi.hsl.jore.importer.feature.infrastructure.link.repository;

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

import java.util.Optional;

@Repository
public class LinkRepository
        implements ILinkRepository {

    private static final InfrastructureLinks LINKS = InfrastructureLinks.INFRASTRUCTURE_LINKS;

    private final DSLContext db;

    @Autowired
    public LinkRepository(final DSLContext db) {
        this.db = db;
    }

    @Transactional
    public LinkPK insertLink(final PersistableLink link) {
        final InfrastructureLinksRecord record = db.newRecord(LINKS);

        record.setInfrastructureNetworkTypeId(link.networkTypePk().value());
        record.setInfrastructureLinkGeog(link.geometry());

        record.store();

        return LinkPK.of(record.getInfrastructureLinkId());
    }

    @Transactional
    public void insertLinks(final List<PersistableLink> links) {
        db.batch(links.map(link -> db.insertInto(LINKS,
                                                 LINKS.INFRASTRUCTURE_NETWORK_TYPE_ID,
                                                 LINKS.INFRASTRUCTURE_LINK_GEOG)
                                     .values(link.networkTypePk().value(),
                                             link.geometry()))
                      .toJavaList())
          .execute();
    }

    @Transactional(readOnly = true)
    public Optional<Link> findById(final LinkPK linkId) {
        return db.selectFrom(LINKS)
                 .where(LINKS.INFRASTRUCTURE_LINK_ID.eq(linkId.value()))
                 .fetchStream()
                 .map(Link::of)
                 .findFirst();
    }

    @Transactional(readOnly = true)
    public List<Link> findAll() {
        return db.selectFrom(LINKS)
                 .fetchStream()
                 .map(Link::of)
                 .collect(List.collector());
    }
}
