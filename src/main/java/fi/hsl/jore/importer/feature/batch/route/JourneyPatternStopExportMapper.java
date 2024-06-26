package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.enumerated.RegulatedTimingPointStatus;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPatternStop;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

/** Maps a result set row into an {@link ImporterJourneyPatternStop} object. */
public class JourneyPatternStopExportMapper implements RowMapper<ImporterJourneyPatternStop> {

    public static final String SQL_PATH = "classpath:jore4-export/export_stops_of_journey_patterns.sql";

    private final IJsonbConverter jsonConverter;

    public JourneyPatternStopExportMapper(final IJsonbConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Override
    public ImporterJourneyPatternStop mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return ImporterJourneyPatternStop.of(
                UUID.fromString(resultSet.getString("journey_pattern_jore4_id")),
                resultSet.getString("route_direction_jore3_id"),
                resultSet.getInt("order_number"),
                resultSet.getString("short_id"),
                resultSet.getBoolean("is_used_as_timing_point"),
                Optional.ofNullable(resultSet.getString("timing_place_label")),
                // should never throw exception because of a database check constraint
                RegulatedTimingPointStatus.of(resultSet.getInt("regulated_timing_point_status"))
                        .orElseThrow(),
                resultSet.getBoolean("is_via_point"),
                Optional.ofNullable(resultSet.getString("via_names"))
                        .map(viaNames -> jsonConverter.fromJson(viaNames, MultilingualString.class)));
    }
}
