package fi.hsl.jore.importer.feature.batch.link_shape.support;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.Jore3LinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.generated.LinkShapePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinkShapes;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinkShapesStaging;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinks;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static fi.hsl.jore.importer.util.PostgisUtil.geometryEquals;
import static org.jooq.impl.DSL.selectOne;

@Repository
public class LinkShapeImportRepository
        extends AbstractImportRepository<Jore3LinkShape, LinkShapePK>
        implements ILinkShapeImportRepository {

    private static final InfrastructureLinkShapesStaging STAGING_TABLE = InfrastructureLinkShapesStaging.INFRASTRUCTURE_LINK_SHAPES_STAGING;
    private static final InfrastructureLinkShapes TARGET_TABLE = InfrastructureLinkShapes.INFRASTRUCTURE_LINK_SHAPES;
    private static final InfrastructureLinks LINKS_TABLE = InfrastructureLinks.INFRASTRUCTURE_LINKS;

    private final DSLContext db;

    @Autowired
    public LinkShapeImportRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE)
          .execute();
    }

    @Override
    @Transactional
    public void submitToStaging(final Iterable<? extends Jore3LinkShape> shapes) {
        final BatchBindStep batch = db.batch(db.insertInto(STAGING_TABLE,
                                                           STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                                                           STAGING_TABLE.INFRASTRUCTURE_LINK_SHAPE)
                                               .values((String) null, null));

        shapes.forEach(shape -> batch.bind(shape.linkExternalId().value(),
                                           shape.geometry()));

        batch.execute();
    }

    protected Set<LinkShapePK> delete() {
        return db.deleteFrom(TARGET_TABLE)
                 // Find rows which are missing from the latest dataset
                 .whereNotExists(selectOne()
                                         .from(STAGING_TABLE)
                                         .where(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID.eq(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID)))
                 .returningResult(TARGET_TABLE.INFRASTRUCTURE_LINK_SHAPE_ID)
                 .fetch()
                 .stream()
                 .map(row -> LinkShapePK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<LinkShapePK> update() {
        return db.update(TARGET_TABLE)
                 // What fields to update
                 .set(TARGET_TABLE.INFRASTRUCTURE_LINK_SHAPE,
                      STAGING_TABLE.INFRASTRUCTURE_LINK_SHAPE)
                 .from(STAGING_TABLE)
                 // Find source rows..
                 .where(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID
                                .eq(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID))
                 // .. with different points
                 .andNot(geometryEquals(TARGET_TABLE.INFRASTRUCTURE_LINK_SHAPE,
                                        STAGING_TABLE.INFRASTRUCTURE_LINK_SHAPE))
                 .returningResult(TARGET_TABLE.INFRASTRUCTURE_LINK_SHAPE_ID)
                 .fetch()
                 .stream()
                 .map(row -> LinkShapePK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<LinkShapePK> insert() {
        return db.insertInto(TARGET_TABLE)
                 .columns(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                          TARGET_TABLE.INFRASTRUCTURE_LINK_ID,
                          TARGET_TABLE.INFRASTRUCTURE_LINK_SHAPE)
                 .select(db.select(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID,
                                   LINKS_TABLE.INFRASTRUCTURE_LINK_ID,
                                   STAGING_TABLE.INFRASTRUCTURE_LINK_SHAPE)
                           .from(STAGING_TABLE)
                           .leftJoin(LINKS_TABLE).on(LINKS_TABLE.INFRASTRUCTURE_LINK_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID))
                           .whereNotExists(selectOne()
                                                   .from(TARGET_TABLE)
                                                   .where(TARGET_TABLE.INFRASTRUCTURE_LINK_EXT_ID.eq(STAGING_TABLE.INFRASTRUCTURE_LINK_EXT_ID))))
                 .returningResult(TARGET_TABLE.INFRASTRUCTURE_LINK_SHAPE_ID)
                 .fetch()
                 .stream()
                 .map(row -> LinkShapePK.of(row.value1()))
                 .collect(HashSet.collector());
    }
}
