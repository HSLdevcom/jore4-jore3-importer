package fi.hsl.jore.importer.feature.batch.route_direction;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.batch.util.DirectionToDirectionTypeMapper;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteDirection;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.route_direction.dto.Jore3RouteDirection;
import javax.annotation.Nullable;
import org.springframework.batch.item.ItemProcessor;

public class RouteDirectionProcessor implements ItemProcessor<JrRouteDirection, Jore3RouteDirection> {

    @Override
    @Nullable
    public Jore3RouteDirection process(final JrRouteDirection item) {
        return Jore3RouteDirection.of(
                ExternalIdUtil.forRouteDirection(item),
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
                DateRange.between(item.validFrom(), item.validTo()));
    }
}
