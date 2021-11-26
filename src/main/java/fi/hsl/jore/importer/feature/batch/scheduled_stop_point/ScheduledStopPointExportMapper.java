package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.config.jooq.converter.geometry.GeometryConverter;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ExportableScheduledStopPoint;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalString;

/**
 * Maps a result set row into an {@link ExportableScheduledStopPoint} object.
 */
public class ScheduledStopPointExportMapper implements RowMapper<ExportableScheduledStopPoint> {

    public static final String SQL_PATH = "classpath:export/export_scheduled_stop_points.sql";
    private static final GeometryConverter POINT_CONVERTER = new GeometryConverter(Geometry.TYPENAME_POINT);

    private final IJsonbConverter jsonConverter;

    public ScheduledStopPointExportMapper(final IJsonbConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Override
    public ExportableScheduledStopPoint mapRow(final ResultSet resultSet,
                                               final int rowNumber) throws SQLException {
        return ExportableScheduledStopPoint.of(
                ExternalId.of(resultSet.getString("external_id")),
                getOptionalString(resultSet, "ely_number"),
                pointFromDatabaseObject(resultSet.getObject("location")),
                jsonConverter.fromJson(resultSet.getString("name"), MultilingualString.class),
                getOptionalString(resultSet, "short_id")
        );
    }

    private Point pointFromDatabaseObject(@Nullable final Object databaseObject) throws SQLException {
        if (databaseObject == null) {
            throw new SQLException("Cannot parse location because the result set contained null value");
        }

        return (Point) Objects.requireNonNull(POINT_CONVERTER.from(databaseObject));
    }
}
