package fi.hsl.jore.importer.config.jooq;

import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.ExecutorProvider;
import org.jooq.RecordListenerProvider;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordUnmapperProvider;
import org.jooq.SQLDialect;
import org.jooq.TransactionListenerProvider;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**
 * Most of the configuration is handled in the autoconfiguration
 *
 * @see org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
 */
@Configuration
@PropertySource("classpath:configuration/jooq.properties")
public class JOOQConfig {

    @Bean
    public Settings settings() {
        return SettingsTools.defaultSettings().withReturnAllOnUpdatableRecord(true);
    }

    @Bean
    public DSLContext importerDsl(
            final @Qualifier("importerDataSource") DataSource dataSource,
            final ObjectProvider<ExecutorProvider> executorProvider,
            final ObjectProvider<RecordListenerProvider> recordListenerProviders,
            final ObjectProvider<RecordMapperProvider> recordMapperProvider,
            final ObjectProvider<RecordUnmapperProvider> recordUnmapperProvider,
            final Settings settings,
            final ObjectProvider<TransactionListenerProvider> transactionListenerProviders,
            final ObjectProvider<VisitListenerProvider> visitListenerProviders) {
        return buildAndConfigureDSLContext(
                dataSource,
                executorProvider,
                recordListenerProviders,
                recordMapperProvider,
                recordUnmapperProvider,
                settings,
                transactionListenerProviders,
                visitListenerProviders);
    }

    @Bean
    public DSLContext jore4Dsl(
            final @Qualifier("jore4DataSource") DataSource dataSource,
            final ObjectProvider<ExecutorProvider> executorProvider,
            final ObjectProvider<RecordListenerProvider> recordListenerProviders,
            final ObjectProvider<RecordMapperProvider> recordMapperProvider,
            final ObjectProvider<RecordUnmapperProvider> recordUnmapperProvider,
            final Settings settings,
            final ObjectProvider<TransactionListenerProvider> transactionListenerProviders,
            final ObjectProvider<VisitListenerProvider> visitListenerProviders) {
        return buildAndConfigureDSLContext(
                dataSource,
                executorProvider,
                recordListenerProviders,
                recordMapperProvider,
                recordUnmapperProvider,
                settings,
                transactionListenerProviders,
                visitListenerProviders);
    }

    private DSLContext buildAndConfigureDSLContext(
            final DataSource dataSource,
            final ObjectProvider<ExecutorProvider> executorProvider,
            final ObjectProvider<RecordListenerProvider> recordListenerProviders,
            final ObjectProvider<RecordMapperProvider> recordMapperProvider,
            final ObjectProvider<RecordUnmapperProvider> recordUnmapperProvider,
            final Settings settings,
            final ObjectProvider<TransactionListenerProvider> transactionListenerProviders,
            final ObjectProvider<VisitListenerProvider> visitListenerProviders) {
        final DefaultConfiguration config = new DefaultConfiguration();

        // Use PostgreSQL dialect explicitly (was previously determined from JooqProperties)
        config.set(SQLDialect.POSTGRES);
        config.set(new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource)));
        // Note: SpringTransactionProvider was removed from Spring Boot 4
        // Using DataSourceConnectionProvider with TransactionAwareDataSourceProxy handles transactions
        config.setSettings(settings);

        config.set(recordListenerProviders.orderedStream().toArray(RecordListenerProvider[]::new));
        config.setTransactionListenerProvider(
                transactionListenerProviders.orderedStream().toArray(TransactionListenerProvider[]::new));
        config.set(visitListenerProviders.orderedStream().toArray(VisitListenerProvider[]::new));

        executorProvider.ifAvailable(config::set);
        recordMapperProvider.ifAvailable(config::set);
        recordUnmapperProvider.ifAvailable(config::set);

        return new DefaultDSLContext(config);
    }
}
