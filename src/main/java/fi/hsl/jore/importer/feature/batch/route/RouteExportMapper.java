package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRangeConverter;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.network.route.dto.ExportableRoute;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Maps a result set row into an {@link ExportableRoute} object.
 */
public class RouteExportMapper implements RowMapper<ExportableRoute> {

    public static final String SQL_PATH = "classpath:export/export_routes.sql";
    private static final DateRangeConverter DATE_RANGE_CONVERTER = DateRangeConverter.INSTANCE;

    private final IJsonbConverter jsonConverter;

    public RouteExportMapper(final IJsonbConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Override
    public ExportableRoute mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return ExportableRoute.of(
                DirectionType.of(resultSet.getString("direction_type")),
                ExternalId.of(resultSet.getString("external_id")),
                jsonConverter.fromJson(resultSet.getString("name"), MultilingualString.class),
                UUID.fromString(resultSet.getString("line_transmodel_id")),
                resultSet.getString("route_number"),
                UUID.fromString(resultSet.getString("start_scheduled_stop_point_transmodel_id")),
                UUID.fromString(resultSet.getString("end_scheduled_stop_point_transmodel_id")),
                DATE_RANGE_CONVERTER.from(resultSet.getString("valid_date_range"))
        );
    }
}
