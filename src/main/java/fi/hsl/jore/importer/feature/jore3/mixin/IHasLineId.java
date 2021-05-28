package fi.hsl.jore.importer.feature.jore3.mixin;


import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;

public interface IHasLineId {
    @JoreColumn(name = "lintunnus")
    LineId lineId();
}
