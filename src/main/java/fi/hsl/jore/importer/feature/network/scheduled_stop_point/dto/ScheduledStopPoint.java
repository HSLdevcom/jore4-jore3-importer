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

    static ScheduledStopPoint of(final ScheduledStopPointPK pk,
                                 final ExternalId externalId,
                                 final NodePK node,
                                 final MultilingualString name,
                                 final TimeRange systemTime) {
        return ImmutableScheduledStopPoint.builder()
                .pk(pk)
                .externalId(externalId)
                .node(node)
                .name(name)
                .systemTime(systemTime)
                .build();
    }

    static ScheduledStopPoint from(final ScheduledStopPointsRecord record, final IJsonbConverter converter) {
        return ScheduledStopPoint.of(
                ScheduledStopPointPK.of(record.getScheduledStopPointId()),
                ExternalId.of(record.getScheduledStopPointExtId()),
                NodePK.of(record.getInfrastructureNodeId()),
                converter.fromJson(record.getScheduledStopPointName(), MultilingualString.class),
                record.getScheduledStopPointSysPeriod()
        );
    }

    static ScheduledStopPoint from(final ScheduledStopPointsWithHistoryRecord record, final IJsonbConverter converter) {
        return ScheduledStopPoint.of(
                ScheduledStopPointPK.of(record.getScheduledStopPointId()),
                ExternalId.of(record.getScheduledStopPointExtId()),
                NodePK.of(record.getInfrastructureNodeId()),
                converter.fromJson(record.getScheduledStopPointName(), MultilingualString.class),
                record.getScheduledStopPointSysPeriod()
        );
    }
}
