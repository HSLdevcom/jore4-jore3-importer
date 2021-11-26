package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.network.line.dto.ExportableLine;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelLine;
import fi.hsl.jore.importer.feature.transmodel.entity.VehicleMode;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Transforms the information of an exported line into a format
 * that can be inserted into the Jore 4 database.
 */
@Component
public class LineExportProcessor implements ItemProcessor<ExportableLine, TransmodelLine> {

    @Override
    public TransmodelLine process(final ExportableLine input) throws Exception {
        return TransmodelLine.of(
                input.name(),
                input.shortName(),
                VehicleMode.of(input.networkType())
        );
    }
}
