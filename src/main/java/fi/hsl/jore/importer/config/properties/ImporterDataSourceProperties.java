package fi.hsl.jore.importer.config.properties;

import fi.hsl.jore.importer.config.profile.StandardDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@StandardDatabase
@PropertySource("classpath:configuration/db.properties")
public class ImporterDataSourceProperties {
    @Value("#{environment['importer.db.driver']}")
    private String driverClassName;

    @Value("#{environment['importer.db.url']}")
    private String jdbcUrl;

    @Value("#{environment['importer.db.min.connections']}")
    private int minimumIdle;

    @Value("#{environment['importer.db.max.connections']}")
    private int maximumPoolSize;

    @Value("#{environment['importer.db.username']}")
    private String username;

    @Value("#{environment['importer.db.password']}")
    private String password;

    public DataSourceConfigDto config() {
        return ImmutableDataSourceConfigDto
                .builder()
                .poolName("imp-pool")
                .driverClassName(driverClassName)
                .jdbcUrl(jdbcUrl)
                .minimumIdle(minimumIdle)
                .maximumPoolSize(maximumPoolSize)
                .username(username)
                .password(password)
                .build();
    }
}
