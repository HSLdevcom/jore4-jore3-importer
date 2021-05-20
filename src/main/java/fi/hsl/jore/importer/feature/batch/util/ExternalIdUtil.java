package fi.hsl.jore.importer.feature.batch.util;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.entity.JrLink;
import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import fi.hsl.jore.importer.feature.jore3.key.JrNodePk;

public final class ExternalIdUtil {

    private ExternalIdUtil() {
    }

    public static ExternalId forNode(final NodeId nodeId) {
        return ExternalId.of(nodeId.value());
    }

    public static ExternalId forNode(final JrNodePk nodePk) {
        return forNode(nodePk.nodeId());
    }

    public static ExternalId forNode(final JrNode node) {
        return forNode(node.pk());
    }

    public static ExternalId forLink(final TransitType transitType,
                                     final NodeId from,
                                     final NodeId to) {
        return ExternalId.of(String.format("%d-%s-%s",
                                           transitType.value(),
                                           from.value(),
                                           to.value()));
    }

    public static ExternalId forLink(final JrLinkPk linkPk) {
        return forLink(linkPk.transitType(),
                       linkPk.startNode(),
                       linkPk.endNode());
    }

    public static ExternalId forLink(final JrLink link) {
        return forLink(link.pk());
    }
}
