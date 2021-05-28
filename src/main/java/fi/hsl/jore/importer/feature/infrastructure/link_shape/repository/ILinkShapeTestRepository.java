package fi.hsl.jore.importer.feature.infrastructure.link_shape.repository;

import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.LinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.PersistableLinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.generated.LinkShapePK;

/**
 * Only for testing, use {@link fi.hsl.jore.importer.feature.batch.link_shape.support.ILinkShapeImportRepository this} to
 * insert/update/delete link shapes.
 */
public interface ILinkShapeTestRepository extends IBasicCrudRepository<LinkShapePK, LinkShape, PersistableLinkShape>,
                                                  IHistoryRepository<LinkShape> {
}
