package fi.hsl.jore.importer.feature.network.route_stop_point.dto;


import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface ImportableRouteStopPoint
        extends CommonFields {

    Optional<MultilingualString> viaName();

    ImportableRouteStopPoint withHastusStopPoint(boolean hastusStopPoint);

    static ImportableRouteStopPoint of(final ExternalId externalId,
                                       final int orderNumber,
                                       final boolean hastusStopPoint,
                                       final boolean viaPoint,
                                       final Optional<MultilingualString> viaName,
                                       final Optional<Integer> timetableColumn) {
        return ImmutableImportableRouteStopPoint.builder()
                .externalId(externalId)
                .orderNumber(orderNumber)
                .hastusStopPoint(hastusStopPoint)
                .viaPoint(viaPoint)
                .viaName(viaName)
                .timetableColumn(timetableColumn)
                .build();
    }
}
