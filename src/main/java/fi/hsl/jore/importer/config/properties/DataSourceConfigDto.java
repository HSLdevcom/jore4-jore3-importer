package fi.hsl.jore.importer.config.properties;

import com.zaxxer.hikari.HikariConfig;
import org.immutables.value.Value;

@Value.Immutable
public interface DataSourceConfigDto {
    String driverClassName();

    String jdbcUrl();

    int minimumIdle();

    int maximumPoolSize();

    String username();

    String password();

    String poolName();

    default HikariConfig buildHikariConfig() {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(poolName());
        hikariConfig.setDriverClassName(driverClassName());
        hikariConfig.setJdbcUrl(jdbcUrl());
        hikariConfig.setMinimumIdle(minimumIdle());
        hikariConfig.setMaximumPoolSize(maximumPoolSize());
        hikariConfig.setUsername(username());
        hikariConfig.setPassword(password());

        // 0 = "A value of zero will not prevent the pool from starting in the case that a
        // connection
        // cannot be obtained."
        hikariConfig.setInitializationFailTimeout(0);

        return hikariConfig;
    }
}
