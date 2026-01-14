package fi.hsl.jore.importer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Disable JDBC datasource autoconfiguration temporarily
// Disable Flyway autoconfiguration - we manage it manually in FlywayConfig based on jore.importer.migrate property
@SpringBootApplication(
        excludeName = {
            "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
            "org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration",
            "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
        })
public class ImporterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ImporterApplication.class, args);
    }
}
