package fi.hsl.jore.importer.feature.batch.line_header;

import static org.assertj.core.api.Assertions.assertThat;

import fi.hsl.jore.importer.feature.jore3.entity.JrLineHeader;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.network.line_header.dto.Jore3LineHeader;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class LineHeaderProcessorTest {

    private final LineHeaderProcessor processor = new LineHeaderProcessor();

    @Test
    void filtersLineHeaderWhoseValidityEndedBeforeCutoff() {
        final JrLineHeader expiredHeader = lineHeaderWithValidTo(LineHeaderProcessor.VALID_TO_CUTOFF.minusDays(1));
        final @Nullable Jore3LineHeader result = processor.process(expiredHeader);

        assertThat(result).isNull();
    }

    @Test
    void retainsLineHeaderWhoseValidityEndsOnCutoffDate() {
        final JrLineHeader cutoffDateHeader = lineHeaderWithValidTo(LineHeaderProcessor.VALID_TO_CUTOFF);
        final @Nullable Jore3LineHeader result = processor.process(cutoffDateHeader);

        assertThat(result).isNotNull();
    }

    private static JrLineHeader lineHeaderWithValidTo(final LocalDate validTo) {
        return JrLineHeader.of(
                LineId.from("1001"),
                validTo.minusYears(1),
                validTo,
                "Line header",
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }
}



