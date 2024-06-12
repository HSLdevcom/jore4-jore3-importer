package fi.hsl.jore.importer.feature.infrastructure.node.dto;

import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureNodesRecord;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureNodesWithHistoryRecord;
import java.util.Optional;
import org.immutables.value.Value;
import org.locationtech.jts.geom.Point;

@Value.Immutable
public interface Node extends IHasPK<NodePK>, IHasSystemTime, CommonFields<Node> {

    static Node of(
            final NodePK pk,
            final ExternalId externalId,
            final NodeType nodeType,
            final Point location,
            final Optional<Point> projectedLocation,
            final TimeRange systemTime) {
        return ImmutableNode.builder()
                .pk(pk)
                .externalId(externalId)
                .nodeType(nodeType)
                .location(location)
                .projectedLocation(projectedLocation)
                .systemTime(systemTime)
                .build();
    }

    static Node from(final InfrastructureNodesRecord record) {
        return of(
                NodePK.of(record.getInfrastructureNodeId()),
                ExternalId.of(record.getInfrastructureNodeExtId()),
                NodeType.of(record.getInfrastructureNodeType()),
                record.getInfrastructureNodeLocation(),
                Optional.ofNullable(record.getInfrastructureNodeProjectedLocation()),
                record.getInfrastructureNodeSysPeriod());
    }

    static Node from(final InfrastructureNodesWithHistoryRecord record) {
        return of(
                NodePK.of(record.getInfrastructureNodeId()),
                ExternalId.of(record.getInfrastructureNodeExtId()),
                NodeType.of(record.getInfrastructureNodeType()),
                record.getInfrastructureNodeLocation(),
                Optional.ofNullable(record.getInfrastructureNodeProjectedLocation()),
                record.getInfrastructureNodeSysPeriod());
    }
}
