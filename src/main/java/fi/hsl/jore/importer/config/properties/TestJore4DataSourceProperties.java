package fi.hsl.jore.importer.config.properties;

import fi.hsl.jore.importer.config.profile.TestDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@TestDatabase
@PropertySource("classpath:configuration/testdb.properties")
public class TestJore4DataSourceProperties {
    @Value("#{environment['test.jore4.db.driver']}")
    private String driverClassName;

    @Value("#{environment['test.jore4.db.url']}")
    private String jdbcUrl;

    @Value("#{environment['test.jore4.db.min.connections']}")
    private int minimumIdle;

    @Value("#{environment['test.jore4.db.max.connections']}")
    private int maximumPoolSize;

    @Value("#{environment['test.jore4.db.username']}")
    private String username;

    @Value("#{environment['test.jore4.db.password']}")
    private String password;

    public DataSourceConfigDto config() {
        return ImmutableDataSourceConfigDto
                .builder()
                .poolName("test-jore4-pool")
                .driverClassName(driverClassName)
                .jdbcUrl(jdbcUrl)
                .minimumIdle(minimumIdle)
                .maximumPoolSize(maximumPoolSize)
                .username(username)
                .password(password)
                .build();
    }
}
