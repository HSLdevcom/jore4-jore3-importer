package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.jore3.entity.JrScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getDoubleOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalString;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

/**
 * Maps rows read from the source database to {@link JrScheduledStopPoint} objects.
 */
public class ScheduledStopPointImportMapper implements RowMapper<JrScheduledStopPoint> {

    public static final String SQL_PATH = "classpath:import/import_scheduled_stop_points.sql";

    @Override
    public JrScheduledStopPoint mapRow(final ResultSet rs,
                                       final int rowNumber) throws SQLException {
        return JrScheduledStopPoint.of(
                NodeId.of(getStringOrThrow(rs, "soltunnus")),
                getOptionalString(rs, "elynumero"),
                getOptionalString(rs, "pysnimi"),
                getOptionalString(rs, "pysnimir"),
                getOptionalString(rs, "sollistunnus"),
                getOptionalString(rs, "solkirjain")
        );
    }
}
