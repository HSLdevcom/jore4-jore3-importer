package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4LineExternalId;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

import static fi.hsl.jore.jore4.jooq.route.Tables.LINE_EXTERNAL_ID;

@Repository
public class Jore4LineExternalIdRepository implements IJore4LineExternalIdRepository {

    private final DSLContext db;

    @Autowired
    public Jore4LineExternalIdRepository(@Qualifier("jore4Dsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    public void insert(List<? extends Jore4LineExternalId> lineExternalIds) {
         if (!lineExternalIds.isEmpty()) {
            final BatchBindStep batch = db.batch(db.insertInto(
                                    LINE_EXTERNAL_ID,
                                    LINE_EXTERNAL_ID.LABEL,
                                    LINE_EXTERNAL_ID.EXTERNAL_ID
                            )
                            .values((String)null, null)
                            .onConflictDoNothing()
            );

            lineExternalIds.forEach(lineExternalId -> batch.bind(
                    lineExternalId.label(),
                    lineExternalId.externalId()
            ));

            batch.execute();
        }
    }
}
