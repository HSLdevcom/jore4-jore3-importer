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
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

import java.util.Optional;
import java.util.UUID;

/**
 * Contains the information of a scheduled stop point which
 * is read from the target database.
 */
@Value.Immutable
public interface ScheduledStopPoint
        extends IHasPK<ScheduledStopPointPK>,
        CommonFields<ScheduledStopPoint>,
        IHasSystemTime
{

    NodePK node();

    Optional<UUID> transmodelId();

    static ScheduledStopPoint of(final ScheduledStopPointPK pk,
                                 final ExternalId externalId,
                                 final Optional<Long> elyNumber,
                                 final NodePK node,
                                 final MultilingualString name,
                                 final Optional<String> shortId,
                                 final Optional<UUID> transmodelId,
                                 final TimeRange systemTime) {
        return ImmutableScheduledStopPoint.builder()
                .pk(pk)
                .externalId(externalId)
                .elyNumber(elyNumber)
                .node(node)
                .name(name)
                .shortId(shortId)
                .transmodelId(transmodelId)
                .systemTime(systemTime)
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
                Optional.ofNullable(record.getScheduledStopPointTransmodelId()),
                record.getScheduledStopPointSysPeriod()
        );
    }

    static ScheduledStopPoint from(final ScheduledStopPointsWithHistoryRecord record, final IJsonbConverter converter) {
        return ScheduledStopPoint.of(
                ScheduledStopPointPK.of(record.getScheduledStopPointId()),
                ExternalId.of(record.getScheduledStopPointExtId()),
                Optional.ofNullable(record.getScheduledStopPointElyNumber()),
                NodePK.of(record.getInfrastructureNodeId()),
                converter.fromJson(record.getScheduledStopPointName(), MultilingualString.class),
                Optional.of(record.getScheduledStopPointShortId()),
                Optional.ofNullable(record.getScheduledStopPointTransmodelId()),
                record.getScheduledStopPointSysPeriod()
        );
    }
}
