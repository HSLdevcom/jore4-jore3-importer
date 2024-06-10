package fi.hsl.jore.importer.config.jobs;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan(basePackages = "fi.hsl.jore.importer.feature")
public class BatchConfig extends DefaultBatchConfiguration  {
    private DataSource batchDataSource;
    private DataSourceTransactionManager batchTransactionManager;

    @Override
    protected DataSource getDataSource() {
        if (batchDataSource == null) {
            batchDataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("/org/springframework/batch/core/schema-hsqldb.sql")
                .generateUniqueName(true)
                .build();
        }

        return this.batchDataSource;
    }

    @Override
    public DataSourceTransactionManager getTransactionManager() {
        if (batchTransactionManager == null) {
            batchTransactionManager = new DataSourceTransactionManager(getDataSource());
        }

        return batchTransactionManager;
    }

    @Bean
    @BatchTransactionManager
    public PlatformTransactionManager batchTransactionManager() {
        return getTransactionManager();
    }

    @Bean
    public static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() {
        return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor");
    }
}
