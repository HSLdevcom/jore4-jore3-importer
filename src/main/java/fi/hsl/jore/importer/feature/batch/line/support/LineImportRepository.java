package fi.hsl.jore.importer.feature.batch.line.support;

import static org.jooq.impl.DSL.selectOne;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLines;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLinesStaging;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LineImportRepository extends AbstractImportRepository<PersistableLine, LinePK>
        implements ILineImportRepository {

    private static final NetworkLinesStaging STAGING_TABLE = NetworkLinesStaging.NETWORK_LINES_STAGING;
    private static final NetworkLines TARGET_TABLE = NetworkLines.NETWORK_LINES;

    private final DSLContext db;

    @Autowired
    public LineImportRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE).execute();
    }

    @Override
    @Transactional
    public void submitToStaging(final Iterable<? extends PersistableLine> lines) {
        final BatchBindStep batch = db.batch(db.insertInto(
                        STAGING_TABLE,
                        STAGING_TABLE.NETWORK_LINE_EXT_ID,
                        STAGING_TABLE.NETWORK_LINE_NUMBER,
                        STAGING_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                        STAGING_TABLE.NETWORK_LINE_TYPE_OF_LINE,
                        STAGING_TABLE.NETWORK_LINE_LEGACY_HSL_MUNICIPALITY_CODE)
                .values((String) null, null, null, null, null));

        lines.forEach(line -> batch.bind(
                line.externalId().value(),
                line.lineNumber(),
                line.networkType().label(),
                line.typeOfLine().getValue(),
                line.lineLegacyHslMunicipalityCode().name()));

        if (batch.size() > 0) {
            batch.execute();
        }
    }

    protected Set<LinePK> delete() {
        return db
                .deleteFrom(TARGET_TABLE)
                // Find rows which are missing from the latest dataset
                .whereNotExists(selectOne()
                        .from(STAGING_TABLE)
                        .where(STAGING_TABLE.NETWORK_LINE_EXT_ID.eq(TARGET_TABLE.NETWORK_LINE_EXT_ID)))
                .returningResult(TARGET_TABLE.NETWORK_LINE_ID)
                .fetch()
                .stream()
                .map(row -> LinePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }

    protected Set<LinePK> update() {
        // TODO: At the moment we don't have any updatable fields in the NETWORK_LINES tables,
        //       but this may change when we decide how to handle e.g. PublicTransportDestination fields
        return Collections.emptySet();
    }

    protected Set<LinePK> insert() {
        return db
                .insertInto(TARGET_TABLE)
                .columns(
                        TARGET_TABLE.NETWORK_LINE_EXT_ID,
                        TARGET_TABLE.NETWORK_LINE_NUMBER,
                        TARGET_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                        TARGET_TABLE.NETWORK_LINE_TYPE_OF_LINE,
                        TARGET_TABLE.NETWORK_LINE_LEGACY_HSL_MUNICIPALITY_CODE)
                .select(db.select(
                                STAGING_TABLE.NETWORK_LINE_EXT_ID,
                                STAGING_TABLE.NETWORK_LINE_NUMBER,
                                STAGING_TABLE.INFRASTRUCTURE_NETWORK_TYPE,
                                STAGING_TABLE.NETWORK_LINE_TYPE_OF_LINE,
                                STAGING_TABLE.NETWORK_LINE_LEGACY_HSL_MUNICIPALITY_CODE)
                        .from(STAGING_TABLE)
                        .whereNotExists(selectOne()
                                .from(TARGET_TABLE)
                                .where(TARGET_TABLE.NETWORK_LINE_EXT_ID.eq(STAGING_TABLE.NETWORK_LINE_EXT_ID))))
                .returningResult(TARGET_TABLE.NETWORK_LINE_ID)
                .fetch()
                .stream()
                .map(row -> LinePK.of(row.value1()))
                .collect(Collectors.toUnmodifiableSet());
    }
}
