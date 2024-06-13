package fi.hsl.jore.importer.feature.batch.util;

import fi.hsl.jore.importer.feature.jore3.enumerated.PublicTransportType;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import java.util.Set;

public final class LineClassificationUtil {

    public static final Set<String> REGIONAL_TRAFFIC_TRAIN_LINE_NUMBERS =
            Set.of("Y", "X", "U", "L", "E", "A", "P", "I", "K");

    public static final Set<String> LIGHT_RAIL_TRAM_LINE_NUMBERS =
            Set.of("550"); // RaideJokeri / Pikaraitiotie 550 (under construction)

    private LineClassificationUtil() {}

    public static TypeOfLine resolveTypeOfLine(
            final TransitType transitType,
            final boolean isTrunkLine,
            final PublicTransportType publicTransportType,
            final String lineNumber) {

        switch (transitType) {
            case BUS:
                if (isTrunkLine) {
                    return TypeOfLine.EXPRESS_BUS_SERVICE;
                } else {
                    switch (publicTransportType) {
                        case BUS_LOCAL:
                            return TypeOfLine.SPECIAL_NEEDS_BUS;
                        case U_TRANSPORT:
                            return TypeOfLine.REGIONAL_BUS_SERVICE;
                    }
                }
                return TypeOfLine.STOPPING_BUS_SERVICE;

            case TRAIN:
                return REGIONAL_TRAFFIC_TRAIN_LINE_NUMBERS.contains(lineNumber.trim())
                        ? TypeOfLine.SUBURBAN_RAILWAY
                        : TypeOfLine.REGIONAL_RAIL_SERVICE;

            case FERRY:
                return TypeOfLine.FERRY_SERVICE;

            case SUBWAY:
                return TypeOfLine.METRO_SERVICE;

            case TRAM:
                return LIGHT_RAIL_TRAM_LINE_NUMBERS.contains(lineNumber.trim())
                        ? TypeOfLine.REGIONAL_TRAM_SERVICE
                        : TypeOfLine.CITY_TRAM_SERVICE;

            default:
                return TypeOfLine.STOPPING_BUS_SERVICE;
                // TODO: Find a way for determining the line type for this case.
                // throw new IllegalArgumentException("Cannot determine type of line: " + lineNumber);
        }
    }
}
