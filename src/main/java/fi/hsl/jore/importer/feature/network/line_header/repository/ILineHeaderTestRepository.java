package fi.hsl.jore.importer.feature.network.line_header.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.network.line_header.dto.LineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.PersistableLineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.generated.LineHeaderPK;

/**
 * Only for testing, use {@link fi.hsl.jore.importer.feature.batch.line_header.support.ILineHeaderImportRepository this} to
 * insert/update/delete lines.
 */
@VisibleForTesting
public interface ILineHeaderTestRepository extends IBasicCrudRepository<LineHeaderPK, LineHeader, PersistableLineHeader>,
                                                   IHistoryRepository<LineHeader> {
}
