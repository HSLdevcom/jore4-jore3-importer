package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.network.route.dto.ExportableRoute;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRoute;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteDirection;
import fi.hsl.jore.importer.feature.transmodel.util.ValidityPeriodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Transforms the information of a route into a format that can be inserted
 * into the Jore 4 database.
 */
@Component
public class RouteExportProcessor implements ItemProcessor<ExportableRoute, TransmodelRoute> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteExportProcessor.class);

    private static final int DEFAULT_PRIORITY = 10;

    @Override
    public TransmodelRoute process(final ExportableRoute input) throws Exception {
        LOGGER.debug("Processing route: {}", input);

        return TransmodelRoute.of(
                UUID.randomUUID(),
                input.name(),
                TransmodelRouteDirection.of(input.directionType()),
                input.directionId(),
                input.routeNumber(),
                input.lineTransmodelId(),
                DEFAULT_PRIORITY,
                ValidityPeriodUtil.constructValidityPeriodStartDay(input.validDateRange().range()),
                ValidityPeriodUtil.constructValidityPeriodEndDay(input.validDateRange().range())
        );
    }
}
