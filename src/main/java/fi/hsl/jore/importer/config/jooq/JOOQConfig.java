package fi.hsl.jore.importer.config.jooq;

import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


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
        return SettingsTools.defaultSettings()
                            .withReturnAllOnUpdatableRecord(true);
    }
}
