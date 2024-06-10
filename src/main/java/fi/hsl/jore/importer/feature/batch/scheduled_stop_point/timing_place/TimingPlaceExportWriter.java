package fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place;

import fi.hsl.jore.importer.feature.jore4.entity.Jore4TimingPlace;
import fi.hsl.jore.importer.feature.jore4.repository.IJore4TimingPlaceRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writes the timing place objects to the Jore 4 database.
 */
@Component
public class TimingPlaceExportWriter implements ItemWriter<Jore4TimingPlace> {

    private final IJore4TimingPlaceRepository jore4Repository;

    @Autowired
    public TimingPlaceExportWriter(final IJore4TimingPlaceRepository jore4Repository) {
        this.jore4Repository = jore4Repository;
    }

    @Override
    public void write(final Chunk<? extends Jore4TimingPlace> items) throws Exception {
        jore4Repository.insert(items.getItems());
    }
}
