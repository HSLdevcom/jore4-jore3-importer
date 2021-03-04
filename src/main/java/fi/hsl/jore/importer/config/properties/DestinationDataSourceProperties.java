package fi.hsl.jore.importer.config.properties;

import fi.hsl.jore.importer.config.profile.StandardDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@StandardDatabase
@PropertySource("classpath:configuration/db.properties")
public class DestinationDataSourceProperties {
    @Value("#{environment['destination.db.driver']}")
    private String driverClassName;

    @Value("#{environment['destination.db.url']}")
    private String jdbcUrl;

    @Value("#{environment['destination.db.min.connections']}")
    private int minimumIdle;

    @Value("#{environment['destination.db.max.connections']}")
    private int maximumPoolSize;

    @Value("#{environment['destination.db.username']}")
    private String username;

    @Value("#{environment['destination.db.password']}")
    private String password;

    public DataSourceConfigDto config() {
        return ImmutableDataSourceConfigDto
                .builder()
                .poolName("dst-pool")
                .driverClassName(driverClassName)
                .jdbcUrl(jdbcUrl)
                .minimumIdle(minimumIdle)
                .maximumPoolSize(maximumPoolSize)
                .username(username)
                .password(password)
                .build();
    }
}
