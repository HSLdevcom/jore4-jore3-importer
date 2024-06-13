package fi.hsl.jore.importer.feature.digiroad.service;

import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

public class CsvDigiroadStopService implements DigiroadStopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDigiroadStopService.class);

    private final Resource csvResource;

    private final HashMap<Long, DigiroadStop> digiroadStops = new HashMap<>();

    public CsvDigiroadStopService(final Resource csvResource) {
        this.csvResource = csvResource;
    }

    @Override
    public Optional<DigiroadStop> findByNationalId(final long nationalId) {
        final DigiroadStop stop = digiroadStops.get(nationalId);
        return Optional.ofNullable(stop);
    }

    @PostConstruct
    public void readStopsFromCsvFile() throws Exception {
        if (!csvResource.exists()) {
            LOGGER.error(
                    "Cannot read Digiroad stops from the location: {} because the CSV file doesn't exist",
                    csvResource.getFilename());
            return;
        }

        try (final BufferedReader reader = new BufferedReader(getReader())) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    final Optional<DigiroadStop> stopContainer = DigiroadStopFactory.fromCsvLine(line);

                    if (stopContainer.isPresent()) {
                        final DigiroadStop stop = stopContainer.get();
                        digiroadStops.put(stop.nationalId(), stop);
                    } else {
                        LOGGER.error("Cannot parse the information of a Digiroad stop from the line: {}", line);
                    }
                } catch (final Exception ex) {
                    LOGGER.error(
                            "Cannot parse the information of a Digiroad stop from the line: {} because of an error: {}",
                            line,
                            ex.getMessage());
                }
            }
        }
    }

    private Reader getReader() throws IOException {
        if (csvResource.isFile()) {
            return new FileReader(csvResource.getFile());
        } else {
            return new InputStreamReader(csvResource.getURL().openStream());
        }
    }
}
