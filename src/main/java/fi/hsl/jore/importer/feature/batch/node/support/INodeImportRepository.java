package fi.hsl.jore.importer.feature.batch.node.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Jore3Node;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.generated.NodePK;

public interface INodeImportRepository extends IImportRepository<Jore3Node, NodePK> {}
