package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Route;
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
public class Jore4RouteRepository implements IJore4RouteRepository {

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public Jore4RouteRepository(@Qualifier("jore4Dsl") final DSLContext db,
                                final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Transactional
    @Override
    public void insert(final List<? extends Jore4Route> routes) {
        if (!routes.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(
                                    ROUTE_,
                                    ROUTE_.ROUTE_ID,
                                    ROUTE_.NAME_I18N,
                                    ROUTE_.DESCRIPTION_I18N,
                                    ROUTE_.DIRECTION,
                                    ROUTE_.LABEL,
                                    ROUTE_.VARIANT,
                                    ROUTE_.ON_LINE_ID,
                                    ROUTE_.PRIORITY,
                                    ROUTE_.VALIDITY_START,
                                    ROUTE_.VALIDITY_END,
                                    ROUTE_.LEGACY_HSL_MUNICIPALITY_CODE
                            )
                            .values((UUID) null, null, null, null, null, null, null, null, null, null, null)
            );

            routes.forEach(route -> batch.bind(
                    route.routeId(),
                    jsonbConverter.asJson(route.description()),
                    jsonbConverter.asJson(route.description()),
                    route.direction().getValue(),
                    route.label(),
                    route.hiddenVariant().orElse(null),
                    route.lineId(),
                    route.priority(),
                    route.validityStart().orElse(null),
                    route.validityEnd().orElse(null),
                    route.legacyHslMunicipalityCode().getJore4Value()
            ));

            batch.execute();
        }
    }
}
