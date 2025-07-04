package fi.hsl.jore.importer.feature.batch.stop_place;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalString;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

import fi.hsl.jore.importer.feature.jore3.entity.JrStopPlace;
import fi.hsl.jore.importer.feature.jore3.field.generated.StopPlaceId;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class StopPlaceImportMapper implements RowMapper<JrStopPlace> {

    public static final String SQL_PATH = "classpath:jore3-import/import_stop_places.sql";

    @Override
    public JrStopPlace mapRow(final ResultSet rs, final int rowNumber) throws SQLException {
        return JrStopPlace.of(
                StopPlaceId.of(getStringOrThrow(rs, "pysalueid")),
                getOptionalString(rs, "nimi"),
                getOptionalString(rs, "nimir"),
                getOptionalString(rs, "nimipitka"),
                getOptionalString(rs, "nimipitkar"),
                getOptionalString(rs, "paikannimi"),
                getOptionalString(rs, "paikannimir"));
    }
}
