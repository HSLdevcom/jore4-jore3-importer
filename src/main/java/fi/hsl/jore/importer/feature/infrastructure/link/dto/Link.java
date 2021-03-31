package fi.hsl.jore.importer.feature.infrastructure.link.dto;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinksRecord;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinksWithHistoryRecord;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface Link
        extends IHasPK<LinkPK>,
                IHasLinkExternalId,
                IHasSystemTime,
                ModifiableFields<Link> {

    static Link of(final InfrastructureLinksRecord record) {
        return ImmutableLink.builder()
                            .pk(LinkPK.of(record.getInfrastructureLinkId()))
                            .externalId(ExternalId.of(record.getInfrastructureLinkExtId()))
                            .networkType(NetworkType.of(record.getInfrastructureNetworkType()))
                            .geometry(record.getInfrastructureLinkGeog())
                            .points(Optional.ofNullable(record.getInfrastructureLinkPoints()))
                            .systemTime(record.getInfrastructureLinkSysPeriod())
                            .build();
    }

    static Link of(final InfrastructureLinksWithHistoryRecord record) {
        return ImmutableLink.builder()
                            .pk(LinkPK.of(record.getInfrastructureLinkId()))
                            .externalId(ExternalId.of(record.getInfrastructureLinkExtId()))
                            .networkType(NetworkType.of(record.getInfrastructureNetworkType()))
                            .geometry(record.getInfrastructureLinkGeog())
                            .points(Optional.ofNullable(record.getInfrastructureLinkPoints()))
                            .systemTime(record.getInfrastructureLinkSysPeriod())
                            .build();
    }
}
