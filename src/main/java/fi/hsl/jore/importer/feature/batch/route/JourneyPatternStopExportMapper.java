package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPatternStop;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

/**
 * Maps a result set row into an {@link ImporterJourneyPatternStop} object.
 */
public class JourneyPatternStopExportMapper implements RowMapper<ImporterJourneyPatternStop> {

    public static final String SQL_PATH = "classpath:export/export_stops_of_journey_patterns.sql";

    private final IJsonbConverter jsonConverter;

    public JourneyPatternStopExportMapper(final IJsonbConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Override
    public ImporterJourneyPatternStop mapRow(final ResultSet resultSet,
                                             final int rowNumber) throws SQLException {
        return ImporterJourneyPatternStop.of(
                resultSet.getBoolean("is_hastus_point"),
                resultSet.getBoolean("is_via_point"),
                Optional.ofNullable(resultSet.getString("via_names")).map(viaNames -> jsonConverter.fromJson(viaNames, MultilingualString.class)),
                UUID.fromString(resultSet.getString("journey_pattern_transmodel_id")),
                resultSet.getInt("order_number"),
                resultSet.getString("short_id")
        );
    }
}
