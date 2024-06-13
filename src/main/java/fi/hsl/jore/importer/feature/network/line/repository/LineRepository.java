package fi.hsl.jore.importer.feature.network.line.repository;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.line.dto.Line;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLines;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLinesWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkLinesRecord;
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
public class LineRepository implements ILineTestRepository {

    private static final NetworkLines LINE = NetworkLines.NETWORK_LINES;
    private static final NetworkLinesWithHistory HISTORY_VIEW = NetworkLinesWithHistory.NETWORK_LINES_WITH_HISTORY;
    private static final TableField<NetworkLinesRecord, UUID> PRIMARY_KEY = LINE.NETWORK_LINE_ID;

    private final DSLContext db;

    @Autowired
    public LineRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public LinePK insert(final PersistableLine line) {
        final NetworkLinesRecord r = db.newRecord(LINE);

        r.setNetworkLineExtId(line.externalId().value());
        r.setNetworkLineNumber(line.lineNumber());
        r.setInfrastructureNetworkType(line.networkType().label());
        r.setNetworkLineTypeOfLine(line.typeOfLine().getValue());
        r.setNetworkLineLegacyHslMunicipalityCode(
                (line.lineLegacyHslMunicipalityCode().name()));

        r.store();

        return LinePK.of(r.getNetworkLineId());
    }

    @Override
    @Transactional
    public List<LinePK> insert(final List<PersistableLine> entities) {
        return entities.stream().map(this::insert).toList();
    }

    @Override
    @Transactional
    public List<LinePK> insert(final PersistableLine... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public LinePK update(final Line line) {
        final NetworkLinesRecord r = Optional.ofNullable(db.selectFrom(LINE)
                        .where(PRIMARY_KEY.eq(line.pk().value()))
                        .fetchAny())
                .orElseThrow();

        r.setInfrastructureNetworkType(line.networkType().label());

        r.store();

        return LinePK.of(r.getNetworkLineId());
    }

    @Override
    @Transactional
    public List<LinePK> update(final List<Line> entities) {
        return entities.stream().map(this::update).toList();
    }

    @Override
    @Transactional
    public List<LinePK> update(final Line... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Line> findById(final LinePK id) {
        return db.selectFrom(LINE)
                .where(PRIMARY_KEY.eq(id.value()))
                .fetchStream()
                .map(Line::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Line> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(LINE)
                .where(LINE.NETWORK_LINE_EXT_ID.eq(externalId.value()))
                .fetchStream()
                .map(Line::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Line> findAll() {
        return db.selectFrom(LINE).fetchStream().map(Line::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<LinePK> findAllIds() {
        return db.select(PRIMARY_KEY)
                .from(LINE)
                .fetchStream()
                .map(row -> LinePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount().from(LINE).fetchOne(0, int.class);
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
    public List<Line> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                .orderBy(HISTORY_VIEW.NETWORK_LINE_SYS_PERIOD.asc())
                .fetchStream()
                .map(Line::from)
                .toList();
    }
}
