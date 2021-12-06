package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImportableScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPointIdMapping;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;
import io.vavr.collection.List;

public interface IScheduledStopPointImportRepository
        extends IImportRepository<ImportableScheduledStopPoint, ScheduledStopPointPK> {

    /**
     * Sets the ids which identifies the scheduled stop points
     * in the Jore 4 database.
     *
     * @param   idMappings   The information that's required to set the transmodel ids
     *                       of scheduled stop points.
     */
    void setTransmodelIds(List<PersistableScheduledStopPointIdMapping> idMappings);
}
