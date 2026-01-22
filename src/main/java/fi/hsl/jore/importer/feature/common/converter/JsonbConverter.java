package fi.hsl.jore.importer.feature.common.converter;

import tools.jackson.databind.ObjectMapper;
import org.jooq.JSONB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonbConverter implements IJsonbConverter {

    private final ObjectMapper objectMapper;

    @Autowired
    public JsonbConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JSONB asJson(final Object obj) {
        if (obj == null) {
            return null;
        }

        return JSONB.jsonb(objectMapper.writeValueAsString(obj));
    }

    @Override
    public <T> T fromJson(final JSONB json, final Class<T> clazz) {
        return json != null ? fromJson(json.data(), clazz) : null;
    }

    @Override
    public <T> T fromJson(final String json, final Class<T> clazz) {
        return objectMapper.readValue(json, clazz);
    }
}
