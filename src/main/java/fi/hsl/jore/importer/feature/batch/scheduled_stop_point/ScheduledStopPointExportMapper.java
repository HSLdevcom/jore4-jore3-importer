package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.hsl.jore.importer.config.jooq.converter.geometry.GeometryConverter;
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

    private final ObjectMapper objectMapper;

    public ScheduledStopPointExportMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ExportableScheduledStopPoint mapRow(ResultSet resultSet,
                                               int rowNumber) throws SQLException {
        return ExportableScheduledStopPoint.of(
                ExternalId.of(resultSet.getString("external_id")),
                getOptionalString(resultSet, "ely_number"),
                pointFromDatabaseObject(resultSet.getObject("location")),
                nameFromJsonString(resultSet.getString("name"))
        );
    }

    private Point pointFromDatabaseObject(@Nullable Object databaseObject) throws SQLException {
        if (databaseObject == null) {
            throw new SQLException("Cannot parse location because the result set contained null value");
        }

        return (Point) Objects.requireNonNull(POINT_CONVERTER.from(databaseObject));
    }

    private MultilingualString nameFromJsonString(String nameAsJson) throws SQLException {
        try {
            HashMap<String, String> names = objectMapper.readValue(nameAsJson, HashMap.class);
            return MultilingualString.of(names);
        }
        catch (JsonProcessingException e) {
            throw new SQLException("Could not parse multilingual name from result set because of an error", e);
        }
    }
}
