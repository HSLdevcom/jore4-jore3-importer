package fi.hsl.jore.importer.feature.jore4.repository;

/** Identifies the table which contains the queried validity period timestamp columns. */
public enum ValidityPeriodTargetTable {
    LINE("network.line"),
    ROUTE("network.route"),
    SCHEDULED_STOP_POINT("network.scheduled_stop_point");

    private final String tableName;

    ValidityPeriodTargetTable(final String tableName) {
        this.tableName = tableName;
    }

    /** Returns the name of the database table; */
    public String getTableName() {
        return tableName;
    }
}
