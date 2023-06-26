package fi.hsl.jore.importer.feature.batch.place;

import fi.hsl.jore.importer.feature.jore3.entity.JrPlace;
import fi.hsl.jore.importer.feature.jore3.field.generated.PlaceId;
import fi.hsl.jore.importer.feature.network.place.dto.PersistablePlace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaceImportProcessorTest {

    private static final String PLACE_ID = "1KALA";
    private static final String PLACE_NAME = "Kalasatama";

    private final PlaceImportProcessor processor = new PlaceImportProcessor();

    private final JrPlace input = JrPlace.of(PlaceId.of(PLACE_ID), PLACE_NAME);

    @Test
    @DisplayName("Should return a place with the correct external id")
    void shouldReturnPlaceWithCorrectExternalId() throws Exception {
        final PersistablePlace returned = processor.process(input);
        assertThat(returned.externalId().value())
                .as("externalId")
                .isEqualTo(PLACE_ID);
    }

    @Test
    @DisplayName("Should return a place with the correct ely number")
    void shouldReturnPlaceWithCorrectName() throws Exception {
        final PersistablePlace returned = processor.process(input);
        assertThat(returned.name())
                .as("name")
                .contains(PLACE_NAME);
    }
}
