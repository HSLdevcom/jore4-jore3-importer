package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRangeConverter;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterRoute;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalShort;

/**
 * Maps a result set row into an {@link ImporterRoute} object.
 */
public class RouteExportMapper implements RowMapper<ImporterRoute> {

    public static final String SQL_PATH = "classpath:export/export_routes.sql";
    private static final DateRangeConverter DATE_RANGE_CONVERTER = DateRangeConverter.INSTANCE;

    private final IJsonbConverter jsonConverter;

    public RouteExportMapper(final IJsonbConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Override
    public ImporterRoute mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return ImporterRoute.of(
                UUID.fromString(resultSet.getString("direction_id")),
                DirectionType.of(resultSet.getString("direction_type")),
                jsonConverter.fromJson(resultSet.getString("name"), MultilingualString.class),
                UUID.fromString(resultSet.getString("line_transmodel_id")),
                resultSet.getString("route_number"),
                getOptionalShort(resultSet, "hidden_variant"),
                DATE_RANGE_CONVERTER.from(resultSet.getString("valid_date_range")),
                LegacyHslMunicipalityCode.valueOf(resultSet.getString("legacy_hsl_municipality_code"))
        );
    }

}
