package fi.hsl.jore.importer.feature.digiroad.service;

import fi.hsl.jore.importer.feature.digiroad.entity.DigiroadStop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CsvDigiroadStopService implements DigiroadStopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDigiroadStopService.class);

    private final Resource csvResource;
    private final Map<String, DigiroadStop> digiroadStops;

    @Autowired
    public CsvDigiroadStopService(@Value("digiroad.stop.csv.file.path") final Resource csvResource) {
        this.csvResource = csvResource;
        this.digiroadStops = new HashMap<>();
    }

    @Override
    public Optional<DigiroadStop> findByElyNumber(String elyNumber) {
        DigiroadStop stop = digiroadStops.get(elyNumber);
        if (stop == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(stop);
        }
    }

    @PostConstruct
    void readStopsFromCsvFile() throws Exception {
        if (!csvResource.exists()) {
            LOGGER.error(
                    "Cannot read Digiroad stops from the CSV File: {} because it doesn't exist",
                    csvResource.getFilename()
            );
            return;
        }


        try (BufferedReader reader = new BufferedReader(new FileReader(csvResource.getFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Optional<DigiroadStop> stopContainer = DigiroadStopFactory.fromCsvLine(line);

                if (stopContainer.isPresent()) {
                    DigiroadStop stop = stopContainer.get();
                    digiroadStops.put(stop.elyNumber(), stop);
                }
                else {
                    LOGGER.error("Cannot parse the information of a Digiroad stop from the line: {}", line);
                }
            }
        }
    }
}
