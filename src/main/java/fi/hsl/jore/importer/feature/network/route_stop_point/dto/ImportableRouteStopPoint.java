package fi.hsl.jore.importer.feature.network.route_stop_point.dto;


import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface ImportableRouteStopPoint
        extends CommonFields {

    ImportableRouteStopPoint withHastusStopPoint(boolean hastusStopPoint);

    static ImportableRouteStopPoint of(final ExternalId externalId,
                                       final int orderNumber,
                                       final boolean hastusStopPoint,
                                       final boolean viaPoint,
                                       final Optional<Integer> timetableColumn) {
        return ImmutableImportableRouteStopPoint.builder()
                .externalId(externalId)
                .orderNumber(orderNumber)
                .hastusStopPoint(hastusStopPoint)
                .viaPoint(viaPoint)
                .timetableColumn(timetableColumn)
                .build();
    }
}
