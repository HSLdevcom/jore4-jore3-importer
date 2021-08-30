package fi.hsl.jore.importer.feature.infrastructure.link_shape.dto;


import fi.hsl.jore.importer.config.jooq.converter.time_range.TimeRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.generated.LinkShapePK;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinkShapesRecord;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.records.InfrastructureLinkShapesWithHistoryRecord;
import org.immutables.value.Value;
import org.locationtech.jts.geom.LineString;

@Value.Immutable
public interface LinkShape
        extends IHasPK<LinkShapePK>,
                IHasSystemTime,
                CommonFields<LinkShape> {

    LinkPK linkId();

    static LinkShape of(final LinkShapePK pk,
                        final LinkPK linkId,
                        final ExternalId linkExternalId,
                        final LineString geometry,
                        final TimeRange systemTime) {
        return ImmutableLinkShape.builder()
                                 .pk(pk)
                                 .linkId(linkId)
                                 .linkExternalId(linkExternalId)
                                 .geometry(geometry)
                                 .systemTime(systemTime)
                                 .build();
    }

    static LinkShape from(final InfrastructureLinkShapesRecord record) {
        return of(
                LinkShapePK.of(record.getInfrastructureLinkShapeId()),
                LinkPK.of(record.getInfrastructureLinkId()),
                ExternalId.of(record.getInfrastructureLinkExtId()),
                record.getInfrastructureLinkShape(),
                record.getInfrastructureLinkShapeSysPeriod()
        );
    }

    static LinkShape from(final InfrastructureLinkShapesWithHistoryRecord record) {
        return of(
                LinkShapePK.of(record.getInfrastructureLinkShapeId()),
                LinkPK.of(record.getInfrastructureLinkId()),
                ExternalId.of(record.getInfrastructureLinkExtId()),
                record.getInfrastructureLinkShape(),
                record.getInfrastructureLinkShapeSysPeriod()
        );
    }
}
