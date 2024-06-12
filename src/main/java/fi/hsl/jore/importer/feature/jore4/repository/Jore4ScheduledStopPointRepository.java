package fi.hsl.jore.importer.feature.jore4.repository;

import static fi.hsl.jore.jore4.jooq.infrastructure_network.Tables.INFRASTRUCTURE_LINK;
import static fi.hsl.jore.jore4.jooq.timing_pattern.Tables.TIMING_PLACE;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import fi.hsl.jore.jore4.jooq.internal_service_pattern.Routines;
import java.util.UUID;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class Jore4ScheduledStopPointRepository implements IJore4ScheduledStopPointRepository {

    private final DSLContext db;

    @Autowired
    public Jore4ScheduledStopPointRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Transactional
    @Override
    public void insert(final Iterable<? extends Jore4ScheduledStopPoint> stopPoints) {
        for (final Jore4ScheduledStopPoint stopPoint : stopPoints) {
            final UUID infrastructureLinkId =
                    db.select(INFRASTRUCTURE_LINK.INFRASTRUCTURE_LINK_ID)
                            .from(INFRASTRUCTURE_LINK)
                            .where(
                                    INFRASTRUCTURE_LINK.EXTERNAL_LINK_ID.eq(
                                            stopPoint.externalInfrastructureLinkId()))
                            .fetchOneInto(UUID.class);

            final UUID timingPlaceId =
                    stopPoint
                            .timingPlaceLabel()
                            .map(
                                    timingPlaceLabel ->
                                            db.select(TIMING_PLACE.TIMING_PLACE_ID)
                                                    .from(TIMING_PLACE)
                                                    .where(TIMING_PLACE.LABEL.eq(timingPlaceLabel))
                                                    .fetchOneInto(UUID.class))
                            .orElse(null);

            Routines.insertScheduledStopPointWithVehicleMode(
                    db.configuration(),
                    stopPoint.scheduledStopPointId(),
                    stopPoint.measuredLocation(),
                    infrastructureLinkId,
                    stopPoint.directionOnInfraLink().getValue(),
                    stopPoint.label(),
                    stopPoint.validityStart().orElse(null),
                    stopPoint.validityEnd().orElse(null),
                    stopPoint.priority(),
                    VehicleMode.BUS.getValue(),
                    timingPlaceId);
        }
    }
}
