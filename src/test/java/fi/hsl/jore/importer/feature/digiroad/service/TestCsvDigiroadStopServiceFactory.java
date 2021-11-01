package fi.hsl.jore.importer.feature.digiroad.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public final class TestCsvDigiroadStopServiceFactory {

    private static final Resource CSV_CLASSPATH_RESOURCE = new ClassPathResource("csv/digiroad_stops.csv");

    public static CsvDigiroadStopService create() throws Exception {
        final CsvDigiroadStopService service = new CsvDigiroadStopService(CSV_CLASSPATH_RESOURCE);
        service.readStopsFromCsvFile();
        return service;
    }
}
