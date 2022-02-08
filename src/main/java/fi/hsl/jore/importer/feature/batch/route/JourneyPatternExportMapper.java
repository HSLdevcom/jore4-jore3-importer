package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.network.route.dto.ExportableJourneyPattern;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Reads the information of an exported journey pattern
 * from the importer's database.
 */
public class JourneyPatternExportMapper implements RowMapper<ExportableJourneyPattern> {

    public static final String SQL_PATH = "classpath:export/export_journey_patterns.sql";

    @Override
    public ExportableJourneyPattern mapRow(final ResultSet resultSet,
                                           final int rowNumber) throws SQLException {
        return ExportableJourneyPattern.of(
                UUID.fromString(resultSet.getString("route_direction_id")),
                UUID.fromString(resultSet.getString("route_transmodel_id"))
        );
    }
}
