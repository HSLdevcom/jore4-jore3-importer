package fi.hsl.jore.importer.feature.infrastructure.link.repository;

import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.infrastructure.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.infrastructure.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import io.vavr.collection.List;

public interface ILinkRepository extends IBasicCrudRepository<LinkPK, Link>,
                                         IHistoryRepository<Link> {
    LinkPK insertLink(PersistableLink link);

    List<LinkPK> upsert(Iterable<? extends PersistableLink> links);

    void updateLinkPoints(Iterable<? extends LinkGeometry> links);
}
