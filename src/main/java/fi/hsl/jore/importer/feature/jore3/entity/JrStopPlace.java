package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.field.generated.StopPlaceId;
import fi.hsl.jore.importer.feature.jore3.key.JrStopPlacePK;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrStopPlace.TABLE)
public interface JrStopPlace extends IHasPrimaryKey<JrStopPlacePK> {

    String TABLE = "jr_lij_pysakkialue";

    @JoreColumn(name = "nimi", example = "Ritarihuone")
    Optional<String> nameFinnish();

    @JoreColumn(name = "nimir", nullable = true, example = "Riddarhuset")
    Optional<String> nameSwedish();

    @JoreColumn(name = "nimipitka", nullable = true, example = "Ritarihuoneen koko nimi")
    Optional<String> nameLongFinnish();

    @JoreColumn(name = "nimipitkar", nullable = true, example = "Ridderhuset hela namn")
    Optional<String> nameLongSwedish();

    @JoreColumn(name = "paikannimi", nullable = true, example = "Kamppi")
    Optional<String> locationFinnish();

    @JoreColumn(name = "paikannimir", nullable = true, example = "Kampen")
    Optional<String> locationSwedish();

    static JrStopPlace of(
            final StopPlaceId stopPlaceId,
            final Optional<String> nameFinnish,
            final Optional<String> nameSwedish,
            final Optional<String> nameLongFinnish,
            final Optional<String> nameLongSwedish,
            final Optional<String> locationFinnish,
            final Optional<String> locationSwedish
    ) {
        return ImmutableJrStopPlace.builder()
                .pk(JrStopPlacePK.of(stopPlaceId))
                .nameFinnish(nameFinnish)
                .nameSwedish(nameSwedish)
                .nameLongFinnish(nameLongFinnish)
                .nameLongSwedish(nameLongSwedish)
                .locationFinnish(locationFinnish)
                .locationSwedish(locationSwedish)
                .build();
    }
}
