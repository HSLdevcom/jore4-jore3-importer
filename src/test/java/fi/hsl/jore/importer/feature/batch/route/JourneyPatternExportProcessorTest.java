package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.network.route.dto.ExportableJourneyPattern;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JourneyPatternExportProcessorTest {

    private static final UUID ROUTE_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");
    private static final ExportableJourneyPattern INPUT = ExportableJourneyPattern.of(ROUTE_ID);


    private final JourneyPatternExportProcessor processor = new JourneyPatternExportProcessor();

    @Test
    @DisplayName("Should return a journey pattern with generated id")
    void shouldReturnJourneyPatternWithGeneratedId() throws Exception {
        final TransmodelJourneyPattern returned = processor.process(INPUT);
        assertThat(returned.journeyPatternId()).isNotNull();
    }

    @Test
    @DisplayName("Should return a journey pattern with the correct route id")
    void shouldReturnJourneyPatternWithCorrectRouteId() throws Exception {
        final TransmodelJourneyPattern returned = processor.process(INPUT);
        assertThat(returned.routeId()).isEqualTo(ROUTE_ID.toString());
    }
}