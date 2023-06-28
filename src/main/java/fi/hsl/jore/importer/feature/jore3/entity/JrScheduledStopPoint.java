package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.key.JrScheduledStopPointPK;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import org.immutables.value.Value;

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
    Optional<Long> elyNumber();

    @JoreColumn(name = "pysnimi",
            example = "Ritarihuone")
    Optional<String> nameFinnish();

    @JoreColumn(name = "pysnimir",
            nullable = true,
            example = "Riddarhuset")
    Optional<String> nameSwedish();

    @JoreColumn(name = "sollistunnus",
            example = "1234"
    )
    Optional<String> shortId();

    @JoreColumn(name = "solkirjain",
            example = "H"
    )
    Optional<String> shortLetter();

    @JoreColumn(name = "paitunnus",
            example = "1ELIEL"
    )
    Optional<String> placeExternalId();

    /**
     * Number of times used in routes
     */
    @JoreColumn(name = "usage_in_routes",
            example = "2")
    int usageInRoutes();

    static JrScheduledStopPoint of(final NodeId nodeId,
                                   final Optional<Long> elyNumber,
                                   final Optional<String> nameFinnish,
                                   final Optional<String> nameSwedish,
                                   final Optional<String> shortId,
                                   final Optional<String> shortLetter,
                                   final Optional<String> placeExternalId,
                                   final int usageInRoutes) {
        return ImmutableJrScheduledStopPoint.builder()
                .pk(JrScheduledStopPointPK.of(nodeId))
                .elyNumber(elyNumber)
                .nameFinnish(nameFinnish)
                .nameSwedish(nameSwedish)
                .shortId(shortId)
                .shortLetter(shortLetter)
                .placeExternalId(placeExternalId)
                .usageInRoutes(usageInRoutes)
                .build();
    }
}
