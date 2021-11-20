package fi.hsl.jore.importer;

import fi.hsl.jore.importer.config.DatasourceConfig;
import fi.hsl.jore.importer.config.DigiroadServiceConfig;
import fi.hsl.jore.importer.config.jackson.VavrModuleConfig;
import fi.hsl.jore.importer.config.jooq.JOOQConfig;
import fi.hsl.jore.importer.config.profile.Profiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@ComponentScan(basePackages = "fi.hsl.jore.importer.feature")
@ContextConfiguration(classes = {
        DatasourceConfig.class,
        JOOQConfig.class,
        VavrModuleConfig.class,
        DigiroadServiceConfig.class
})
@ActiveProfiles(Profiles.TEST_DATABASE)
public class BatchIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(BatchIntegrationTest.class);

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    protected JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    public void overrideJobLauncher() {
        jobLauncherTestUtils.setJobLauncher(createBlockingJobLauncher());
    }

    private JobLauncher createBlockingJobLauncher() {
        // Override the default async JobLauncher with a blocking one for tests
        final SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobLauncherTestUtils.getJobRepository());
        try {
            launcher.afterPropertiesSet();
        } catch (final Exception e) {
            LOG.error("Failed to construct JobLauncher for tests", e);
        }
        return launcher;
    }

    /**
     * The {@link org.springframework.batch.test.JobLauncherTestUtils} does not support starting
     * {@link org.springframework.batch.core.job.flow.Flow flows}, so we must manually run all the steps
     * ourselves.
     *
     * @param steps Names of the steps to run
     */
    protected void runSteps(final Iterable<String> steps) {
        steps.forEach(
                stepName -> {
                    final JobExecution stepExecution = jobLauncherTestUtils.launchStep(stepName);

                    assertThat(String.format("Step %s should complete successfully", stepName),
                               stepExecution.getExitStatus().getExitCode(),
                               is("COMPLETED"));

                }
        );
    }
}
