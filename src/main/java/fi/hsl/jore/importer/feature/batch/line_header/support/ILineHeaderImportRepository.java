package fi.hsl.jore.importer.feature.batch.line_header.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLineIdMapping;
import fi.hsl.jore.importer.feature.network.line_header.dto.Jore3LineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.generated.LineHeaderPK;

public interface ILineHeaderImportRepository
        extends IImportRepository<Jore3LineHeader, LineHeaderPK> {

    /**
     * Sets the Jore 4 ids which identifies the line in the Jore 4 database.
     *
     * @param idMappings The information that's required to set the Jore 4 ids of lines.
     */
    void setJore4Ids(Iterable<PersistableLineIdMapping> idMappings);
}
