package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelScheduledStopPoint;
import fi.hsl.jore.importer.feature.transmodel.entity.VehicleMode;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import fi.hsl.jore.jore4.jooq.internal_service_pattern.Routines;

import static fi.hsl.jore.jore4.jooq.infrastructure_network.Tables.INFRASTRUCTURE_LINK;

@Repository
public class TransmodelScheduledStopPointRepository implements ITransmodelScheduledStopPointRepository {

    private final DSLContext db;

    @Autowired
    public TransmodelScheduledStopPointRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Transactional
    @Override
    public void insert(final List<? extends TransmodelScheduledStopPoint> stopPoints) {
        if (!stopPoints.isEmpty()) {
            stopPoints.forEach(stopPoint -> Routines.insertScheduledStopPointWithVehicleMode(db.configuration(),
                    stopPoint.scheduledStopPointId(),
                    stopPoint.measuredLocation(),
                    db.select(INFRASTRUCTURE_LINK.INFRASTRUCTURE_LINK_ID)
                            .from(INFRASTRUCTURE_LINK)
                            .where(INFRASTRUCTURE_LINK.EXTERNAL_LINK_ID.eq(stopPoint.externalInfrastructureLinkId()))
                            .fetchOneInto(UUID.class),
                    stopPoint.directionOnInfraLink().getValue(),
                    stopPoint.label(),
                    stopPoint.validityStart().orElse(null),
                    stopPoint.validityEnd().orElse(null),
                    stopPoint.priority(),
                    VehicleMode.BUS.getValue()
            ));
        }
    }
}
