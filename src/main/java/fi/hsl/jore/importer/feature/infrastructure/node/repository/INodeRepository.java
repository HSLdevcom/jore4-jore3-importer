package fi.hsl.jore.importer.feature.infrastructure.node.repository;

import fi.hsl.jore.importer.feature.infrastructure.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;
import io.vavr.collection.List;

public interface INodeRepository extends IBasicCrudRepository<NodePK, Node> {
    List<NodePK> upsert(Iterable<? extends PersistableNode> nodes);
}
