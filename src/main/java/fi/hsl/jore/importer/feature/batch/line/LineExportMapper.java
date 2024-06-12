package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRangeConverter;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import fi.hsl.jore.importer.feature.network.line.dto.ImporterLine;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/** Maps a result set row into an {@link ImporterLine} object. */
public class LineExportMapper implements RowMapper<ImporterLine> {

    public static final String SQL_PATH = "classpath:jore4-export/export_lines.sql";
    private static final DateRangeConverter DATE_RANGE_CONVERTER = DateRangeConverter.INSTANCE;

    private final IJsonbConverter jsonConverter;

    public LineExportMapper(final IJsonbConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Override
    public ImporterLine mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return ImporterLine.of(
                ExternalId.of(resultSet.getString("line_header_external_id")),
                resultSet.getString("line_number"),
                jsonConverter.fromJson(
                        resultSet.getString("line_header_name"), MultilingualString.class),
                NetworkType.of(resultSet.getString("network_type")),
                jsonConverter.fromJson(
                        resultSet.getString("line_header_short_name"), MultilingualString.class),
                DATE_RANGE_CONVERTER.from(resultSet.getString("line_header_cluster_date_range")),
                TypeOfLine.of(resultSet.getString("type_of_line")),
                LegacyHslMunicipalityCode.valueOf(
                        resultSet.getString("legacy_hsl_municipality_code")));
    }
}
