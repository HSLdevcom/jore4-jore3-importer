package fi.hsl.jore.importer.config.properties;

import fi.hsl.jore.importer.config.profile.StandardDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@StandardDatabase
@PropertySource("classpath:configuration/db.properties")
public class SourceDataSourceProperties {
    @Value("#{environment['source.db.driver']}")
    private String driverClassName;

    @Value("#{environment['source.db.url']}")
    private String jdbcUrl;

    @Value("#{environment['source.db.min.connections']}")
    private int minimumIdle;

    @Value("#{environment['source.db.max.connections']}")
    private int maximumPoolSize;

    @Value("#{environment['source.db.username']}")
    private String username;

    @Value("#{environment['source.db.password']}")
    private String password;

    public DataSourceConfigDto config() {
        return ImmutableDataSourceConfigDto
                .builder()
                .poolName("src-pool")
                .driverClassName(driverClassName)
                .jdbcUrl(jdbcUrl)
                .minimumIdle(minimumIdle)
                .maximumPoolSize(maximumPoolSize)
                .username(username)
                .password(password)
                .build();
    }
}
