package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore3.entity.JrRoute;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.network.route.dto.Jore3Route;
import javax.annotation.Nullable;
import org.springframework.batch.item.ItemProcessor;

public class RouteProcessor implements ItemProcessor<JrRoute, Jore3Route> {

    @Override
    @Nullable
    public Jore3Route process(final JrRoute item) {
        return Jore3Route.of(
                ExternalIdUtil.forRoute(item),
                ExternalIdUtil.forLine(item.fkLine()),
                item.routeId().displayId(),
                item.routeId().hiddenVariantValue(),
                MultilingualString.empty()
                        .with(JoreLocaleUtil.FINNISH, item.name())
                        .with(JoreLocaleUtil.SWEDISH, item.nameSwedish()),
                LegacyHslMunicipalityCode.of(item.routeId().destination()));
    }
}
