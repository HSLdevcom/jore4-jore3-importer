package fi.hsl.jore.importer.feature.transmodel.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.List;

import static fi.hsl.jore.importer.TestConstants.LOCAL_TIME_ZONE;

/**
 * Provides methods which are used to query validity period start
 * and end timestamps from the Jore 4 database. Note that this
 * repository class should only be used by integration
 * tests.
 */
public class TransmodelValidityPeriodTestRepository {

    private static final String SQL_QUERY_TEMPLATE_GET_VALIDITY_PERIOD_END_TIME = "SELECT validity_end FROM %s";
    private static final String SQL_QUERY_TEMPLATE_GET_VALIDITY_PERIOD_START_TIME = "SELECT validity_start FROM %s";

    private final JdbcTemplate jdbcTemplate;
    private final String sqlQueryGetValidityPeriodEndTime;
    private final String sqlQueryGetValidityPeriodStartTime;

    /**
     * Creates a new repository
     * @param dataSource    The data source of the Jore 4 database.
     * @param targetTable   The table which contains the timestamp columns.
     */
    public TransmodelValidityPeriodTestRepository(final DataSource dataSource,
                                                  final ValidityPeriodTargetTable targetTable) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.sqlQueryGetValidityPeriodEndTime = String.format(SQL_QUERY_TEMPLATE_GET_VALIDITY_PERIOD_END_TIME,
                targetTable.getTableName()
        );
        this.sqlQueryGetValidityPeriodStartTime = String.format(SQL_QUERY_TEMPLATE_GET_VALIDITY_PERIOD_START_TIME,
                targetTable.getTableName()
        );
    }

    /**
     * Finds the validity period start timestamp in Finnish time zone (Europe/Helsinki).
     * Note that this method assumes the target table has exactly one row.
     *
     * @return  The found validity period start time.
     */
    public OffsetDateTime findValidityPeriodStartTimestampAtFinnishTimeZone() {
        final List<OffsetDateTime> validityPeriodStartDates = jdbcTemplate.queryForList(
                this.sqlQueryGetValidityPeriodStartTime,
                OffsetDateTime.class
        );

        return validityPeriodStartDates.get(0)
                .atZoneSameInstant(LOCAL_TIME_ZONE)
                .toOffsetDateTime();
    }

    /**
     * Finds the validity period start timestamp in Finnish time zone (Europe/Helsinki).
     * Note that this method assumes the target table has exactly one row.
     *
     * @return The found validity period end time.
     */
    public OffsetDateTime findValidityPeriodEndTimestampAtFinnishTimeZone() {
        final List<OffsetDateTime> validityPeriodEndDates = jdbcTemplate.queryForList(
                this.sqlQueryGetValidityPeriodEndTime,
                OffsetDateTime.class
        );

        return validityPeriodEndDates.get(0)
                .atZoneSameInstant(LOCAL_TIME_ZONE)
                .toOffsetDateTime();
    }
}
