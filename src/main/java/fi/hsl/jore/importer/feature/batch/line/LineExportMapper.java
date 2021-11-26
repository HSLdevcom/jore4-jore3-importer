package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.batch.common.JdbcRowMapperUtil;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.network.line.dto.ExportableLine;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a result set row into an {@link ExportableLine} object.
 */
public class LineExportMapper implements RowMapper<ExportableLine> {

    public static final String SQL_PATH = "classpath:export/export_lines.sql";

    private final JdbcRowMapperUtil rowMapperUtil;

    public LineExportMapper(final JdbcRowMapperUtil rowMapperUtil) {
        this.rowMapperUtil = rowMapperUtil;
    }

    @Override
    public ExportableLine mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return ExportableLine.of(
                rowMapperUtil.multilingualStringFromJsonString(resultSet.getString("name")),
                NetworkType.of(resultSet.getString("network_type")),
                rowMapperUtil.multilingualStringFromJsonString(resultSet.getString("short_name"))
        );
    }
}
