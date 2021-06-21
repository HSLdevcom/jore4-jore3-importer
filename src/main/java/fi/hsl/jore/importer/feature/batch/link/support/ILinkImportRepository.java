package fi.hsl.jore.importer.feature.batch.link.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.ImportableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;

public interface ILinkImportRepository extends IImportRepository<ImportableLink, LinkPK> {
}
