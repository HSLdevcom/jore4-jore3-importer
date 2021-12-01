package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRangeConverter;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
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
    private static final DateRangeConverter DATE_RANGE_CONVERTER = DateRangeConverter.INSTANCE;

    private final IJsonbConverter jsonConverter;

    public LineExportMapper(final IJsonbConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Override
    public ExportableLine mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return ExportableLine.of(
                ExternalId.of(resultSet.getString("external_id")),
                jsonConverter.fromJson(resultSet.getString("name"), MultilingualString.class),
                NetworkType.of(resultSet.getString("network_type")),
                jsonConverter.fromJson(resultSet.getString("short_name"), MultilingualString.class),
                DATE_RANGE_CONVERTER.from(resultSet.getString("valid_date_range"))
        );
    }
}
