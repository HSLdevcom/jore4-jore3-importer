package fi.hsl.jore.importer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

// Disable JDBC datasource autoconfiguration temporarily
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, R2dbcAutoConfiguration.class})
public class ImporterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ImporterApplication.class, args);
    }
}
