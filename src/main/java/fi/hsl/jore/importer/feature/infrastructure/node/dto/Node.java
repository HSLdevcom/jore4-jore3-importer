package fi.hsl.jore.importer.feature.infrastructure.node.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureNodesRecord;
import org.immutables.value.Value;

@Value.Immutable
public interface Node
        extends IHasPK<NodePK>,
                IHasNodeExternalId,
                ModifiableFields<Node> {

    static Node of(final InfrastructureNodesRecord record) {
        return ImmutableNode.builder()
                            .pk(NodePK.of(record.getInfrastructureNodeId()))
                            .externalId(ExternalId.of(record.getInfrastructureNodeExtId()))
                            .nodeType(NodeType.of(record.getInfrastructureNodeType()))
                            .location(record.getInfrastructureNodeLocation())
                            .build();
    }
}
