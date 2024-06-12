package fi.hsl.jore.importer.feature.network.line_header.repository;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.line_header.dto.LineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.PersistableLineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.generated.LineHeaderPK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeaders;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeadersWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLineHeadersRecord;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import java.util.Optional;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LineHeaderRepository implements ILineHeaderTestRepository {

    private static final NetworkLineHeaders HEADER = NetworkLineHeaders.NETWORK_LINE_HEADERS;
    private static final NetworkLineHeadersWithHistory HISTORY_VIEW =
            NetworkLineHeadersWithHistory.NETWORK_LINE_HEADERS_WITH_HISTORY;
    private static final TableField<NetworkLineHeadersRecord, UUID> PRIMARY_KEY = HEADER.NETWORK_LINE_HEADER_ID;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public LineHeaderRepository(@Qualifier("importerDsl") final DSLContext db, final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Override
    @Transactional
    public LineHeaderPK insert(final PersistableLineHeader header) {
        final NetworkLineHeadersRecord r = db.newRecord(HEADER);

        r.setNetworkLineId(header.lineId().value());
        r.setNetworkLineHeaderExtId(header.externalId().value());
        r.setNetworkLineHeaderName(jsonbConverter.asJson(header.name()));
        r.setNetworkLineHeaderNameShort(jsonbConverter.asJson(header.nameShort()));
        r.setNetworkLineHeaderOrigin_1(jsonbConverter.asJson(header.origin1()));
        r.setNetworkLineHeaderOrigin_2(jsonbConverter.asJson(header.origin2()));
        r.setNetworkLineHeaderValidDateRange(header.validTime());

        r.store();

        return LineHeaderPK.of(r.getNetworkLineHeaderId());
    }

    @Override
    @Transactional
    public List<LineHeaderPK> insert(final List<PersistableLineHeader> entities) {
        return entities.map(this::insert);
    }

    @Override
    @Transactional
    public List<LineHeaderPK> insert(final PersistableLineHeader... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public LineHeaderPK update(final LineHeader header) {
        final NetworkLineHeadersRecord r = Optional.ofNullable(db.selectFrom(HEADER)
                        .where(PRIMARY_KEY.eq(header.pk().value()))
                        .fetchAny())
                .orElseThrow();

        r.setNetworkLineHeaderName(jsonbConverter.asJson(header.name()));
        r.setNetworkLineHeaderNameShort(jsonbConverter.asJson(header.nameShort()));
        r.setNetworkLineHeaderOrigin_1(jsonbConverter.asJson(header.origin1()));
        r.setNetworkLineHeaderOrigin_2(jsonbConverter.asJson(header.origin2()));
        r.setNetworkLineHeaderValidDateRange(header.validTime());

        r.store();

        return LineHeaderPK.of(r.getNetworkLineHeaderId());
    }

    @Override
    @Transactional
    public List<LineHeaderPK> update(final List<LineHeader> entities) {
        return entities.map(this::update);
    }

    @Override
    @Transactional
    public List<LineHeaderPK> update(final LineHeader... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LineHeader> findById(final LineHeaderPK id) {
        return db.selectFrom(HEADER)
                .where(PRIMARY_KEY.eq(id.value()))
                .fetchStream()
                .map(record -> LineHeader.from(record, jsonbConverter))
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LineHeader> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(HEADER)
                .where(HEADER.NETWORK_LINE_HEADER_EXT_ID.eq(externalId.value()))
                .fetchStream()
                .map(record -> LineHeader.from(record, jsonbConverter))
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LineHeader> findAll() {
        return db.selectFrom(HEADER)
                .fetchStream()
                .map(record -> LineHeader.from(record, jsonbConverter))
                .collect(List.collector());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<LineHeaderPK> findAllIds() {
        return db.select(PRIMARY_KEY)
                .from(HEADER)
                .fetchStream()
                .map(row -> LineHeaderPK.of(row.value1()))
                .collect(HashSet.collector());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount().from(HEADER).fetchOne(0, int.class);
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
    public List<LineHeader> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                .orderBy(HISTORY_VIEW.NETWORK_LINE_HEADER_SYS_PERIOD.asc())
                .fetchStream()
                .map(record -> LineHeader.from(record, jsonbConverter))
                .collect(List.collector());
    }
}
