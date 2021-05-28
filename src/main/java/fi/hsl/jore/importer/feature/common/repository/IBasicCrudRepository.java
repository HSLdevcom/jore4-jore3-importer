package fi.hsl.jore.importer.feature.common.repository;

import fi.hsl.jore.importer.feature.common.dto.field.PK;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import io.vavr.collection.List;
import io.vavr.collection.Set;

import java.util.Optional;

public interface IBasicCrudRepository<ID extends PK, ENTITY extends IHasPK<ID>, PERSISTABLE> {
    List<ENTITY> findAll();

    Set<ID> findAllIds();

    Optional<ENTITY> findById(ID id);

    Optional<ENTITY> findByExternalId(ExternalId id);

    int count();

    boolean empty();

    ID insert(PERSISTABLE entity);

    List<ID> insert(List<PERSISTABLE> entities);

    @SuppressWarnings("unchecked")
    List<ID> insert(PERSISTABLE... entities);

    ID update(ENTITY entity);

    List<ID> update(List<ENTITY> entities);

    @SuppressWarnings("unchecked")
    List<ID> update(ENTITY... entities);
}
