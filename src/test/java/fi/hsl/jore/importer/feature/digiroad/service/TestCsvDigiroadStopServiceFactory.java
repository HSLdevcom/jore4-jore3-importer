package fi.hsl.jore.importer.feature.digiroad.service;

import fi.hsl.jore.importer.config.DigiroadServiceConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public final class TestCsvDigiroadStopServiceFactory {

    public static CsvDigiroadStopService create() throws Exception {
        final CsvDigiroadStopService service = new CsvDigiroadStopService(DigiroadServiceConfig.CSV_STOP_CLASSPATH_RESOURCE);
        service.readStopsFromCsvFile();
        return service;
    }
}
