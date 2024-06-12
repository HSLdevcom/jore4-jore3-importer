package fi.hsl.jore.importer.feature.network.place.repository;

import com.google.common.annotations.VisibleForTesting;
import fi.hsl.jore.importer.feature.common.repository.IBasicCrudRepository;
import fi.hsl.jore.importer.feature.common.repository.IHistoryRepository;
import fi.hsl.jore.importer.feature.network.place.dto.PersistablePlace;
import fi.hsl.jore.importer.feature.network.place.dto.Place;
import fi.hsl.jore.importer.feature.network.place.dto.generated.PlacePK;

/**
 * Only for testing, use {@link
 * fi.hsl.jore.importer.feature.batch.place.support.IPlaceImportRepository this} to
 * insert/update/delete lines.
 */
@VisibleForTesting
public interface IPlaceTestRepository
        extends IBasicCrudRepository<PlacePK, Place, PersistablePlace>, IHistoryRepository<Place> {}
