package fi.hsl.jore.importer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// Disable JDBC datasource autoconfiguration temporarily
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ImporterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ImporterApplication.class, args);
    }

}
