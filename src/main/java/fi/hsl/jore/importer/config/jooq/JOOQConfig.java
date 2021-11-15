package fi.hsl.jore.importer.config.jooq;

import org.jooq.DSLContext;
import org.jooq.ExecutorProvider;
import org.jooq.RecordListenerProvider;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordUnmapperProvider;
import org.jooq.TransactionListenerProvider;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.jooq.impl.DefaultExecutorProvider;
import org.jooq.impl.DefaultRecordMapperProvider;
import org.jooq.impl.DefaultRecordUnmapperProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.autoconfigure.jooq.JooqProperties;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


/**
 * Most of the configuration is handled in the autoconfiguration
 *
 * @see org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
 */
@Configuration
@EnableConfigurationProperties({JooqProperties.class})
@PropertySource("classpath:configuration/jooq.properties")
public class JOOQConfig {

    @Bean
    public Settings settings() {
        return SettingsTools.defaultSettings()
                            .withReturnAllOnUpdatableRecord(true);
    }

    @Bean
    public DSLContext importerDsl(final @Qualifier("importerDataSource") DataSource dataSource,
                                  final ObjectProvider<ExecutorProvider> executorProvider,
                                  final JooqProperties properties,
                                  final ObjectProvider<RecordListenerProvider> recordListenerProviders,
                                  final ObjectProvider<RecordMapperProvider> recordMapperProvider,
                                  final ObjectProvider<RecordUnmapperProvider> recordUnmapperProvider,
                                  final Settings settings,
                                  final ObjectProvider<TransactionListenerProvider> transactionListenerProviders,
                                  final PlatformTransactionManager txManager,
                                  final ObjectProvider<VisitListenerProvider> visitListenerProviders) {
        return buildAndConfigureDSLContext(dataSource,
                executorProvider,
                properties,
                recordListenerProviders,
                recordMapperProvider,
                recordUnmapperProvider,
                settings,
                transactionListenerProviders,
                txManager,
                visitListenerProviders
        );
    }

    @Bean
    public DSLContext jore4Dsl(final @Qualifier("jore4DataSource") DataSource dataSource,
                               final ObjectProvider<ExecutorProvider> executorProvider,
                               final JooqProperties properties,
                               final ObjectProvider<RecordListenerProvider> recordListenerProviders,
                               final ObjectProvider<RecordMapperProvider> recordMapperProvider,
                               final ObjectProvider<RecordUnmapperProvider> recordUnmapperProvider,
                               final Settings settings,
                               final ObjectProvider<TransactionListenerProvider> transactionListenerProviders,
                               final PlatformTransactionManager txManager,
                               final ObjectProvider<VisitListenerProvider> visitListenerProviders) {
        return buildAndConfigureDSLContext(dataSource,
                executorProvider,
                properties,
                recordListenerProviders,
                recordMapperProvider,
                recordUnmapperProvider,
                settings,
                transactionListenerProviders,
                txManager,
                visitListenerProviders
        );
    }

    private DSLContext buildAndConfigureDSLContext(final DataSource dataSource,
                                                   final ObjectProvider<ExecutorProvider> executorProvider,
                                                   final JooqProperties properties,
                                                   final ObjectProvider<RecordListenerProvider> recordListenerProviders,
                                                   final ObjectProvider<RecordMapperProvider> recordMapperProvider,
                                                   final ObjectProvider<RecordUnmapperProvider> recordUnmapperProvider,
                                                   final Settings settings,
                                                   final ObjectProvider<TransactionListenerProvider> transactionListenerProviders,
                                                   final PlatformTransactionManager txManager,
                                                   final ObjectProvider<VisitListenerProvider> visitListenerProviders) {
        final DefaultConfiguration config = new DefaultConfiguration();

        config.set(properties.determineSqlDialect(dataSource));
        config.set(new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource)));
        config.set(new SpringTransactionProvider(txManager));
        config.set(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()));
        config.setSettings(settings);

        config.set(recordListenerProviders.orderedStream().toArray(RecordListenerProvider[]::new));
        config.setTransactionListenerProvider(transactionListenerProviders.orderedStream().toArray(TransactionListenerProvider[]::new));
        config.set(visitListenerProviders.orderedStream().toArray(VisitListenerProvider[]::new));

        executorProvider.ifAvailable(config::set);
        recordMapperProvider.ifAvailable(config::set);
        recordUnmapperProvider.ifAvailable(config::set);

        return new DefaultDSLContext(config);
    }
}
