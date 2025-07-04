package fi.hsl.jore.importer.feature.batch.stop_place.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.stops.stop_place.dto.Jore3StopPlace;
import fi.hsl.jore.importer.feature.stops.stop_place.dto.generated.StopPlacePK;

public interface IStopPlaceImportRepository
        extends IImportRepository<Jore3StopPlace, StopPlacePK> {

    void setJore4Ids();
}
