package fi.hsl.jore.importer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.hsl.jore.importer.config.profile.StandardDatabase;
import fi.hsl.jore.importer.config.profile.TestDatabase;
import fi.hsl.jore.importer.feature.mapmatching.service.IMapMatchingService;
import fi.hsl.jore.importer.feature.mapmatching.service.MapMatchingService;
import fi.hsl.jore.importer.feature.mapmatching.service.MockMapMatchingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MapMatchingConfig {

    @Configuration
    @StandardDatabase
    public static class StandardDatabaseConfiguration {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public IMapMatchingService mapMatchingService(@Value("#{environment['map.matching.api.baseUrl']}") final String mapMatchingApiUrl,
                                                      final ObjectMapper objectMapper,
                                                      final RestTemplate restTemplate) {
            return new MapMatchingService(mapMatchingApiUrl, objectMapper, restTemplate);
        }
    }

    @Configuration
    @TestDatabase
    public static class TestDatabaseConfiguration {

        @Bean
        public IMapMatchingService mapMatchingService() {
            return new MockMapMatchingService();
        }
    }
}

