package fi.hsl.jore.importer.feature.batch.place.support;

import fi.hsl.jore.importer.feature.batch.common.IImportRepository;
import fi.hsl.jore.importer.feature.network.place.dto.PersistablePlace;
import fi.hsl.jore.importer.feature.network.place.dto.generated.PlacePK;

public interface IPlaceImportRepository extends IImportRepository<PersistablePlace, PlacePK> {}
