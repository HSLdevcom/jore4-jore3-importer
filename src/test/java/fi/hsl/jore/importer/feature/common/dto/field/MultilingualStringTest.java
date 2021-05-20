package fi.hsl.jore.importer.feature.common.dto.field;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.hsl.jore.importer.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class MultilingualStringTest extends IntegrationTest {

    public static final Locale FINNISH = Locale.forLanguageTag("fi-FI");

    public static final Locale SWEDISH = Locale.forLanguageTag("sv-SE");

    private final ObjectMapper objectMapper;

    public MultilingualStringTest(@Autowired final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static Stream<Arguments> strings() {
        return Stream.of(
                arguments(MultilingualString.empty()),
                arguments(MultilingualString.empty()
                                            .with(FINNISH, "Hei")),
                arguments(MultilingualString.empty()
                                            .with(FINNISH, "Hei")
                                            .with(SWEDISH, "Hej"))
        );
    }

    @ParameterizedTest
    @MethodSource("strings")
    public void roundtripTest(final MultilingualString s) throws JsonProcessingException {
        final String asJsonString = objectMapper.writeValueAsString(s);
        final MultilingualString o = objectMapper.readValue(asJsonString, MultilingualString.class);

        assertThat(o,
                   is(s));
    }

    @Test
    public void whenGivenNoTranslations_thenReturnEmptyMap() throws JsonProcessingException {
        final MultilingualString s = MultilingualString.empty();
        final String asJsonString = objectMapper.writeValueAsString(s);
        assertThat(asJsonString,
                   is("{}"));
    }

    @Test
    public void whenGivenTranslations_thenReturnMap() throws JsonProcessingException {
        final MultilingualString s = MultilingualString.empty()
                                                       .with(FINNISH, "Hei")
                                                       .with(SWEDISH, "Hej");
        final String asJsonString = objectMapper.writeValueAsString(s);
        assertThat(asJsonString,
                   is("{\"fi_FI\":\"Hei\",\"sv_SE\":\"Hej\"}"));
    }
}
