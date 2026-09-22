package fi.hsl.jore.importer.feature.jore4.repository;

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPatternStop;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class Jore4JourneyPatternStopRepository implements IJore4JourneyPatternStopRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jore4JourneyPatternStopRepository.class);
    private static final Pattern BATCH_ENTRY_INDEX_PATTERN = Pattern.compile("Batch entry (\\d+)");

    private final DSLContext db;
    private final IJsonbConverter jsonbConverter;

    @Autowired
    public Jore4JourneyPatternStopRepository(
            @Qualifier("jore4Dsl") final DSLContext db, final IJsonbConverter jsonbConverter) {
        this.db = db;
        this.jsonbConverter = jsonbConverter;
    }

    @Transactional
    @Override
    public void insert(final Iterable<? extends Jore4JourneyPatternStop> journeyPatternStops) {
        final List<Jore4JourneyPatternStop> stopsInBatch = new ArrayList<>();
        BatchBindStep batch = db.batch(db.insertInto(
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN,
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID,
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL,
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE,
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_USED_AS_TIMING_POINT,
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_REGULATED_TIMING_POINT,
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_LOADING_TIME_ALLOWED,
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_VIA_POINT,
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.VIA_POINT_NAME_I18N,
                        SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.VIA_POINT_SHORT_NAME_I18N)
                .values((UUID) null, null, null, null, null, null, null, null, null));

        for (final Jore4JourneyPatternStop journeyPatternStop : journeyPatternStops) {
            stopsInBatch.add(journeyPatternStop);
            batch = batch.bind(
                    journeyPatternStop.journeyPatternId(),
                    journeyPatternStop.scheduledStopPointLabel(),
                    journeyPatternStop.scheduledStopPointSequence(),
                    journeyPatternStop.isUsedAsTimingPoint(),
                    journeyPatternStop.isRegulatedTimingPoint(),
                    journeyPatternStop.isLoadingTimeAllowed(),
                    journeyPatternStop.isViaPoint(),
                    // Use the existing via name for both normal and short names
                    journeyPatternStop
                            .viaPointNames()
                            .map(jsonbConverter::asJson)
                            .orElse(null),
                    journeyPatternStop
                            .viaPointNames()
                            .map(jsonbConverter::asJson)
                            .orElse(null));
        }

        if (batch.size() > 0) {
            try {
                batch.execute();
            } catch (final RuntimeException exception) {
                logFailedPayload(stopsInBatch, exception);
                throw exception;
            }
        }
    }

    private static void logFailedPayload(
            final List<Jore4JourneyPatternStop> stopsInBatch, final RuntimeException exception) {
        final Optional<String> batchUpdateExceptionMessage =
                findBatchUpdateException(exception).map(BatchUpdateException::getMessage);
        final OptionalInt batchEntryIndex = batchUpdateExceptionMessage
                .map(Jore4JourneyPatternStopRepository::extractBatchEntryIndex)
                .orElseGet(OptionalInt::empty);

        if (batchEntryIndex.isPresent()
                && batchEntryIndex.getAsInt() >= 0
                && batchEntryIndex.getAsInt() < stopsInBatch.size()) {
            LOGGER.error(
                    "Failed to insert journey pattern stop at JDBC batch index {}. Payload: {}",
                    batchEntryIndex.getAsInt(),
                    stopsInBatch.get(batchEntryIndex.getAsInt()),
                    exception);
        } else {
            LOGGER.error(
                    "Failed to insert a batch of {} journey pattern stops; the JDBC driver did not report a valid zero-based item index. Batch message: {}. Payloads: {}",
                    stopsInBatch.size(),
                    batchUpdateExceptionMessage.orElse("No BatchUpdateException was found in the cause chain"),
                    stopsInBatch,
                    exception);
        }
    }

    private static Optional<BatchUpdateException> findBatchUpdateException(final Throwable exception) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof BatchUpdateException batchUpdateException) {
                return Optional.of(batchUpdateException);
            }
            current = current.getCause();
        }
        return Optional.empty();
    }

    private static OptionalInt extractBatchEntryIndex(final String message) {
        final Matcher matcher = BATCH_ENTRY_INDEX_PATTERN.matcher(message);
        return matcher.find() ? OptionalInt.of(Integer.parseInt(matcher.group(1))) : OptionalInt.empty();
    }
}
