package fi.hsl.jore.importer.feature.batch.line_header.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.line_header.dto.ImportableLineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.generated.LineHeaderPK;

public interface ILineHeaderImportRepository extends IImportRepository<ImportableLineHeader, LineHeaderPK> {
}
