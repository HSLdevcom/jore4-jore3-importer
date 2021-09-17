package fi.hsl.jore.importer;

import fi.hsl.jore.importer.config.profile.Profiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

/**
 * A custom annotation which configures integration tests which
 * use the test database.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest
@ActiveProfiles(Profiles.TEST_DATABASE)
public @interface IntTest {
}
