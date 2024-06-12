package fi.hsl.jore.importer.feature.jore3.entity;

import fi.hsl.jore.importer.feature.jore3.enumerated.ClientOrganization;
import fi.hsl.jore.importer.feature.jore3.enumerated.PublicTransportDestination;
import fi.hsl.jore.importer.feature.jore3.enumerated.PublicTransportType;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.jore3.key.JrLinePk;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasLineId;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasPrimaryKey;
import fi.hsl.jore.importer.feature.jore3.mixin.IHasTransitType;
import fi.hsl.jore.importer.feature.jore3.style.JoreDtoStyle;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = JrLine.TABLE)
public interface JrLine extends IHasPrimaryKey<JrLinePk>, IHasLineId, IHasTransitType {

    String TABLE = "jr_linja";

    @Value.Derived
    default JrLinePk pk() {
        return JrLinePk.of(lineId());
    }

    @JoreColumn(name = "lintilorg", nullable = true, example = "HEL")
    ClientOrganization clientOrganization();

    @JoreColumn(name = "linjoukkollaji", nullable = true, example = "1")
    PublicTransportType publicTransportType();

    @JoreColumn(name = "linjlkohde", nullable = true, example = "9201")
    PublicTransportDestination publicTransportDestination();

    @JoreColumn(name = "linrunkolinja", example = "1")
    boolean isTrunkLine();

    static JrLine of(
            final LineId lineId,
            final TransitType transitType,
            final ClientOrganization clientOrganization,
            final PublicTransportType publicTransportType,
            final PublicTransportDestination publicTransportDestination,
            final boolean isTrunkLine) {
        return ImmutableJrLine.builder()
                .lineId(lineId)
                .transitType(transitType)
                .clientOrganization(clientOrganization)
                .publicTransportType(publicTransportType)
                .publicTransportDestination(publicTransportDestination)
                .isTrunkLine(isTrunkLine)
                .build();
    }
}
