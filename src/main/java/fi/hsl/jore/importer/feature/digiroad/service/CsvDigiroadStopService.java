package fi.hsl.jore.importer.feature.digiroad.service;

import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;

public class CsvDigiroadStopService implements DigiroadStopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDigiroadStopService.class);

    private final Resource csvResource;

    //This cannot be final because the io.vavr.collection.HashMap is immutable
    //and .put() method returns a new map which contains the added value.
    private Map<Integer, DigiroadStop> digiroadStops;

    public CsvDigiroadStopService(final Resource csvResource) {
        this.csvResource = csvResource;
        this.digiroadStops = HashMap.empty();
    }

    @Override
    public Optional<DigiroadStop> findByNationalId(final int nationalId) {
        final DigiroadStop stop = digiroadStops.get(nationalId).getOrNull();
        return Optional.ofNullable(stop);
    }

    @PostConstruct
    public void readStopsFromCsvFile() throws Exception {
        if (!csvResource.exists()) {
            LOGGER.error(
                    "Cannot read Digiroad stops from the CSV File: {} because it doesn't exist",
                    csvResource.getFilename()
            );
            return;
        }

        try (final BufferedReader reader = new BufferedReader(new FileReader(csvResource.getFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    final Optional<DigiroadStop> stopContainer = DigiroadStopFactory.fromCsvLine(line);

                    if (stopContainer.isPresent()) {
                        final DigiroadStop stop = stopContainer.get();
                        digiroadStops = digiroadStops.put(stop.nationalId(), stop);
                    }
                    else {
                        LOGGER.error("Cannot parse the information of a Digiroad stop from the line: {}", line);
                    }
                }
                catch (final Exception ex) {
                    LOGGER.error("Cannot parse the information of a Digiroad stop from the line: {} because of an error: {}",
                            line,
                            ex.getMessage()
                    );
                }
            }
        }
    }
}
