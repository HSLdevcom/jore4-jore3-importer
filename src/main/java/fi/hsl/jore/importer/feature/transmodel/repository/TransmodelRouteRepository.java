package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRoute;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.route.Tables.ROUTE_;

@Repository
public class TransmodelRouteRepository implements ITransmodelRouteRepository {

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public TransmodelRouteRepository(@Qualifier("jore4Dsl") final DSLContext db,
                                     final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Transactional
    @Override
    public void insert(final List<? extends TransmodelRoute> routes) {
        if (!routes.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(
                                    ROUTE_,
                                    ROUTE_.ROUTE_ID,
                                    ROUTE_.DESCRIPTION_I18N,
                                    ROUTE_.DIRECTION,
                                    ROUTE_.LABEL,
                                    ROUTE_.ON_LINE_ID,
                                    ROUTE_.PRIORITY,
                                    ROUTE_.STARTS_FROM_SCHEDULED_STOP_POINT_ID,
                                    ROUTE_.ENDS_AT_SCHEDULED_STOP_POINT_ID,
                                    ROUTE_.VALIDITY_START,
                                    ROUTE_.VALIDITY_END
                            )
                            .values((UUID) null, null, null, null, null, null, null, null, null, null)
            );

            routes.forEach(route -> batch.bind(
                    route.routeId(),
                    jsonbConverter.asJson(route.description()),
                    route.direction().getValue(),
                    route.label(),
                    route.lineId(),
                    route.priority(),
                    route.startScheduledStopPointId(),
                    route.endScheduledStopPointId(),
                    route.validityStart().orElse(null),
                    route.validityEnd().orElse(null)
            ));

            batch.execute();
        }
    }
}
