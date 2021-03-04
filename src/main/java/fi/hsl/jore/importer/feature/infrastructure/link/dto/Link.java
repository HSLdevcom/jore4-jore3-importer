package fi.hsl.jore.importer.feature.infrastructure.link.dto;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinksRecord;
import org.immutables.value.Value;

@Value.Immutable
public interface Link
        extends IHasPK<LinkPK>,
                ModifiableFields<Link> {

    static Link of(final InfrastructureLinksRecord record) {
        return ImmutableLink.builder()
                            .pk(LinkPK.of(record.getInfrastructureLinkId()))
                            .networkTypePk(NetworkTypePK.of(record.getInfrastructureNetworkTypeId()))
                            .geometry(record.getInfrastructureLinkGeog())
                            .build();
    }
}
