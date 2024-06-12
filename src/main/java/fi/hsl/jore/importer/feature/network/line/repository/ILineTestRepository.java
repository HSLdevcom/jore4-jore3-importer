package fi.hsl.jore.importer.feature.network.line.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.network.line.dto.Line;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;

/**
 * Only for testing, use {@link fi.hsl.jore.importer.feature.batch.line.support.ILineImportRepository this} to
 * insert/update/delete lines.
 */
@VisibleForTesting
public interface ILineTestRepository
        extends IBasicCrudRepository<LinePK, Line, PersistableLine>, IHistoryRepository<Line> {}
