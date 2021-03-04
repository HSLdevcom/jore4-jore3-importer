package fi.hsl.jore.importer.feature.infrastructure.link.repository;

import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import io.vavr.collection.List;

import java.util.Optional;

public interface ILinkRepository {
    LinkPK insertLink(PersistableLink link);

    List<LinkPK> insertLinks(Iterable<? extends PersistableLink> links);

    Optional<Link> findById(LinkPK linkId);

    List<Link> findAll();
}
