package fi.hsl.jore.importer.config.properties;

import fi.hsl.jore.importer.config.profile.TestDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@TestDatabase
@PropertySource("classpath:configuration/testdb.properties")
public class TestImporterDataSourceProperties {
    @Value("#{environment['test.importer.db.driver']}")
    private String driverClassName;

    @Value("#{environment['test.importer.db.url']}")
    private String jdbcUrl;

    @Value("#{environment['test.importer.db.min.connections']}")
    private int minimumIdle;

    @Value("#{environment['test.importer.db.max.connections']}")
    private int maximumPoolSize;

    @Value("#{environment['test.importer.db.username']}")
    private String username;

    @Value("#{environment['test.importer.db.password']}")
    private String password;

    public DataSourceConfigDto config() {
        return ImmutableDataSourceConfigDto
                .builder()
                .poolName("test-dst-pool")
                .driverClassName(driverClassName)
                .jdbcUrl(jdbcUrl)
                .minimumIdle(minimumIdle)
                .maximumPoolSize(maximumPoolSize)
                .username(username)
                .password(password)
                .build();
    }
}
