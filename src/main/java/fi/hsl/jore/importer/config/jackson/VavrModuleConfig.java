package fi.hsl.jore.importer.config.jackson;

import io.vavr.jackson.datatype.VavrModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VavrModuleConfig {
    @Bean
    public com.fasterxml.jackson.databind.Module vavrModule() {
        return new VavrModule();
    }
}
