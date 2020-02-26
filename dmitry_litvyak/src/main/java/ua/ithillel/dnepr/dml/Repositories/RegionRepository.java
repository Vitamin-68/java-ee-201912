package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.dml.domain.Region;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class RegionRepository implements CrudRepository<Region, Integer> {

    public static final String REGION_ID = "region_id";
    public static final String COUNTRY_ID = "country_id";
    public static final String CITY_ID = "city_id";
    public static final String NAME = "name";
    private String filePath;
    private char delimiter;

    public RegionRepository(String filePath) {
        this.delimiter = ';';
        this.filePath = filePath;
    }

    public RegionRepository(String filePath, char delim) {
        this.delimiter = delim;
        this.filePath = filePath;
    }

    public RegionRepository(){}

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public Optional<List<Region>> findAll() {
        Optional<List<Region>> result = Optional.empty();
        final List<Region> regions = new ArrayList<>();
        try {
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine : csvSourse.getRecords()) {
                Region region = new Region();
                region.setCity_id(Integer.parseInt(csvLine.get(CITY_ID)));
                region.setId(Integer.parseInt(csvLine.get(REGION_ID)));
                region.setCountry_id(Integer.parseInt(csvLine.get(COUNTRY_ID)));
                region.setName(csvLine.get(NAME));
                regions.add(region);
            }
            if(!regions.isEmpty()) {
                result = Optional.of(regions);
            }
        } catch (Exception e) {

            log.error("CSV reader:", e);
        }
        return result;
    }

    @Override
    public Optional<Region> findById(Integer id) {
        Optional<Region> result = Optional.empty();
        try {
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine : csvSourse.getRecords()) {
                if (Integer.parseInt(csvLine.get(REGION_ID)) == id) {
                    result = Optional.of(getRegion(csvLine));
                }
            }
        } catch (Exception e) {
            log.error("CSV reader:", e);
        }
        return result;
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        Optional<List<Region>> result = Optional.empty();
        try {
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            Map<String, Integer> header = csvSourse.getHeaderMap();
            if (header.get(fieldName) != null) {
                if (Region.class.getDeclaredField(fieldName) != null) {
                    List<Region> resultRegion = new ArrayList<>();
                    for (CSVRecord csvLine : csvSourse.getRecords()) {
                        if (Objects.equals(csvLine.get(fieldName), value.toString())) {
                            resultRegion.add(getRegion(csvLine));
                        }
                    }
                    result = Optional.of(resultRegion);
                }
            }
        } catch (Exception e) {
            log.error("CSV reader:", e);
        }
        return result;
    }

    private Region getRegion(CSVRecord csvLine) {
        Region region = new Region();
        region.setCity_id(Integer.parseInt(csvLine.get(CITY_ID)));
        region.setId(Integer.parseInt(csvLine.get(REGION_ID)));
        region.setCountry_id(Integer.parseInt(csvLine.get(COUNTRY_ID)));
        region.setName(csvLine.get(NAME));
        return region;
    }

    @Override
    public Region create(Region entity) {
        Optional<Region> test = findById(entity.getId());
        if (!test.isEmpty()) return test.get();
        try {
            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(REGION_ID, COUNTRY_ID, CITY_ID, NAME)
                    .withDelimiter(delimiter)
                    .withQuoteMode(QuoteMode.ALL));
            for (Region locRegion : findAll().get()) {
                csvPrinter.printRecord(locRegion.getId(), locRegion.getCountry_id(), locRegion.getCity_id(), locRegion.getName());
            }
            csvPrinter.printRecord(entity.getId(), entity.getCountry_id(), entity.getCity_id(), entity.getName());
            csvPrinter.flush();
            csvPrinter.close();
        } catch (IOException e) {
            log.error("CSV printer:", e);
        }
        return entity;
    }

    @Override
    public Region update(Region entity) {
        Optional<List<Region>> allRecords = findAll();
        for (Region currentRegion : allRecords.get()) {
            if (currentRegion.getId().equals(entity.getId())) {
                currentRegion.setCity_id(entity.getCity_id());
                currentRegion.setCountry_id(entity.getCountry_id());
                currentRegion.setName(entity.getName());
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                            .withHeader(REGION_ID, COUNTRY_ID, CITY_ID, NAME)
                            .withDelimiter(delimiter)
                            .withQuoteMode(QuoteMode.ALL));
                    for (Region locRegion : allRecords.get()) {
                        csvPrinter.printRecord(locRegion.getId(), locRegion.getCountry_id(), locRegion.getCity_id(), locRegion.getName());
                    }
                    csvPrinter.flush();
                    csvPrinter.close();
                } catch (IOException e) {
                    log.error("CSV printer:", e);
                }
                return currentRegion;
            }
        }
        return new Region();
    }

    @Override
    public Region delete(Integer id) {
        Optional<List<Region>> allRecords = findAll();
        for (Region currentRegion : allRecords.get()) {
            if (currentRegion.getId().equals(id)) {
                allRecords.get().remove(currentRegion);
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                            .withHeader(REGION_ID, COUNTRY_ID, CITY_ID, NAME)
                            .withDelimiter(delimiter)
                            .withQuoteMode(QuoteMode.ALL));
                    for (Region tmpRegion : allRecords.get()) {
                        csvPrinter.printRecord(tmpRegion.getId(), tmpRegion.getCountry_id(), tmpRegion.getCity_id(), tmpRegion.getName());
                    }
                    csvPrinter.flush();
                    csvPrinter.close();
                } catch (IOException e) {
                    log.error("CSV printer:", e);
                }
                return new Region();
            }
        }
        return new Region();
    }
}
