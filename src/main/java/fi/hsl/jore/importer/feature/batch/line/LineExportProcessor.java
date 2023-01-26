package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.network.line.dto.ExportableLine;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;
import fi.hsl.jore.importer.feature.transmodel.entity.VehicleMode;
import fi.hsl.jore.importer.feature.transmodel.util.ValidityPeriodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Transforms the information of an exported line into a format
 * that can be inserted into the Jore 4 database.
 */
@Component
public class LineExportProcessor implements ItemProcessor<ExportableLine, TransmodelLine> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LineExportProcessor.class);

    private static final int DEFAULT_PRIORITY = 10;

    @Override
    public TransmodelLine process(final ExportableLine input) throws Exception {
        LOGGER.debug("Processing line: {}", input);

        return TransmodelLine.of(
                UUID.randomUUID(),
                input.externalId().value(),
                input.lineNumber(),
                input.name(),
                input.shortName(),
                VehicleMode.of(input.networkType()),
                input.typeOfLine(),
                DEFAULT_PRIORITY,
                ValidityPeriodUtil.constructValidityPeriodStartDay(input.validDateRange().range()),
                ValidityPeriodUtil.constructValidityPeriodEndDay(input.validDateRange().range()),
                input.legacyHslMunicipalityCode()
        );
    }
}
