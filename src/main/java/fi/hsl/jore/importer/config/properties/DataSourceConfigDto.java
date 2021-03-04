package fi.hsl.jore.importer.config.properties;

import com.zaxxer.hikari.HikariConfig;
import org.immutables.value.Value;


@Value.Immutable
public interface DataSourceConfigDto {
    String getDriverClassName();

    String getJdbcUrl();

    int getMinimumIdle();

    int getMaximumPoolSize();

    String getUsername();

    String getPassword();

    String poolName();

    default HikariConfig buildHikariConfig() {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(poolName());
        hikariConfig.setDriverClassName(getDriverClassName());
        hikariConfig.setJdbcUrl(getJdbcUrl());
        hikariConfig.setMinimumIdle(getMinimumIdle());
        hikariConfig.setMaximumPoolSize(getMaximumPoolSize());
        hikariConfig.setUsername(getUsername());
        hikariConfig.setPassword(getPassword());

        return hikariConfig;
    }
}
