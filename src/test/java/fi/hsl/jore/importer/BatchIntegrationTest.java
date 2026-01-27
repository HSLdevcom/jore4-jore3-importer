package fi.hsl.jore.importer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.config.DatasourceConfig;
import fi.hsl.jore.importer.config.DigiroadServiceConfig;
import fi.hsl.jore.importer.config.MapMatchingConfig;
import fi.hsl.jore.importer.config.jobs.BatchConfig;
import fi.hsl.jore.importer.config.jooq.JOOQConfig;
import fi.hsl.jore.importer.config.profile.Profiles;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.test.JobOperatorTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBatchTest
@ComponentScan(basePackages = "fi.hsl.jore.importer.feature")
@ContextConfiguration(
        classes = {
            DatasourceConfig.class,
            JOOQConfig.class,
            DigiroadServiceConfig.class,
            MapMatchingConfig.class,
            BatchConfig.class
        })
@ActiveProfiles(Profiles.TEST_DATABASE)
public class BatchIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(BatchIntegrationTest.class);

    @Autowired
    protected JobOperatorTestUtils jobOperatorTestUtils;

    @Autowired
    protected JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    protected MapJobRegistry mapJobRegistry;

    @BeforeEach
    public void overrideJobLauncher() {
        jobOperatorTestUtils.setJobOperator(createBlockingJobOperator());
    }

    private JobOperator createBlockingJobOperator() {
        // Override the default async JobOperator with a blocking one for tests
        final TaskExecutorJobOperator operator = new TaskExecutorJobOperator();
        operator.setJobRepository(jobOperatorTestUtils.getJobRepository());
        operator.setJobRegistry(mapJobRegistry);
        operator.setTaskExecutor(new SyncTaskExecutor());

        try {
            operator.afterPropertiesSet();
        } catch (final Exception e) {
            LOG.error("Failed to construct JobOperator for tests", e);
        }
        return operator;
    }

    /**
     * The {@link org.springframework.batch.test.JobOperatorTestUtils} does not support starting
     * {@link org.springframework.batch.core.job.flow.Flow flows}, so we must manually run all the steps ourselves.
     *
     * @param steps Names of the steps to run
     */
    protected void runSteps(final Iterable<String> steps) {
        steps.forEach(stepName -> {
            final JobExecution stepExecution = jobOperatorTestUtils.startStep(stepName);

            assertThat(
                    String.format("Step %s should complete successfully", stepName),
                    stepExecution.getExitStatus().getExitCode(),
                    is("COMPLETED"));
        });
    }
}
