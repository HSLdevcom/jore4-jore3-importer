package fi.hsl.jore.importer.feature.batch.node;

import fi.hsl.jore.importer.feature.batch.node.support.NodeTypeMapper;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.ImportableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class NodeProcessor implements ItemProcessor<JrNode, ImportableNode> {

    @Override
    @Nullable
    public ImportableNode process(final JrNode item) {
        final ExternalId id = ExternalIdUtil.forNode(item);
        final NodeType type = NodeTypeMapper.resolveNodeType(item.nodeType());
        final ImportableNode node = ImportableNode.of(id, type, item.location());

        return NodeType.STOP == type ?
                node.withProjectedLocation(item.projectedLocation()) :
                node;
    }
}
