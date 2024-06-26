package fi.hsl.jore.importer.feature.infrastructure.link_shape.repository;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.LinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.PersistableLinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.generated.LinkShapePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinkShapes;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureLinkShapesWithHistory;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinkShapesRecord;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LinkShapeRepository implements ILinkShapeTestRepository {

    private static final InfrastructureLinkShapes SHAPES = InfrastructureLinkShapes.INFRASTRUCTURE_LINK_SHAPES;
    private static final InfrastructureLinkShapesWithHistory HISTORY_VIEW =
            InfrastructureLinkShapesWithHistory.INFRASTRUCTURE_LINK_SHAPES_WITH_HISTORY;
    private static final TableField<InfrastructureLinkShapesRecord, UUID> PRIMARY_KEY =
            SHAPES.INFRASTRUCTURE_LINK_SHAPE_ID;

    private final DSLContext db;

    @Autowired
    public LinkShapeRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public LinkShapePK insert(final PersistableLinkShape entity) {
        final InfrastructureLinkShapesRecord record = db.newRecord(SHAPES);

        record.setInfrastructureLinkExtId(entity.linkExternalId().value());
        record.setInfrastructureLinkId(entity.linkId().value());
        record.setInfrastructureLinkShape(entity.geometry());

        record.store();

        return LinkShapePK.of(record.getInfrastructureLinkShapeId());
    }

    @Override
    @Transactional
    public List<LinkShapePK> insert(final List<PersistableLinkShape> entities) {
        return entities.stream().map(this::insert).toList();
    }

    @Override
    @Transactional
    public List<LinkShapePK> insert(final PersistableLinkShape... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public LinkShapePK update(final LinkShape shape) {
        final InfrastructureLinkShapesRecord r = Optional.ofNullable(db.selectFrom(SHAPES)
                        .where(PRIMARY_KEY.eq(shape.pk().value()))
                        .fetchAny())
                .orElseThrow();

        r.setInfrastructureLinkShape(shape.geometry());

        r.store();

        return LinkShapePK.of(r.getInfrastructureLinkShapeId());
    }

    @Override
    @Transactional
    public List<LinkShapePK> update(final List<LinkShape> entities) {
        return entities.stream().map(this::update).toList();
    }

    @Override
    @Transactional
    public List<LinkShapePK> update(final LinkShape... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LinkShape> findById(final LinkShapePK shapeId) {
        return db.selectFrom(SHAPES)
                .where(PRIMARY_KEY.eq(shapeId.value()))
                .fetchStream()
                .map(LinkShape::from)
                .findFirst();
    }

    /**
     * Note that because Link shapes do not have a proper external id of their own, the search here uses the parent link
     * external id.
     *
     * @param externalId External id of the parent link
     * @return LinkShape for the parent link if one exists
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LinkShape> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(SHAPES)
                .where(SHAPES.INFRASTRUCTURE_LINK_EXT_ID.eq(externalId.value()))
                .fetchStream()
                .map(LinkShape::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LinkShape> findAll() {
        return db.selectFrom(SHAPES).fetchStream().map(LinkShape::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<LinkShapePK> findAllIds() {
        return db.select(PRIMARY_KEY)
                .from(SHAPES)
                .fetchStream()
                .map(row -> LinkShapePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount().from(SHAPES).fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public int countHistory() {
        //noinspection ConstantConditions
        return db.selectCount().from(HISTORY_VIEW).fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean empty() {
        return count() == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emptyHistory() {
        return countHistory() == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LinkShape> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                .orderBy(HISTORY_VIEW.INFRASTRUCTURE_LINK_SHAPE_SYS_PERIOD.asc())
                .fetchStream()
                .map(LinkShape::from)
                .toList();
    }
}
