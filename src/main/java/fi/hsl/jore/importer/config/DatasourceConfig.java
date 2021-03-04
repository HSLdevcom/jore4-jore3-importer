package fi.hsl.jore.importer.config;

import com.zaxxer.hikari.HikariDataSource;
import fi.hsl.jore.importer.config.profile.StandardDatabase;
import fi.hsl.jore.importer.config.profile.TestDatabase;
import fi.hsl.jore.importer.config.properties.DataSourceConfigDto;
import fi.hsl.jore.importer.config.properties.DestinationDataSourceProperties;
import fi.hsl.jore.importer.config.properties.SourceDataSourceProperties;
import fi.hsl.jore.importer.config.properties.TestDestinationDataSourceProperties;
import fi.hsl.jore.importer.config.properties.TestSourceDataSourceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

@Configuration
public class DatasourceConfig {

    @Configuration
    @StandardDatabase
    @Import({
                    SourceDataSourceProperties.class,
                    DestinationDataSourceProperties.class
            })
    public static class StandardDatabaseConfiguration {
        @Resource
        private SourceDataSourceProperties sourceDataSourceProperties;

        @Resource
        private DestinationDataSourceProperties destinationDataSourceProperties;

        @Bean
        @Qualifier("sourceDataSourceConfig")
        public DataSourceConfigDto sourceDataSourceConfig() {
            return sourceDataSourceProperties.config();
        }

        @Bean
        @Qualifier("destinationDataSourceConfig")
        public DataSourceConfigDto destinationDataSourceConfig() {
            return destinationDataSourceProperties.config();
        }
    }

    @Configuration
    @TestDatabase
    @Import({
                    TestSourceDataSourceProperties.class,
                    TestDestinationDataSourceProperties.class
            })
    public static class TestDatabaseConfiguration {
        @Resource
        private TestSourceDataSourceProperties testSourceDataSourceProperties;

        @Resource
        private TestDestinationDataSourceProperties testDestinationDataSourceProperties;

        @Bean
        @Qualifier("sourceDataSourceConfig")
        public DataSourceConfigDto sourceDataSourceConfig() {
            return testSourceDataSourceProperties.config();
        }

        @Bean
        @Qualifier("destinationDataSourceConfig")
        public DataSourceConfigDto destinationDataSourceConfig() {
            return testDestinationDataSourceProperties.config();
        }
    }

    // The sourceDataSource is a plain Hikari connection pool
    @Bean(destroyMethod = "close")
    @Qualifier("sourceDataSource")
    public HikariDataSource sourceDataSource(@Qualifier("sourceDataSourceConfig") final DataSourceConfigDto dataSourceConfigDto) {
        return new HikariDataSource(dataSourceConfigDto.buildHikariConfig());
    }

    @Bean(destroyMethod = "close")
    @Primary
    @Qualifier("destinationDataSource")
    public HikariDataSource destinationDataSource(@Qualifier("destinationDataSourceConfig") final DataSourceConfigDto dataSourceConfigDto) {
        return new HikariDataSource(dataSourceConfigDto.buildHikariConfig());
    }
}
