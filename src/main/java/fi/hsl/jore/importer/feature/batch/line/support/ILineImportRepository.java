package fi.hsl.jore.importer.feature.batch.line.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;

public interface ILineImportRepository extends IImportRepository<PersistableLine, LinePK> {
}
