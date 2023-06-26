package fi.hsl.jore.importer.feature.batch.place;

import fi.hsl.jore.importer.feature.jore3.entity.JrPlace;
import fi.hsl.jore.importer.feature.jore3.field.generated.PlaceId;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

public class PlaceImportRowMapper implements RowMapper<JrPlace> {

    public static final String SQL_PATH = "classpath:jore3-import/import_places.sql";

    @Override
    @Nullable
    public JrPlace mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return JrPlace.of(
                PlaceId.of(getStringOrThrow(rs, "paitunnus")),
                getStringOrThrow(rs, "nimi")
        );
    }
}
