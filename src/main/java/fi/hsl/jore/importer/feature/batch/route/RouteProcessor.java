package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.entity.JrRoute;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.route.dto.ImportableRoute;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nullable;

public class RouteProcessor implements ItemProcessor<JrRoute, ImportableRoute> {

    @Override
    @Nullable
    public ImportableRoute process(final JrRoute item) {
        return ImportableRoute.of(
                ExternalIdUtil.forRoute(item),
                ExternalIdUtil.forLine(item.fkLine()),
                item.routeId().displayId(),
                item.routeId().hiddenVariantValue(),
                MultilingualString.empty()
                                  .with(JoreLocaleUtil.FINNISH, item.name())
                                  .with(JoreLocaleUtil.SWEDISH, item.nameSwedish())
        );
    }
}
