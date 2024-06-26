package fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto;

import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.converter.IJsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.generated.ScheduledStopPointPK;
import fi.hsl.jore.importer.jooq.network.tables.records.ScheduledStopPointsRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.ScheduledStopPointsWithHistoryRecord;
import java.util.Optional;
import java.util.UUID;
import org.immutables.value.Value;

/** Contains the information of a scheduled stop point which is read from the target database. */
@Value.Immutable
public interface ScheduledStopPoint
        extends IHasPK<ScheduledStopPointPK>, CommonFields<ScheduledStopPoint>, IHasSystemTime {

    NodePK node();

    Optional<UUID> placeId();

    Optional<UUID> jore4Id();

    /** Number of times used in routes */
    int usageInRoutes();

    static ScheduledStopPoint of(
            final ScheduledStopPointPK pk,
            final ExternalId stopPointExternalId,
            final Optional<Long> elyNumber,
            final NodePK node,
            final MultilingualString name,
            final Optional<String> shortId,
            final Optional<UUID> placeId,
            final Optional<UUID> jore4Id,
            final int usageInRoutes,
            final TimeRange systemTime) {
        return ImmutableScheduledStopPoint.builder()
                .pk(pk)
                .externalId(stopPointExternalId)
                .elyNumber(elyNumber)
                .node(node)
                .name(name)
                .shortId(shortId)
                .placeId(placeId)
                .jore4Id(jore4Id)
                .systemTime(systemTime)
                .usageInRoutes(usageInRoutes)
                .build();
    }

    static ScheduledStopPoint from(final ScheduledStopPointsRecord record, final IJsonbConverter converter) {
        return ScheduledStopPoint.of(
                ScheduledStopPointPK.of(record.getScheduledStopPointId()),
                ExternalId.of(record.getScheduledStopPointExtId()),
                Optional.ofNullable(record.getScheduledStopPointElyNumber()),
                NodePK.of(record.getInfrastructureNodeId()),
                converter.fromJson(record.getScheduledStopPointName(), MultilingualString.class),
                Optional.of(record.getScheduledStopPointShortId()),
                Optional.ofNullable(record.getNetworkPlaceId()),
                Optional.ofNullable(record.getScheduledStopPointJore4Id()),
                record.getUsageInRoutes(),
                record.getScheduledStopPointSysPeriod());
    }

    static ScheduledStopPoint from(final ScheduledStopPointsWithHistoryRecord record, final IJsonbConverter converter) {
        return ScheduledStopPoint.of(
                ScheduledStopPointPK.of(record.getScheduledStopPointId()),
                ExternalId.of(record.getScheduledStopPointExtId()),
                Optional.ofNullable(record.getScheduledStopPointElyNumber()),
                NodePK.of(record.getInfrastructureNodeId()),
                converter.fromJson(record.getScheduledStopPointName(), MultilingualString.class),
                Optional.of(record.getScheduledStopPointShortId()),
                Optional.ofNullable(record.getNetworkPlaceId()),
                Optional.ofNullable(record.getScheduledStopPointJore4Id()),
                record.getUsageInRoutes(),
                record.getScheduledStopPointSysPeriod());
    }
}
