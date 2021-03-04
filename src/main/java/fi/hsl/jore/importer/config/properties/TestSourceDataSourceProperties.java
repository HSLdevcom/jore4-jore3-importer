package fi.hsl.jore.importer.config.properties;

import fi.hsl.jore.importer.config.profile.TestDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@TestDatabase
@PropertySource("classpath:configuration/testdb.properties")
public class TestSourceDataSourceProperties {
    @Value("#{environment['test.source.db.driver']}")
    private String driverClassName;

    @Value("#{environment['test.source.db.url']}")
    private String jdbcUrl;

    @Value("#{environment['test.source.db.min.connections']}")
    private int minimumIdle;

    @Value("#{environment['test.source.db.max.connections']}")
    private int maximumPoolSize;

    @Value("#{environment['test.source.db.username']}")
    private String username;

    @Value("#{environment['test.source.db.password']}")
    private String password;

    public DataSourceConfigDto config() {
        return ImmutableDataSourceConfigDto
                .builder()
                .poolName("test-src-pool")
                .driverClassName(driverClassName)
                .jdbcUrl(jdbcUrl)
                .minimumIdle(minimumIdle)
                .maximumPoolSize(maximumPoolSize)
                .username(username)
                .password(password)
                .build();
    }
}
