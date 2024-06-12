package fi.hsl.jore.importer.feature.infrastructure.link.repository;

import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;

/**
 * Only for testing, use {@link
 * fi.hsl.jore.importer.feature.batch.link.support.ILinkImportRepository this} to
 * insert/update/delete links.
 */
public interface ILinkTestRepository
        extends IBasicCrudRepository<LinkPK, Link, PersistableLink>, IHistoryRepository<Link> {}
