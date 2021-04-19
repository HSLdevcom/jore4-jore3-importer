package fi.hsl.jore.importer.feature.batch.node.support;

import fi.hsl.jore.importer.feature.infrastructure.node.dto.NodeType;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public final class NodeTypeMapper {

    public static final Map<fi.hsl.jore.importer.feature.jore3.field.NodeType, NodeType> TO_NODE_TYPE =
            HashMap.of(
                    fi.hsl.jore.importer.feature.jore3.field.NodeType.BUS_STOP, NodeType.STOP,
                    fi.hsl.jore.importer.feature.jore3.field.NodeType.CROSSROADS, NodeType.CROSSROADS,
                    fi.hsl.jore.importer.feature.jore3.field.NodeType.MUNICIPAL_BORDER, NodeType.BORDER
            );

    private NodeTypeMapper() {
    }

    public static NodeType resolveNodeType(final fi.hsl.jore.importer.feature.jore3.field.NodeType nodeType) {
        return TO_NODE_TYPE.getOrElse(nodeType,
                                      NodeType.UNKNOWN);
    }
}
