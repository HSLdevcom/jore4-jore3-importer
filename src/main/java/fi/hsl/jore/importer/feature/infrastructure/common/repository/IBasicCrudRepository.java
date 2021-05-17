package fi.hsl.jore.importer.feature.infrastructure.common.repository;

import fi.hsl.jore.importer.feature.common.dto.field.PK;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import io.vavr.collection.List;
import io.vavr.collection.Set;

import java.util.Optional;

public interface IBasicCrudRepository<ID extends PK, ENTITY extends IHasPK<ID>> {
    List<ENTITY> findAll();

    Set<ID> findAllIds();

    Optional<ENTITY> findById(ID id);

    Optional<ENTITY> findByExternalId(ExternalId id);

    int count();

    boolean empty();
}
