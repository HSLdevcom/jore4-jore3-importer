package fi.hsl.jore.importer.feature.batch.link_shape.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.ImportableLinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.generated.LinkShapePK;

public interface ILinkShapeImportRepository extends IImportRepository<ImportableLinkShape, LinkShapePK> {
}
