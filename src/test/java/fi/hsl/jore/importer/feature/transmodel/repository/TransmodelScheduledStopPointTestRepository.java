package fi.hsl.jore.importer.feature.transmodel.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.List;

import static fi.hsl.jore.importer.TestConstants.LOCAL_TIME_ZONE;

/**
 * Provides methods which are used to query column
 * values from the Jore 4 database. Note that this
 * repository class should only be used by integration
 * tests. If you are writing production code, you must
 * use the {@link TransmodelScheduledStopPointRepository} class.
 */
public class TransmodelScheduledStopPointTestRepository {

    private static final String SQL_QUERY_GET_VALIDITY_PERIOD_END_TIME = "SELECT s.validity_end " +
            "FROM internal_service_pattern.scheduled_stop_point s";
    private static final String SQL_QUERY_GET_VALIDITY_PERIOD_START_TIME = "SELECT s.validity_start " +
            "FROM internal_service_pattern.scheduled_stop_point s";

    private final JdbcTemplate jdbcTemplate;

    public TransmodelScheduledStopPointTestRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Finds the validity period start timestamp in Finnish time zone (Europe/Helsinki).
     * Note that this method assumes the the <code>internal_service_pattern.scheduled_stop_point</code>
     * table has exactly one row.
     *
     * @return  The found validity period start time.
     */
    public OffsetDateTime findValidityPeriodStartTimestampAtFinnishTimeZone() {
        final List<OffsetDateTime> validityPeriodStartDates = jdbcTemplate.queryForList(
                SQL_QUERY_GET_VALIDITY_PERIOD_START_TIME,
                OffsetDateTime.class
        );

        return validityPeriodStartDates.get(0)
                .atZoneSameInstant(LOCAL_TIME_ZONE)
                .toOffsetDateTime();
    }

    /**
     * Finds the validity period end timestamp in Finnish time zone (Europe/Helsinki).
     * Note that this method assumes the the <code>internal_service_pattern.scheduled_stop_point</code>
     * table has exactly one row.
     *
     * @return The found validity period end time.
     */
    public OffsetDateTime findValidityPeriodEndTimestampAtFinnishTimeZone() {
        final List<OffsetDateTime> validityPeriodEndDates = jdbcTemplate.queryForList(
                SQL_QUERY_GET_VALIDITY_PERIOD_END_TIME,
                OffsetDateTime.class
        );

        return validityPeriodEndDates.get(0)
                .atZoneSameInstant(LOCAL_TIME_ZONE)
                .toOffsetDateTime();
    }
}
