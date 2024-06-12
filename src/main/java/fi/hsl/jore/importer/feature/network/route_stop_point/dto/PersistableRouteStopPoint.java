package fi.hsl.jore.importer.feature.network.route_stop_point.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.enumerated.RegulatedTimingPointStatus;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
public interface PersistableRouteStopPoint extends CommonFields {

    static PersistableRouteStopPoint of(
            final ExternalId externalId,
            final int orderNumber,
            final boolean hastusStopPoint,
            final RegulatedTimingPointStatus regulatedTimingPointStatus,
            final Optional<Integer> timetableColumn) {
        return ImmutablePersistableRouteStopPoint.builder()
                .externalId(externalId)
                .orderNumber(orderNumber)
                .hastusStopPoint(hastusStopPoint)
                .regulatedTimingPointStatus(regulatedTimingPointStatus)
                .timetableColumn(timetableColumn)
                .build();
    }
}
