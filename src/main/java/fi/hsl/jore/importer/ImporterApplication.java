package fi.hsl.jore.importer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.r2dbc.autoconfigure.R2dbcAutoConfiguration;

// Disable JDBC datasource autoconfiguration temporarily
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, R2dbcAutoConfiguration.class})
public class ImporterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ImporterApplication.class, args);
    }
}
