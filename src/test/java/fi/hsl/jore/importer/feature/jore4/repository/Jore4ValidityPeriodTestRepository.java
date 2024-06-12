package fi.hsl.jore.importer.feature.jore4.repository;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Provides methods which are used to query validity period start and end timestamps from the Jore 4
 * database. Note that this repository class should only be used by integration tests.
 */
public class Jore4ValidityPeriodTestRepository {

    private static final String SQL_QUERY_TEMPLATE_GET_VALIDITY_PERIOD_END_TIME =
            "SELECT validity_end FROM %s";
    private static final String SQL_QUERY_TEMPLATE_GET_VALIDITY_PERIOD_START_TIME =
            "SELECT validity_start FROM %s";

    private final JdbcTemplate jdbcTemplate;
    private final String sqlQueryGetValidityPeriodEnd;
    private final String sqlQueryGetValidityPeriodStart;

    /**
     * Creates a new repository
     *
     * @param dataSource The data source of the Jore 4 database.
     * @param targetTable The table which contains the timestamp columns.
     */
    public Jore4ValidityPeriodTestRepository(
            final DataSource dataSource, final ValidityPeriodTargetTable targetTable) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.sqlQueryGetValidityPeriodEnd =
                String.format(
                        SQL_QUERY_TEMPLATE_GET_VALIDITY_PERIOD_END_TIME,
                        targetTable.getTableName());
        this.sqlQueryGetValidityPeriodStart =
                String.format(
                        SQL_QUERY_TEMPLATE_GET_VALIDITY_PERIOD_START_TIME,
                        targetTable.getTableName());
    }

    /**
     * Finds the validity period start date. Note that this method assumes the target table has
     * exactly one row.
     *
     * @return The found validity period start date.
     */
    public LocalDate findValidityPeriodStartDate() {
        final List<LocalDate> validityPeriodStartDates =
                jdbcTemplate.queryForList(this.sqlQueryGetValidityPeriodStart, LocalDate.class);

        return validityPeriodStartDates.get(0);
    }

    /**
     * Finds the validity period start date. Note that this method assumes the target table has
     * exactly one row.
     *
     * @return The found validity period end date.
     */
    public LocalDate findValidityPeriodEndDate() {
        final List<LocalDate> validityPeriodEndDates =
                jdbcTemplate.queryForList(this.sqlQueryGetValidityPeriodEnd, LocalDate.class);

        return validityPeriodEndDates.get(0);
    }
}
