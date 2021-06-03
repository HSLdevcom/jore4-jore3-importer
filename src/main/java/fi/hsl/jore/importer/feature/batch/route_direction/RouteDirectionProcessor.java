package fi.hsl.jore.importer.feature.batch.route_direction;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.batch.util.DirectionToDirectionTypeMapper;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteDirection;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.route_direction.dto.ImportableRouteDirection;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class RouteDirectionProcessor implements ItemProcessor<JrRouteDirection, ImportableRouteDirection> {

    @Override
    @Nullable
    public ImportableRouteDirection process(final JrRouteDirection item) {
        return ImportableRouteDirection.of(ExternalIdUtil.forRouteDirection(item),
                                           ExternalIdUtil.forRoute(item.fkRoute()),
                                           DirectionToDirectionTypeMapper.resolveDirectionType(item.direction()),
                                           item.lengthMeters(),
                                           MultilingualString.empty()
                                                             .with(JoreLocaleUtil.FINNISH, item.name())
                                                             .with(JoreLocaleUtil.SWEDISH, item.nameSwedish()),
                                           MultilingualString.empty()
                                                             .with(JoreLocaleUtil.FINNISH, item.nameShort())
                                                             .with(JoreLocaleUtil.SWEDISH, item.nameShortSwedish()),
                                           MultilingualString.empty()
                                                             .with(JoreLocaleUtil.FINNISH, item.origin())
                                                             .with(JoreLocaleUtil.SWEDISH, item.originSwedish()),
                                           MultilingualString.empty()
                                                             .with(JoreLocaleUtil.FINNISH, item.destination())
                                                             .with(JoreLocaleUtil.SWEDISH, item.destinationSwedish()),
                                           DateRange.between(item.validFrom(),
                                                             item.validTo()));
    }
}
