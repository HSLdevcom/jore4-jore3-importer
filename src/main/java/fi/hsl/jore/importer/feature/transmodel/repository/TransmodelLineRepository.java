package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

import static fi.hsl.jore.jore4.jooq.route.Tables.LINE;

@Repository
public class TransmodelLineRepository implements ITransmodelLineRepository {

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public TransmodelLineRepository(@Qualifier("jore4Dsl") final DSLContext db,
                                    final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Override
    public void insert(final List<? extends TransmodelLine> lines) {
        final BatchBindStep batch = db.batch(db.insertInto(
                LINE,
                LINE.NAME_I18N,
                LINE.PRIMARY_VEHICLE_MODE,
                LINE.SHORT_NAME_I18N
        )
                .values((String) null, null, null)
        );

        lines.forEach(line -> batch.bind(
                jsonbConverter.asJson(line.name()),
                line.primaryVehicleMode().getValue(),
                jsonbConverter.asJson(line.shortName())
        ));

        batch.execute();
    }
}
