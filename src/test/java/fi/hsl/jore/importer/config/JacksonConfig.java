package fi.hsl.jore.importer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuration for Jackson 2.x ObjectMapper. Spring Boot 4 defaults to Jackson 3.x (tools.jackson), but we're using
 * Jackson 2.x (com.fasterxml.jackson). This configuration manually creates the ObjectMapper bean that would normally be
 * auto-configured.
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.createXmlMapper(false).build();

        // Register modules for Java 8+ types (Optional, java.time.*, parameter names)
        // These are needed regardless of Java version - they handle specific type serialization
        mapper.registerModule(new Jdk8Module()); // Handles Optional, OptionalInt, etc.
        mapper.registerModule(new JavaTimeModule()); // Handles LocalDate, LocalDateTime, Instant, etc.
        mapper.registerModule(new ParameterNamesModule()); // Allows using parameter names for deserialization

        // Disable writing dates as timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.modules(new Jdk8Module(), new JavaTimeModule(), new ParameterNamesModule());
        return builder;
    }
}
