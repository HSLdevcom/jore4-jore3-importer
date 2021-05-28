package fi.hsl.jore.importer.feature.common.converter;

import org.jooq.JSONB;

public interface IJsonbConverter {
    JSONB asJson(Object obj);

    <T> T fromJson(JSONB json, Class<T> clazz);
}
