package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore3.key.JrLineHeaderPk;
import fi.hsl.jore.importer.feature.jore3.key.JrLinePk;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreForeignKey;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasDuration;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasLineId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.Optional;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrLineHeader.TABLE)
public interface JrLineHeader
        extends IHasPrimaryKey<JrLineHeaderPk>,
                IHasLineId,
                IHasDuration {

    String TABLE = "jr_linjannimet";

    @Value.Derived
    default JrLineHeaderPk pk() {
        return JrLineHeaderPk.of(lineId(),
                                 validFrom(),
                                 validTo());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = JrLine.TABLE)
    default JrLinePk fkLine() {
        return JrLinePk.of(lineId());
    }

    @JoreColumn(name = "linalkupvm",
                example = "2013-01-01 00:00:00.000")
    LocalDate validFrom();

    @JoreColumn(name = "linloppupvm",
                example = "2019-06-02 00:00:00.000")
    LocalDate validTo();

    @JoreColumn(name = "linnimi",
                example = "Olympiaterminaali - Kamppi (M) - Ooppera")
    String name();

    @JoreColumn(name = "linnimilyh",
                nullable = true,
                example = "Olympiaterm.-Ooppera")
    Optional<String> nameShort();

    @JoreColumn(name = "linnimir",
                nullable = true,
                example = "Olympiaterminalen - Kampen (M) - Operan")
    Optional<String> nameSwedish();

    @JoreColumn(name = "linnimilyh",
                nullable = true,
                example = "Olympiaterm.-Operan")
    Optional<String> nameShortSwedish();

    @JoreColumn(name = "linlahtop1",
                nullable = true,
                example = "Kauppatori / Eira")
    Optional<String> origin1();

    @JoreColumn(name = "linlahtop1r",
                nullable = true,
                example = "Salutorget / Eira")
    Optional<String> origin1Swedish();

    @JoreColumn(name = "linlahtop2",
                nullable = true,
                example = "Martinlaakso")
    Optional<String> origin2();

    @JoreColumn(name = "linlahtop2r",
                nullable = true,
                example = "Mårtensdal")
    Optional<String> origin2Swedish();

    static JrLineHeader of(final LineId lineId,
                           final LocalDate validFrom,
                           final LocalDate validTo,
                           final String name,
                           final Optional<String> nameShort,
                           final Optional<String> nameSwedish,
                           final Optional<String> nameShortSwedish,
                           final Optional<String> origin1,
                           final Optional<String> origin1Swedish,
                           final Optional<String> origin2,
                           final Optional<String> origin2Swedish) {
        return ImmutableJrLineHeader.builder()
                                    .lineId(lineId)
                                    .validFrom(validFrom)
                                    .validTo(validTo)
                                    .name(name)
                                    .nameShort(nameShort)
                                    .nameSwedish(nameSwedish)
                                    .nameShortSwedish(nameShortSwedish)
                                    .origin1(origin1)
                                    .origin1Swedish(origin1Swedish)
                                    .origin2(origin2)
                                    .origin2Swedish(origin2Swedish)
                                    .build();
    }
}
