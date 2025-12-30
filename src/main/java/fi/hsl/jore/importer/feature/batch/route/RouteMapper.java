package fi.hsl.jore.importer.feature.batch.route;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalString;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

import fi.hsl.jore.importer.feature.jore3.entity.JrRoute;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import jakarta.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RouteMapper implements RowMapper<JrRoute> {

    public static final String SQL_PATH = "classpath:jore3-import/import_routes.sql";

    @Override
    @Nullable
    public JrRoute mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return JrRoute.of(
                RouteId.from(getStringOrThrow(rs, "reitunnus")),
                LineId.from(getStringOrThrow(rs, "lintunnus")),
                getOptionalString(rs, "reinimi"),
                getOptionalString(rs, "reinimir")
                // reinimilyh and reinimilyhr are missing from JrRoute class
                // because they are deprecated.
                );
    }
}
