package fi.hsl.jore.importer.config;

import fi.hsl.jore.importer.config.profile.StandardDatabase;
import fi.hsl.jore.importer.config.profile.TestDatabase;
import fi.hsl.jore.importer.feature.digiroad.service.CsvDigiroadStopService;
import fi.hsl.jore.importer.feature.digiroad.service.DigiroadStopService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
public class DigiroadServiceConfig {

    public static final Resource CSV_STOP_CLASSPATH_RESOURCE = new ClassPathResource("csv/digiroad_stops.csv");

    @Configuration
    @StandardDatabase
    public static class StandardDatabaseConfiguration {

        @Bean
        public DigiroadStopService digiroadStopService(final Environment environment) {
            final Resource csvResource = new FileSystemResource(environment.getRequiredProperty("digiroad.stop.csv.file.path"));
            return new CsvDigiroadStopService(csvResource);
        }
    }

    @Configuration
    @TestDatabase
    public static class TestDatabaseConfiguration {

        @Bean
        public DigiroadStopService digiroadStopService() throws Exception {
            final CsvDigiroadStopService service = new CsvDigiroadStopService(DigiroadServiceConfig.CSV_STOP_CLASSPATH_RESOURCE);
            service.readStopsFromCsvFile();
            return service;
        }
    }
}
