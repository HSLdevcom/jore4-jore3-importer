package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.infrastructure_network.Tables.INFRASTRUCTURE_LINK;
import static fi.hsl.jore.jore4.jooq.internal_service_pattern.tables.ScheduledStopPoint.SCHEDULED_STOP_POINT;

@Repository
public class TransmodelScheduledStopPointRepository implements ITransmodelScheduledStopPointRepository {

    private final DSLContext db;

    @Autowired
    public TransmodelScheduledStopPointRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    public void insert(final List<? extends TransmodelScheduledStopPoint> stopPoints) {
        if (!stopPoints.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(
                                    SCHEDULED_STOP_POINT,
                                    SCHEDULED_STOP_POINT.SCHEDULED_STOP_POINT_ID,
                                    SCHEDULED_STOP_POINT.DIRECTION,
                                    SCHEDULED_STOP_POINT.LABEL,
                                    SCHEDULED_STOP_POINT.LOCATED_ON_INFRASTRUCTURE_LINK_ID,
                                    SCHEDULED_STOP_POINT.MEASURED_LOCATION,
                                    SCHEDULED_STOP_POINT.PRIORITY,
                                    SCHEDULED_STOP_POINT.VALIDITY_START,
                                    SCHEDULED_STOP_POINT.VALIDITY_END
                            )
                            .values((UUID) null, null, null, null, null, null, null, null)
            );

            stopPoints.forEach(stopPoint -> batch.bind(
                    stopPoint.scheduledStopPointId(),
                    stopPoint.directionOnInfraLink().getValue(),
                    stopPoint.label(),
                    db.select(INFRASTRUCTURE_LINK.INFRASTRUCTURE_LINK_ID)
                            .from(INFRASTRUCTURE_LINK)
                            .where(INFRASTRUCTURE_LINK.EXTERNAL_LINK_ID.eq(stopPoint.externalInfrastructureLinkId()))
                            .fetchOneInto(UUID.class),
                    stopPoint.measuredLocation(),
                    stopPoint.priority(),
                    stopPoint.validityStart().orElse(null),
                    stopPoint.validityEnd().orElse(null)
            ));

            batch.execute();
        }
    }
}
