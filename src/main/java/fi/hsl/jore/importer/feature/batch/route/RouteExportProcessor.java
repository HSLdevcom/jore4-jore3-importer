package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.network.route.dto.ImporterRoute;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Route;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteDirection;
import fi.hsl.jore.importer.feature.jore4.util.ValidityPeriodUtil;
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
public class RouteExportProcessor implements ItemProcessor<ImporterRoute, Jore4Route> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteExportProcessor.class);

    private static final int DEFAULT_PRIORITY = 10;

    @Override
    public Jore4Route process(final ImporterRoute input) throws Exception {
        LOGGER.debug("Processing route: {}", input);

        return Jore4Route.of(
                UUID.randomUUID(),
                input.name(),
                Jore4RouteDirection.of(input.directionType()),
                input.directionId(),
                input.routeNumber(),
                input.hiddenVariant(),
                input.lineTransmodelId(),
                DEFAULT_PRIORITY,
                ValidityPeriodUtil.constructValidityPeriodStartDay(input.validDateRange().range()),
                ValidityPeriodUtil.constructValidityPeriodEndDay(input.validDateRange().range()),
                input.legacyHslMunicipalityCode()
        );
    }
}
