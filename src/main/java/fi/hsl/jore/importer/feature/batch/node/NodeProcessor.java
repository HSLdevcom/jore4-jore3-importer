package fi.hsl.jore.importer.feature.batch.node;

import fi.hsl.jore.importer.feature.batch.node.support.NodeTypeMapper;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class NodeProcessor implements ItemProcessor<JrNode, PersistableNode> {

    @Override
    @Nullable
    public PersistableNode process(final JrNode item) {
        final ExternalId id = ExternalId.of(item.nodeId().value());
        final NodeType type = NodeTypeMapper.resolveNodeType(item.nodeType());
        final PersistableNode node = PersistableNode.of(id, type, item.location());

        return NodeType.STOP == type ?
                node.withProjectedLocation(item.projectedLocation()) :
                node;
    }
}
