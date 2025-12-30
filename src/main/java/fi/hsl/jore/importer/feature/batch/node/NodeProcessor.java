package fi.hsl.jore.importer.feature.batch.node;

import fi.hsl.jore.importer.feature.batch.node.support.NodeTypeMapper;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Jore3Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import jakarta.annotation.Nullable;
import org.springframework.batch.item.ItemProcessor;

public class NodeProcessor implements ItemProcessor<JrNode, Jore3Node> {

    @Override
    @Nullable
    public Jore3Node process(final JrNode item) {
        final ExternalId id = ExternalIdUtil.forNode(item);
        final NodeType type = NodeTypeMapper.resolveNodeType(item.nodeType());
        final Jore3Node node = Jore3Node.of(id, type, item.location());

        return NodeType.STOP == type ? node.withProjectedLocation(item.projectedLocation()) : node;
    }
}
