package fi.hsl.jore.importer.config.migration;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flyway configuration for database migrations.
 *
 * <p>This configuration honors the jore.importer.migrate property to control whether migrations should run. When
 * jore.importer.migrate=true, Flyway will run migrations on the importer database. When jore.importer.migrate=false
 * (default), migrations are blocked.
 *
 * <p>This works by conditionally creating a Flyway bean only when migrations are enabled.
 */
@Configuration
public class FlywayConfig {

    private static final Logger LOG = LoggerFactory.getLogger(FlywayConfig.class);

    /**
     * Creates and configures a Flyway instance for the importer database. This bean is only created when
     * jore.importer.migrate=true.
     *
     * @param importerDataSource The importer database data source
     * @param flywaySchemas The schemas to manage (from flyway.schemas property)
     * @return Configured Flyway instance
     */
    @Bean(initMethod = "migrate")
    @ConditionalOnProperty(name = "jore.importer.migrate", havingValue = "true", matchIfMissing = false)
    public Flyway flyway(
            @Qualifier("importerDataSource") final DataSource importerDataSource,
            @Value("${flyway.schemas:infrastructure_network}") final String flywaySchemas) {
        LOG.info("jore.importer.migrate=true - Flyway migrations will run on importer database");

        return Flyway.configure()
                .dataSource(importerDataSource)
                .schemas(flywaySchemas.split(","))
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }

    public FlywayConfig() {
        LOG.debug("FlywayConfig initialized - migrations controlled by jore.importer.migrate property");
    }
}
