package fi.hsl.jore.importer.feature.common.repository;

import fi.hsl.jore.importer.feature.common.dto.mixin.IHasSystemTime;
import io.vavr.collection.List;

public interface IHistoryRepository<ENTITY extends IHasSystemTime> {

    /**
     * Returns a list of entities, including old revisions. Oldest entities first.
     *
     * @return List of entities
     */
    List<ENTITY> findFromHistory();

    int countHistory();

    boolean emptyHistory();
}
