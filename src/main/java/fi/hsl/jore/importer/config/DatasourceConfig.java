package fi.hsl.jore.importer.config;

import com.zaxxer.hikari.HikariDataSource;
import fi.hsl.jore.importer.config.profile.StandardDatabase;
import fi.hsl.jore.importer.config.profile.TestDatabase;
import fi.hsl.jore.importer.config.properties.DataSourceConfigDto;
import fi.hsl.jore.importer.config.properties.ImporterDataSourceProperties;
import fi.hsl.jore.importer.config.properties.Jore4DataSourceProperties;
import fi.hsl.jore.importer.config.properties.SourceDataSourceProperties;
import fi.hsl.jore.importer.config.properties.TestImporterDataSourceProperties;
import fi.hsl.jore.importer.config.properties.TestJore4DataSourceProperties;
import fi.hsl.jore.importer.config.properties.TestSourceDataSourceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {

    @Configuration
    @StandardDatabase
    @Import({
                    SourceDataSourceProperties.class,
                    ImporterDataSourceProperties.class,
                    Jore4DataSourceProperties.class
            })
    public static class StandardDatabaseConfiguration {
        @Resource
        private SourceDataSourceProperties sourceDataSourceProperties;

        @Resource
        private ImporterDataSourceProperties importerDataSourceProperties;

        @Resource
        private Jore4DataSourceProperties jore4DataSourceProperties;

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

        @Bean
        @Qualifier("jore4DataSourceConfig")
        public DataSourceConfigDto jore4DataSourceConfig() {
            return jore4DataSourceProperties.config();
        }
    }

    @Configuration
    @TestDatabase
    @Import({
                    TestSourceDataSourceProperties.class,
                    TestImporterDataSourceProperties.class,
                    TestJore4DataSourceProperties.class
            })
    public static class TestDatabaseConfiguration {
        @Resource
        private TestSourceDataSourceProperties testSourceDataSourceProperties;

        @Resource
        private TestImporterDataSourceProperties testImporterDataSourceProperties;

        @Resource
        private TestJore4DataSourceProperties testJore4DataSourceProperties;

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

        @Bean
        @Qualifier("jore4DataSourceConfig")
        public DataSourceConfigDto jore4DataSourceConfig() {
            return testJore4DataSourceProperties.config();
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

    @Bean(destroyMethod = "close")
    @Qualifier("jore4DataSource")
    public HikariDataSource jore4DataSource(@Qualifier("jore4DataSourceConfig") final DataSourceConfigDto dataSourceConfigDto) {
        return new HikariDataSource(dataSourceConfigDto.buildHikariConfig());
    }

    /**
     * This transaction manager ensures that database queries are always
     * executed inside a transaction. This is important because the Jore 4
     * database has several constraints which require that data is inserted
     * into two different tables "at the same time" aka inside the same
     * transaction.
     *
     * Note that this transaction manager has also some drawbacks
     * which are documented quite well:
     *
     * https://github.com/spring-projects/spring-data-commons/issues/2232
     */
    @Bean
    @Qualifier("chainedTransactionManager")
    @Primary
    public PlatformTransactionManager chainedTransactionManager(@Qualifier("sourceDataSource") final DataSource sourceDataSource,
                                                                @Qualifier("importerDataSource") final DataSource importerDataSource,
                                                                @Qualifier("jore4DataSource") final DataSource jore4DataSource) {
        return new ChainedTransactionManager(
                new DataSourceTransactionManager(sourceDataSource),
                new DataSourceTransactionManager(importerDataSource),
                new DataSourceTransactionManager(jore4DataSource)
        );
    }
}
