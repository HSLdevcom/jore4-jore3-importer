package fi.hsl.jore.importer.feature.common.dto;

public interface HasPK<ID extends PK> {
    ID pk();
}
