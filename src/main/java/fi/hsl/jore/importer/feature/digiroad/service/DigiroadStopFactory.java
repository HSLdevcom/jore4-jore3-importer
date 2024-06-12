package fi.hsl.jore.importer.feature.digiroad.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStopDirection;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import fi.hsl.jore.importer.feature.jore3.util.StringParserUtil;
import java.util.Optional;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.locationtech.jts.geom.Point;

/** An object mother class which creates new {@link DigiroadStop} objects. */
class DigiroadStopFactory {

    private static final String CSV_SEPARATOR_CHARACTER = ";";
    private static final String DIRECTION_AGAINST_DIGITIZING_DIRECTION = "backward";
    private static final String DIRECTION_IN_DIGITIZING_DIRECTION = "forward";
    private static final int VALID_LINE_COLUMN_COUNT = 8;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Parses the information of a {@link DigiroadStop} object from line which is read from a CSV file. The file must
     * use a semicolon (;) as a separator character. The line must contain the following columns in this order:
     *
     * <ul>
     *   <li>The external id of the stop.
     *   <li>The external id of the infrastructure link
     *   <li>The national stop id (aka ely number)
     *   <li>The stop direction on the infrastructure link
     *   <li>The location of the stop
     *   <li>The Finnish name of the stop
     *   <li>The Swedish name of the stop
     *   <li>The source system
     * </ul>
     *
     * @param line
     * @return
     */
    static Optional<DigiroadStop> fromCsvLine(final String line) {
        if (line == null) {
            throw new NullPointerException("Line cannot be null");
        }

        final String[] columns = line.split(CSV_SEPARATOR_CHARACTER);
        if (columns.length != VALID_LINE_COLUMN_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "Expected the line to have %d columns but had %d", VALID_LINE_COLUMN_COUNT, columns.length));
        }

        final DigiroadStop stop = DigiroadStop.of(
                StringParserUtil.parseRequiredValue("externalStopId", columns[0].trim()),
                StringParserUtil.parseRequiredValue("externalLinkId", columns[1].trim()),
                parseDirection(columns[3]),
                StringParserUtil.parseRequiredLong("elyNumber", columns[2]),
                parseLocation(columns[4]),
                getStringContainer(columns[5]),
                getStringContainer(columns[6]));
        return Optional.of(stop);
    }

    private static Optional<String> getStringContainer(final String value) {
        return Optional.of(value).map(String::trim).filter(s -> !s.isEmpty());
    }

    private static DigiroadStopDirection parseDirection(final String value) {
        final String trimmedValue = value.trim();
        switch (trimmedValue) {
            case DIRECTION_IN_DIGITIZING_DIRECTION:
                return DigiroadStopDirection.FORWARD;
            case DIRECTION_AGAINST_DIGITIZING_DIRECTION:
                return DigiroadStopDirection.BACKWARD;
            default:
                throw new IllegalArgumentException(String.format("Unknown direction: %s", value));
        }
    }

    private static Point parseLocation(final String value) {
        try {
            final org.geojson.Point point = (org.geojson.Point)
                    OBJECT_MAPPER.readValue(removeExtraQuotationMarks(value.trim()), GeoJsonObject.class);
            final LngLatAlt coordinates = point.getCoordinates();
            return JoreGeometryUtil.fromDbCoordinates(coordinates.getLatitude(), coordinates.getLongitude());
        } catch (final JsonProcessingException ex) {
            throw new RuntimeException(String.format("Couldn't parse location from value: %s", value), ex);
        }
    }

    /**
     * Transforms the value: "{""type"": ""Point"", ""coordinates"": [24.696376131, 60.207149801]}" to value: {"type":
     * "Point", "coordinates": [24.696376131, 60.207149801]}
     */
    private static String removeExtraQuotationMarks(final String value) {
        final String valueWithoutExtraQuotationMarks = value.substring(1, value.length() - 1);
        return valueWithoutExtraQuotationMarks.replaceAll("\"\"", "\"");
    }
}
