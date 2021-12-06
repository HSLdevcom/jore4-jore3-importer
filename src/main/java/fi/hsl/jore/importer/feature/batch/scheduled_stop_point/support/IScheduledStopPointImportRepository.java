package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImportableScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;

import java.util.UUID;

public interface IScheduledStopPointImportRepository
        extends IImportRepository<ImportableScheduledStopPoint, ScheduledStopPointPK> {

    /**
     * Sets the id which identifies the scheduled stop point
     * in the Jore 4 database.
     *
     * @param elyNumber     A unique id which identifies the scheduled stop point.
     * @param transmodelId  A unique id which identifies the scheduled stop point
     *                      in Jore 4 database.
     */
    void setTransmodelId(String elyNumber, UUID transmodelId);
}
