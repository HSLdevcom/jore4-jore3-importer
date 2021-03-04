package fi.hsl.jore.importer;

import fi.hsl.jore.importer.config.profile.Profiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(Profiles.TEST_DATABASE)
public abstract class IntegrationTest {
}
