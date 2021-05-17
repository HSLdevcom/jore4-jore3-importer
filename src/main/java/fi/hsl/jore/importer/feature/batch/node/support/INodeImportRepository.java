package fi.hsl.jore.importer.feature.batch.node.support;

import fi.hsl.jore.importer.feature.batch.util.IImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.ImportableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;

public interface INodeImportRepository extends IImportRepository<ImportableNode, NodePK> {
}
