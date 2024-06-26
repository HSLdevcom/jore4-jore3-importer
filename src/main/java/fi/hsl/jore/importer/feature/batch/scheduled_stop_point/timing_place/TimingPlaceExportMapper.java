package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place;

import fi.hsl.jore.importer.feature.network.scheduled_stop_point.timing_place.ImporterTimingPlace;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/** Maps a result set row into an {@link ImporterTimingPlace} object. */
public class TimingPlaceExportMapper implements RowMapper<ImporterTimingPlace> {

    public static final String SQL_PATH = "classpath:jore4-export/export_timing_places.sql";

    @Override
    public ImporterTimingPlace mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {

        return ImporterTimingPlace.of(
                resultSet.getString("timing_place_label"), resultSet.getString("timing_place_name"));
    }
}
