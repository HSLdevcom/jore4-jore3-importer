package fi.hsl.jore.importer.config.jobs;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;

@ComponentScan(basePackages = "fi.hsl.jore.importer.feature")
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

    @Autowired
    protected JobBuilderFactory jobs;

    @Autowired
    protected StepBuilderFactory steps;

    @Override
    public void setDataSource(final DataSource dataSource) {
        // Do not assign a datasource to spring-batch even if one is available
        // -> batch information is stored in memory using MapJobRepositoryFactoryBean
    }
}
