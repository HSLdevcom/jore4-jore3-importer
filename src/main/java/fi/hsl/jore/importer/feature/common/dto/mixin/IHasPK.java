package fi.hsl.jore.importer.feature.common.dto.mixin;

import fi.hsl.jore.importer.feature.common.dto.field.PK;

public interface IHasPK<ID extends PK> {
    ID pk();
}
