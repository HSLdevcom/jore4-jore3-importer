package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.Jore3ScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPointIdMapping;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;
import io.vavr.collection.List;

public interface IScheduledStopPointImportRepository
        extends IImportRepository<Jore3ScheduledStopPoint, ScheduledStopPointPK> {

    /**
     * Sets the ids which identifies the scheduled stop points
     * in the Jore 4 database.
     *
     * @param   idMappings   The information that's required to set the Jore 4 ids
     *                       of scheduled stop points.
     */
    void setJore4Ids(Iterable<PersistableScheduledStopPointIdMapping> idMappings);
}
