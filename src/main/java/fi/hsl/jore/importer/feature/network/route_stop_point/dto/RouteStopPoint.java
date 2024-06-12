package fi.hsl.jore.importer.feature.network.route_stop_point.dto;

import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.jore3.enumerated.RegulatedTimingPointStatus;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.generated.RouteStopPointPK;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
public interface RouteStopPoint extends IHasPK<RouteStopPointPK>, IHasSystemTime, CommonFields {

    Optional<MultilingualString> viaName();

    static RouteStopPoint of(
            final RouteStopPointPK pk,
            final ExternalId externalId,
            final int orderNumber,
            final boolean hastusStopPoint,
            final RegulatedTimingPointStatus regulatedTimingPointStatus,
            final boolean viaPoint,
            final Optional<MultilingualString> viaName,
            final Optional<Integer> timetableColumn,
            final TimeRange systemTime) {
        return ImmutableRouteStopPoint.builder()
                .pk(pk)
                .externalId(externalId)
                .orderNumber(orderNumber)
                .hastusStopPoint(hastusStopPoint)
                .regulatedTimingPointStatus(regulatedTimingPointStatus)
                .viaPoint(viaPoint)
                .viaName(viaName)
                .timetableColumn(timetableColumn)
                .systemTime(systemTime)
                .build();
    }
}
