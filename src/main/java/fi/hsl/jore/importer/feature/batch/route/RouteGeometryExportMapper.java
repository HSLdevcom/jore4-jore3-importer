package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.config.jooq.converter.geometry.LineStringConverter;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import org.locationtech.jts.geom.LineString;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Maps a result set row into an {@link ImporterRouteGeometry} object.
 */
public class RouteGeometryExportMapper implements RowMapper<ImporterRouteGeometry> {

    public static final String SQL_PATH = "classpath:jore4-export/export_route_geometries.sql";

    @Override
    public ImporterRouteGeometry mapRow(final ResultSet resultSet,
                                        final int rowNumber) throws SQLException {
        final String routeGeometryHex = resultSet.getString("network_route_direction_shape")
                //We need to remove '(' and ')' characters found from the beginning
                //and the end of the hex string.
                .replaceAll("\\(", "")
                .replaceAll("\\)", "");
        final LineString routeGeometry = LineStringConverter.INSTANCE.from(routeGeometryHex);
        return ImporterRouteGeometry.from(
                routeGeometry,
                UUID.fromString(resultSet.getString("route_direction_id")),
                resultSet.getString("route_direction_ext_id"),
                UUID.fromString(resultSet.getString("route_transmodel_id"))
        );
    }
}
