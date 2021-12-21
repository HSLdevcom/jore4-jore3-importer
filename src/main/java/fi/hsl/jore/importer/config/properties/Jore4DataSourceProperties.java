package fi.hsl.jore.importer.config.properties;

import fi.hsl.jore.importer.config.profile.StandardDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@StandardDatabase
@PropertySource("classpath:configuration/db.properties")
public class Jore4DataSourceProperties {
    @Value("#{environment['jore4.db.driver']}")
    private String driverClassName;

    @Value("#{environment['jore4.db.url']}")
    private String jdbcUrl;

    @Value("#{environment['jore4.db.min.connections']}")
    private int minimumIdle;

    @Value("#{environment['jore4.db.max.connections']}")
    private int maximumPoolSize;

    @Value("#{environment['jore4.db.username']}")
    private String username;

    @Value("#{environment['jore4.db.password']}")
    private String password;

    public DataSourceConfigDto config() {
        return ImmutableDataSourceConfigDto
                .builder()
                .poolName("jore4-pool")
                .driverClassName(driverClassName)
                .jdbcUrl(jdbcUrl)
                .minimumIdle(minimumIdle)
                .maximumPoolSize(maximumPoolSize)
                .username(username)
                .password(password)
                .build();
    }
}
