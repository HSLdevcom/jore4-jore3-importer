package fi.hsl.jore.importer.config;

import com.zaxxer.hikari.HikariDataSource;
import fi.hsl.jore.importer.config.profile.StandardDatabase;
import fi.hsl.jore.importer.config.profile.TestDatabase;
import fi.hsl.jore.importer.config.properties.DataSourceConfigDto;
import fi.hsl.jore.importer.config.properties.ImporterDataSourceProperties;
import fi.hsl.jore.importer.config.properties.SourceDataSourceProperties;
import fi.hsl.jore.importer.config.properties.TestImporterDataSourceProperties;
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
                    ImporterDataSourceProperties.class
            })
    public static class StandardDatabaseConfiguration {
        @Resource
        private SourceDataSourceProperties sourceDataSourceProperties;

        @Resource
        private ImporterDataSourceProperties importerDataSourceProperties;

        @Bean
        @Qualifier("sourceDataSourceConfig")
        public DataSourceConfigDto sourceDataSourceConfig() {
            return sourceDataSourceProperties.config();
        }

        @Bean
        @Qualifier("importerDataSourceConfig")
        public DataSourceConfigDto importerDataSourceConfig() {
            return importerDataSourceProperties.config();
        }
    }

    @Configuration
    @TestDatabase
    @Import({
                    TestSourceDataSourceProperties.class,
                    TestImporterDataSourceProperties.class
            })
    public static class TestDatabaseConfiguration {
        @Resource
        private TestSourceDataSourceProperties testSourceDataSourceProperties;

        @Resource
        private TestImporterDataSourceProperties testImporterDataSourceProperties;

        @Bean
        @Qualifier("sourceDataSourceConfig")
        public DataSourceConfigDto sourceDataSourceConfig() {
            return testSourceDataSourceProperties.config();
        }

        @Bean
        @Qualifier("importerDataSourceConfig")
        public DataSourceConfigDto importerDataSourceConfig() {
            return testImporterDataSourceProperties.config();
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
    @Qualifier("importerDataSource")
    public HikariDataSource importerDataSource(@Qualifier("importerDataSourceConfig") final DataSourceConfigDto dataSourceConfigDto) {
        return new HikariDataSource(dataSourceConfigDto.buildHikariConfig());
    }
}
