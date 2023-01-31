package fi.hsl.jore.importer;

import fi.hsl.jore.importer.config.profile.Profiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles(Profiles.TEST_DATABASE)
@Sql(scripts = "/sql/importer/drop_tables.sql")
public abstract class IntegrationTest {
}
