package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.key.JrScheduledStopPointPK;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import fi.hsl.jore.importer.feature.jore3.util.JoreGeometryUtil;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

/**
 * Contains the information of a scheduled stop point which is read
 * from the <code>jr_pysakki</code> table of the Jore3 database.
 */
@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrScheduledStopPoint.TABLE)
public interface JrScheduledStopPoint extends IHasPrimaryKey<JrScheduledStopPointPK> {

    String TABLE = "jr_pysakki";

    @JoreColumn(name = "elynumero",
            example = "1234567890"
    )
    Optional<String> elyNumber();

    @JoreColumn(name = "pysnimi",
            example = "Ritarihuone")
    Optional<String> nameFinnish();

    @JoreColumn(name = "pysnimir",
            nullable = true,
            example = "Riddarhuset")
    Optional<String> nameSwedish();

    static JrScheduledStopPoint of(final NodeId nodeId,
                                   final Optional<String> elyNumber,
                                   final Optional<String> nameFinnish,
                                   final Optional<String> nameSwedish) {
        return ImmutableJrScheduledStopPoint.builder()
                .pk(JrScheduledStopPointPK.of(nodeId))
                .elyNumber(elyNumber)
                .nameFinnish(nameFinnish)
                .nameSwedish(nameSwedish)
                .build();
    }
}
