package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.network.route.dto.ExportableRoute;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRoute;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteDirection;
import fi.hsl.jore.importer.feature.transmodel.util.ValidityPeriodUtil;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Transforms the information of a route into a format that can be inserted
 * into the Jore 4 database.
 */
@Component
public class RouteExportProcessor implements ItemProcessor<ExportableRoute, TransmodelRoute> {

    private static final int DEFAULT_PRIORITY = 10;

    @Override
    public TransmodelRoute process(final ExportableRoute input) throws Exception {
        return TransmodelRoute.of(
                UUID.randomUUID().toString(),
                input.name(),
                TransmodelRouteDirection.of(input.directionType()),
                input.externalId().value(),
                input.routeNumber(),
                input.lineTransmodelId().toString(),
                DEFAULT_PRIORITY,
                input.startScheduledStopPointTransmodelId().toString(),
                input.endScheduledStopPointTransmodelId().toString(),
                ValidityPeriodUtil.constructValidityPeriodStartTime(input.validDateRange().range()),
                ValidityPeriodUtil.constructValidityPeriodEndTime(input.validDateRange().range())
        );
    }
}
