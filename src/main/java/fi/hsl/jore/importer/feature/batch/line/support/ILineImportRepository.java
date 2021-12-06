package fi.hsl.jore.importer.feature.batch.line.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;

import java.util.UUID;

public interface ILineImportRepository extends IImportRepository<PersistableLine, LinePK> {

    /**
     * Sets the id which identifies the line  in the Jore 4 database.
     * @param externalId    A unique id which identifies the line.
     * @param transmodelId  A unique id which identifies the line in Jore 4 database.
     */
    void setTransmodelId(ExternalId externalId, UUID transmodelId);
}
