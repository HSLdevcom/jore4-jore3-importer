package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPattern;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JourneyPatternExportProcessorTest {

    private static final UUID ROUTE_DIRECTION_EXT_ID = UUID.fromString("a6fb1824-0cdc-49cf-bd43-7e931ffd249a");
    private static final UUID ROUTE_ID = UUID.fromString("5bfa9a65-c80f-4af8-be95-8370cb12df50");
    private static final ImporterJourneyPattern INPUT = ImporterJourneyPattern.of(
            ROUTE_DIRECTION_EXT_ID,
            ROUTE_ID
    );


    private final JourneyPatternExportProcessor processor = new JourneyPatternExportProcessor();

    @Test
    @DisplayName("Should return a journey pattern with generated id")
    void shouldReturnJourneyPatternWithGeneratedId() throws Exception {
        final Jore4JourneyPattern returned = processor.process(INPUT);
        assertThat(returned.journeyPatternId()).isNotNull();
    }

    @Test
    @DisplayName("Should return a journey pattern with the correct route direction ext id")
    void shouldReturnJourneyPatternWithCorrectRouteDirectionExtId() throws Exception {
        final Jore4JourneyPattern returned = processor.process(INPUT);
        assertThat(returned.routeDirectionExtId()).isEqualTo(ROUTE_DIRECTION_EXT_ID);
    }

    @Test
    @DisplayName("Should return a journey pattern with the correct route id")
    void shouldReturnJourneyPatternWithCorrectRouteId() throws Exception {
        final Jore4JourneyPattern returned = processor.process(INPUT);
        assertThat(returned.routeId()).isEqualTo(ROUTE_ID);
    }
}
