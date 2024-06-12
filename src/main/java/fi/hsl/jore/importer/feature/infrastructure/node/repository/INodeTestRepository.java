package fi.hsl.jore.importer.feature.infrastructure.node.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;

/**
 * Only for testing, use {@link
 * fi.hsl.jore.importer.feature.batch.node.support.INodeImportRepository this} to
 * insert/update/delete nodes.
 */
@VisibleForTesting
public interface INodeTestRepository
        extends IBasicCrudRepository<NodePK, Node, PersistableNode>, IHistoryRepository<Node> {}
