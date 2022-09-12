package fi.hsl.jore.importer.feature.batch.line_header.support;

import fi.hsl.jore.importer.feature.batch.common.AbstractImportRepository;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.network.line_header.dto.Jore3LineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.generated.LineHeaderPK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeaders;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLineHeadersStaging;
import fi.hsl.jore.importer.jooq.network.tables.NetworkLines;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.jooq.impl.DSL.selectOne;

@Repository
public class LineHeaderImportRepository
        extends AbstractImportRepository<Jore3LineHeader, LineHeaderPK>
        implements ILineHeaderImportRepository {

    private static final NetworkLineHeadersStaging STAGING_TABLE = NetworkLineHeadersStaging.NETWORK_LINE_HEADERS_STAGING;
    private static final NetworkLineHeaders TARGET_TABLE = NetworkLineHeaders.NETWORK_LINE_HEADERS;
    private static final NetworkLines LINES_TABLE = NetworkLines.NETWORK_LINES;

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public LineHeaderImportRepository(@Qualifier("importerDsl") final DSLContext db,
                                      final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Override
    @Transactional
    public void clearStagingTable() {
        db.truncate(STAGING_TABLE)
          .execute();
    }

    @Override
    @Transactional
    public void submitToStaging(final Iterable<? extends Jore3LineHeader> headers) {
        final BatchBindStep batch = db.batch(db.insertInto(STAGING_TABLE,
                                                           STAGING_TABLE.NETWORK_LINE_HEADER_EXT_ID,
                                                           STAGING_TABLE.NETWORK_LINE_EXT_ID,
                                                           STAGING_TABLE.NETWORK_LINE_HEADER_NAME,
                                                           STAGING_TABLE.NETWORK_LINE_HEADER_NAME_SHORT,
                                                           STAGING_TABLE.NETWORK_LINE_HEADER_ORIGIN_1,
                                                           STAGING_TABLE.NETWORK_LINE_HEADER_ORIGIN_2,
                                                           STAGING_TABLE.NETWORK_LINE_HEADER_VALID_DATE_RANGE)
                                               .values((String) null, null, null, null, null, null, null));

        headers.forEach(header -> batch.bind(header.externalId().value(),
                                             header.lineId().value(),
                                             jsonbConverter.asJson(header.name()),
                                             jsonbConverter.asJson(header.nameShort()),
                                             jsonbConverter.asJson(header.origin1()),
                                             jsonbConverter.asJson(header.origin2()),
                                             header.validTime()));

        batch.execute();
    }

    protected Set<LineHeaderPK> delete() {
        return db.deleteFrom(TARGET_TABLE)
                 // Find rows which are missing from the latest dataset
                 .whereNotExists(selectOne()
                                         .from(STAGING_TABLE)
                                         .where(STAGING_TABLE.NETWORK_LINE_HEADER_EXT_ID.eq(TARGET_TABLE.NETWORK_LINE_HEADER_EXT_ID)))
                 .returningResult(TARGET_TABLE.NETWORK_LINE_HEADER_ID)
                 .fetch()
                 .stream()
                 .map(row -> LineHeaderPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<LineHeaderPK> update() {
        return db.update(TARGET_TABLE)
                 // What fields to update
                 .set(TARGET_TABLE.NETWORK_LINE_HEADER_NAME,
                      STAGING_TABLE.NETWORK_LINE_HEADER_NAME)
                 .set(TARGET_TABLE.NETWORK_LINE_HEADER_NAME_SHORT,
                      STAGING_TABLE.NETWORK_LINE_HEADER_NAME_SHORT)
                 .set(TARGET_TABLE.NETWORK_LINE_HEADER_ORIGIN_1,
                      STAGING_TABLE.NETWORK_LINE_HEADER_ORIGIN_1)
                 .set(TARGET_TABLE.NETWORK_LINE_HEADER_ORIGIN_2,
                      STAGING_TABLE.NETWORK_LINE_HEADER_ORIGIN_2)
                 // Note that start date cannot update (it's part of the external id), but the end date can
                 .set(TARGET_TABLE.NETWORK_LINE_HEADER_VALID_DATE_RANGE,
                      STAGING_TABLE.NETWORK_LINE_HEADER_VALID_DATE_RANGE)
                 .from(STAGING_TABLE)
                 // Find source rows..
                 .where(TARGET_TABLE.NETWORK_LINE_HEADER_EXT_ID
                                .eq(STAGING_TABLE.NETWORK_LINE_HEADER_EXT_ID))
                 // .. with updated fields
                 .and((TARGET_TABLE.NETWORK_LINE_HEADER_NAME.ne(STAGING_TABLE.NETWORK_LINE_HEADER_NAME))
                              .or(TARGET_TABLE.NETWORK_LINE_HEADER_NAME_SHORT.ne(STAGING_TABLE.NETWORK_LINE_HEADER_NAME_SHORT))
                              .or(TARGET_TABLE.NETWORK_LINE_HEADER_ORIGIN_1.ne(STAGING_TABLE.NETWORK_LINE_HEADER_ORIGIN_1))
                              .or(TARGET_TABLE.NETWORK_LINE_HEADER_ORIGIN_2.ne(STAGING_TABLE.NETWORK_LINE_HEADER_ORIGIN_2))
                              .or(TARGET_TABLE.NETWORK_LINE_HEADER_VALID_DATE_RANGE.ne(STAGING_TABLE.NETWORK_LINE_HEADER_VALID_DATE_RANGE)))
                 .returningResult(TARGET_TABLE.NETWORK_LINE_HEADER_ID)
                 .fetch()
                 .stream()
                 .map(row -> LineHeaderPK.of(row.value1()))
                 .collect(HashSet.collector());
    }

    protected Set<LineHeaderPK> insert() {
        return db.insertInto(TARGET_TABLE)
                 .columns(TARGET_TABLE.NETWORK_LINE_HEADER_EXT_ID,
                          TARGET_TABLE.NETWORK_LINE_ID,
                          TARGET_TABLE.NETWORK_LINE_HEADER_NAME,
                          TARGET_TABLE.NETWORK_LINE_HEADER_NAME_SHORT,
                          TARGET_TABLE.NETWORK_LINE_HEADER_ORIGIN_1,
                          TARGET_TABLE.NETWORK_LINE_HEADER_ORIGIN_2,
                          TARGET_TABLE.NETWORK_LINE_HEADER_VALID_DATE_RANGE)
                 .select(db.select(STAGING_TABLE.NETWORK_LINE_HEADER_EXT_ID,
                                   LINES_TABLE.NETWORK_LINE_ID,
                                   STAGING_TABLE.NETWORK_LINE_HEADER_NAME,
                                   STAGING_TABLE.NETWORK_LINE_HEADER_NAME_SHORT,
                                   STAGING_TABLE.NETWORK_LINE_HEADER_ORIGIN_1,
                                   STAGING_TABLE.NETWORK_LINE_HEADER_ORIGIN_2,
                                   STAGING_TABLE.NETWORK_LINE_HEADER_VALID_DATE_RANGE)
                           .from(STAGING_TABLE)
                           .leftJoin(LINES_TABLE).on(LINES_TABLE.NETWORK_LINE_EXT_ID.eq(STAGING_TABLE.NETWORK_LINE_EXT_ID))
                           .whereNotExists(selectOne()
                                                   .from(TARGET_TABLE)
                                                   .where(TARGET_TABLE.NETWORK_LINE_HEADER_EXT_ID.eq(STAGING_TABLE.NETWORK_LINE_HEADER_EXT_ID))))
                 .returningResult(TARGET_TABLE.NETWORK_LINE_HEADER_ID)
                 .fetch()
                 .stream()
                 .map(row -> LineHeaderPK.of(row.value1()))
                 .collect(HashSet.collector());
    }
}
