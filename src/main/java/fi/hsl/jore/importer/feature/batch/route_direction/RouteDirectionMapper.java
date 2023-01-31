package fi.hsl.jore.importer.feature.batch.route_direction;

import fi.hsl.jore.importer.feature.jore3.entity.JrRouteDirection;
import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getIntOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getLocalDateTimeOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalInt;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalString;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

public class RouteDirectionMapper implements RowMapper<JrRouteDirection> {

    public static final String SQL_PATH = "classpath:jore3-import/import_route_directions.sql";

    @Override
    @Nullable
    public JrRouteDirection mapRow(final ResultSet rs,
                                   final int rowNum) throws SQLException {
        return JrRouteDirection.of(
                RouteId.from(getStringOrThrow(rs, "reitunnus")),
                Direction.of(getIntOrThrow(rs, "suusuunta"))
                         .orElse(Direction.UNKNOWN),
                getOptionalInt(rs, "suupituus"),
                getLocalDateTimeOrThrow(rs, "suuvoimast").toLocalDate(),
                getLocalDateTimeOrThrow(rs, "suuvoimviimpvm").toLocalDate(),
                getStringOrThrow(rs, "suunimi"),
                getOptionalString(rs, "suunimilyh"),
                getOptionalString(rs, "suunimir"),
                getOptionalString(rs, "suunimilyhr"),
                getOptionalString(rs, "suulahpaik"),
                getOptionalString(rs, "suulahpaikr"),
                getOptionalString(rs, "suupaapaik"),
                getOptionalString(rs, "suupaapaikr")
        );
    }
}
