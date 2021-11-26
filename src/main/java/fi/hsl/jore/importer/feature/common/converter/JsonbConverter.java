package fi.hsl.jore.importer.feature.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        try {
            return JSONB.jsonb(objectMapper.writeValueAsString(obj));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(final JSONB json, final Class<T> clazz) {
        return fromJson(json.data(), clazz);
    }

    @Override
    public <T> T fromJson(final String json, final Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
