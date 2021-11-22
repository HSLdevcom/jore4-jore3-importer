package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.entity.JrScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImportableScheduledStopPoint;
import org.springframework.batch.item.ItemProcessor;

import java.util.Optional;

/**
 * Transforms the input data read from the source database into
 * a format which can be inserted into the target database.
 */
public class ScheduledStopPointImportProcessor implements ItemProcessor<JrScheduledStopPoint, ImportableScheduledStopPoint> {

    @Override
    public ImportableScheduledStopPoint process(final JrScheduledStopPoint input) throws Exception {
        return ImportableScheduledStopPoint.of(
                ExternalId.of(input.pk().nodeId().value()),
                input.elyNumber(),
                MultilingualString.empty()
                        .with(JoreLocaleUtil.FINNISH, input.nameFinnish())
                        .with(JoreLocaleUtil.SWEDISH, input.nameSwedish()),
                constructShortId(input)
        );
    }

    private Optional<String> constructShortId(final JrScheduledStopPoint input) {
        final String shortId = input.shortLetter().orElse("") +
                input.shortId().orElse("");
       return Optional.of(shortId).filter(id -> !id.isEmpty());
    }
}
