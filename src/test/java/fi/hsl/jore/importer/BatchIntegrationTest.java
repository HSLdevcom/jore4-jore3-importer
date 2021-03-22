package fi.hsl.jore.importer;

import fi.hsl.jore.importer.config.DatasourceConfig;
import fi.hsl.jore.importer.config.profile.Profiles;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@ComponentScan(basePackages = "fi.hsl.jore.importer.feature")
@ContextConfiguration(classes = DatasourceConfig.class)
@ActiveProfiles(Profiles.TEST_DATABASE)
public class BatchIntegrationTest {

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    protected JobRepositoryTestUtils jobRepositoryTestUtils;
}
