package fi.hsl.jore.importer.feature.batch.line.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLineTransmodelId;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;

import java.util.UUID;

public interface ILineImportRepository extends IImportRepository<PersistableLine, LinePK> {

    /**
     * Sets the transmodel ids which identifies the lines in the Jore 4 database.
     *
     * @param transmodelIds The information that's required to set the transmodel ids
     *                      of lines.
     */
    void setTransmodelIds(Iterable<? extends PersistableLineTransmodelId> transmodelIds);
}
