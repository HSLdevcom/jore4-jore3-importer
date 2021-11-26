package fi.hsl.jore.importer.feature.batch.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Provides utility methods which can be used for implementing
 * the {@code RowMapper} interface.
 */
@Component
public class JdbcRowMapperUtil {

    private final ObjectMapper objectMapper;

    @Autowired
    public JdbcRowMapperUtil(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Parses multilingual string from json string.
     * @param json  The parsed value.
     * @return  The created {@link MultilingualString} object.
     * @throws RuntimeException If the multilingual string could
     *                          not be parsed for some reason.
     */
    public MultilingualString multilingualStringFromJsonString(final String json) {
        try {
            final HashMap<String, String> names = objectMapper.readValue(json, HashMap.class);
            return MultilingualString.of(names);
        }
        catch (final JsonProcessingException e) {
            throw new RuntimeException("Could not parse multilingual string because of an error", e);
        }
    }
}
