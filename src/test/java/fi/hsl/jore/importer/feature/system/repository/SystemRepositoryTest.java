package fi.hsl.jore.importer.feature.system.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import fi.hsl.jore.importer.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemRepositoryTest extends IntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(SystemRepositoryTest.class);

    private final ISystemRepository systemRepository;

    public SystemRepositoryTest(@Autowired final ISystemRepository systemRepository) {
        this.systemRepository = systemRepository;
    }

    @Test
    public void testCurrentTimestamp() {
        assertDoesNotThrow(() -> {
            LOG.info("Current timestamp is {}", systemRepository.currentTimestamp());
        });
    }

    @Test
    public void testCurrentDate() {
        assertDoesNotThrow(() -> {
            LOG.info("Current date is {}", systemRepository.currentDate());
        });
    }

    @Test
    public void testCurrentTime() {
        assertDoesNotThrow(() -> {
            LOG.info("Current time is {}", systemRepository.currentTime());
        });
    }
}
