package fi.hsl.jore.importer.feature.batch.node;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.jore.entity.JrNode;
import io.vavr.collection.List;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class NodeProcessor implements ItemProcessor<JrNode, List<PersistableNode>> {

    @Override
    @Nullable
    public List<PersistableNode> process(final JrNode item) {
        final ExternalId id = ExternalId.of(item.nodeId().value());
        switch (item.nodeType()) {
            case BUS_STOP:
                return List.of(PersistableNode.of(id, NodeType.STOP, item.location()),
                               PersistableNode.of(id, NodeType.STOP_PROJECTED, item.projectedLocation()));
            case CROSSROADS:
                return List.of(PersistableNode.of(id, NodeType.CROSSROADS, item.location()));
            case MUNICIPAL_BORDER:
                return List.of(PersistableNode.of(id, NodeType.BORDER, item.location()));
            case UNKNOWN:
            default:
                return List.empty();
        }
    }
}
