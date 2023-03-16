package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place;

import fi.hsl.jore.importer.feature.network.scheduled_stop_point.timing_place.ImporterTimingPlace;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a result set row into an {@link ImporterTimingPlace} object.
 */
public class TimingPlaceExportMapper implements RowMapper<ImporterTimingPlace> {

    public static final String SQL_PATH = "classpath:jore4-export/export_timing_places.sql";

    @Override
    public ImporterTimingPlace mapRow(final ResultSet resultSet,
                                      final int rowNumber) throws SQLException {

        return ImporterTimingPlace.of((resultSet.getString("hastus_place_id")));
    }
}
