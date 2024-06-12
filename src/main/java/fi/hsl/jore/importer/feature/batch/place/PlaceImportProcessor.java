package fi.hsl.jore.importer.feature.batch.place;

import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.jore3.entity.JrPlace;
import fi.hsl.jore.importer.feature.network.place.dto.PersistablePlace;
import org.springframework.batch.item.ItemProcessor;

public class PlaceImportProcessor implements ItemProcessor<JrPlace, PersistablePlace> {

    @Override
    public PersistablePlace process(final JrPlace item) {
        return PersistablePlace.of(ExternalIdUtil.forPlace(item), item.name());
    }
}
