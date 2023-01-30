package fi.hsl.jore.importer.feature.network.scheduled_stop_point.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.PersistableScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;

@VisibleForTesting
public interface IScheduledStopPointTestRepository
        extends IBasicCrudRepository<ScheduledStopPointPK, ScheduledStopPoint, PersistableScheduledStopPoint>,
                IHistoryRepository<ScheduledStopPoint> {
}
