package fi.hsl.jore.importer.feature.jore.mixin;

import fi.hsl.jore.importer.feature.jore.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore.mapping.JoreColumn;

public interface IHasNodes {
    @JoreColumn(name = "lnkalkuSolmu")
    NodeId startNode();

    @JoreColumn(name = "lnkloppuSolmu")
    NodeId endNode();
}
