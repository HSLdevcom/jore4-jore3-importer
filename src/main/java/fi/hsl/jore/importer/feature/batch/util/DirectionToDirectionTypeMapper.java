package fi.hsl.jore.importer.feature.batch.util;

import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import java.util.Map;

public final class DirectionToDirectionTypeMapper {

    public static final Map<Direction, DirectionType> TO_DIRECTION_TYPE = Map.of(
            Direction.DIRECTION_1, DirectionType.OUTBOUND,
            Direction.DIRECTION_2, DirectionType.INBOUND,
            Direction.UNKNOWN, DirectionType.UNKNOWN);

    private DirectionToDirectionTypeMapper() {}

    public static DirectionType resolveDirectionType(final Direction direction) {
        return TO_DIRECTION_TYPE.getOrDefault(direction, DirectionType.UNKNOWN);
    }
}
