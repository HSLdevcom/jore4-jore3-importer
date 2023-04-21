package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import fi.hsl.jore.importer.feature.jore4.util.ValidityPeriodUtil;
import fi.hsl.jore.importer.feature.network.line.dto.ImporterLine;
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
public class LineExportProcessor implements ItemProcessor<ImporterLine, Jore4Line> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LineExportProcessor.class);

    private static final int DEFAULT_PRIORITY = 10;

    @Override
    public Jore4Line process(final ImporterLine input) throws Exception {
        LOGGER.debug("Processing line: {}", input);
        final String externalId = input.externalId().value();

        final Short exportId = tryParseExportId(input.exportId());
        if (exportId == null) {
            // The id field is supposed to be a 4-digit number stored as string.
            // The field is required, so abort processing of whole line if we can't handle it.
            LOGGER.warn(
                "Skipping importing line {} from Jore3: could not parse export_id as short: {}.",
                externalId,
                input.exportId()
            );
            return null;
        }

        return Jore4Line.of(
                UUID.randomUUID(),
                externalId,
                exportId,
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

    private Short tryParseExportId(String exportId) {
         try {
            return Short.parseShort(exportId);
        } catch (final NumberFormatException ignored) {
            return null;
        }
    }
}
