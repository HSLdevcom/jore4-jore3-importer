package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.route.Tables.LINE;

@Repository
public class Jore4LineRepository implements IJore4LineRepository {

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public Jore4LineRepository(@Qualifier("jore4Dsl") final DSLContext db,
                               final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Override
    public void insert(final Iterable<? extends Jore4Line> lines) {
        BatchBindStep batch = db.batch(db.insertInto(
                LINE,
                LINE.LINE_ID,
                LINE.LABEL,
                LINE.NAME_I18N,
                LINE.PRIMARY_VEHICLE_MODE,
                LINE.TYPE_OF_LINE,
                LINE.TRANSPORT_TARGET,
                LINE.PRIORITY,
                LINE.SHORT_NAME_I18N,
                LINE.VALIDITY_START,
                LINE.VALIDITY_END,
                LINE.LEGACY_HSL_MUNICIPALITY_CODE
            )
            .values((UUID) null, null, null, null, null, null, null, null, null, null, null));

        for (final Jore4Line line : lines) {
            batch = batch.bind(
                line.lineId(),
                line.label(),
                jsonbConverter.asJson(line.name()),
                line.primaryVehicleMode().getValue(),
                line.typeOfLine().getValue(),
                "helsinki_internal_traffic", // TODO: replace this with imported value
                line.priority(),
                jsonbConverter.asJson(line.shortName()),
                line.validityStart().orElse(null),
                line.validityEnd().orElse(null),
                line.legacyHslMunicipalityCode().getJore4Value()
            );
        }

        if (batch.size() > 0) {
            batch.execute();
        }
    }
}
