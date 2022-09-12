package fi.hsl.jore.importer.feature.network.route_stop_point.dto;


import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface Jore3RouteStopPoint
        extends CommonFields {

    Optional<MultilingualString> viaName();

    Jore3RouteStopPoint withHastusStopPoint(boolean hastusStopPoint);

    static Jore3RouteStopPoint of(final ExternalId externalId,
                                  final int orderNumber,
                                  final boolean hastusStopPoint,
                                  final boolean viaPoint,
                                  final Optional<MultilingualString> viaName,
                                  final Optional<Integer> timetableColumn) {
        return ImmutableJore3RouteStopPoint.builder()
                .externalId(externalId)
                .orderNumber(orderNumber)
                .hastusStopPoint(hastusStopPoint)
                .viaPoint(viaPoint)
                .viaName(viaName)
                .timetableColumn(timetableColumn)
                .build();
    }
}
