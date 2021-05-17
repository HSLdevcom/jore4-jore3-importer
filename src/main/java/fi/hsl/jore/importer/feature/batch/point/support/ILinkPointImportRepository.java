package fi.hsl.jore.importer.feature.batch.point.support;

import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.batch.util.IImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;

public interface ILinkPointImportRepository extends IImportRepository<LinkGeometry, LinkPK> {
}
