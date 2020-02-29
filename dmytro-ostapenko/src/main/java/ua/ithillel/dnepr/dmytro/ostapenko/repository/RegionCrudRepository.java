package ua.ithillel.dnepr.dmytro.ostapenko.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.dmytro.ostapenko.repository.entity.Region;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.DELIMITER;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.HEADER_CITY_ID;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.HEADER_COUNTRY_ID;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.HEADER_NAME;
import static ua.ithillel.dnepr.dmytro.ostapenko.Constants.HEADER_REGION_ID;

@Slf4j
public class RegionCrudRepository implements CrudRepository<Region, Integer> {
    private final String filePath;

    public RegionCrudRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<Region>> findAll() {
        Optional<List<Region>> result = Optional.empty();
        List<Region> regions = new ArrayList<>();
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(DELIMITER)
                    .parse(Files.newBufferedReader(Paths.get(filePath)));

            for (CSVRecord record : records) {
                Region region = new Region();
                region.setRegionId(Integer.parseInt(record.get(HEADER_REGION_ID)));
                region.setCountryId(Integer.parseInt(record.get(HEADER_COUNTRY_ID)));
                region.setCityId(Integer.parseInt(record.get(HEADER_CITY_ID)));
                region.setRegionName(record.get(HEADER_NAME));
                regions.add(region);
            }
            result = Optional.of(regions);
        } catch (IOException e) {
            log.error("RegionCrudRepository findAll CSVRecord Exception", e);
        }
        return result;
    }

    @Override
    public Optional<Region> findById(Integer id) {
        Optional<Region> result = Optional.empty();
        Region region = new Region();
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(DELIMITER)
                    .parse(Files.newBufferedReader(Paths.get(filePath)));
            for (CSVRecord record : records) {
                if (Objects.equals(Integer.parseInt(record.get(HEADER_REGION_ID)), id)) {
                    region.setRegionId(Integer.parseInt(record.get(HEADER_REGION_ID)));
                    region.setCountryId(Integer.parseInt(record.get(HEADER_COUNTRY_ID)));
                    region.setCityId(Integer.parseInt(record.get(HEADER_CITY_ID)));
                    region.setRegionName(record.get(HEADER_NAME));
                    result = Optional.of(region);
                }
            }
        } catch (IOException e) {
            log.error("RegionCrudRepository findById CSVRecord Exception", e);
        }
        return result;
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        Optional<List<Region>> result = Optional.empty();
        List<Region> regions = new ArrayList<>();
        Region region = new Region();
        try (CSVParser records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(Files.newBufferedReader(Paths.get(filePath)))) {
            List<String> headers = records.getHeaderNames();
            if (headers.contains(fieldName)) {
                for (CSVRecord record : records.getRecords()) {
                    if (Objects.equals(record.get(fieldName), value)) {
                        region.setRegionId(Integer.parseInt(record.get(HEADER_REGION_ID)));
                        region.setCountryId(Integer.parseInt(record.get(HEADER_COUNTRY_ID)));
                        region.setCityId(Integer.parseInt(record.get(HEADER_CITY_ID)));
                        region.setRegionName(record.get(HEADER_NAME));
                        regions.add(region);
                        result = Optional.of(regions);
                    }
                }
            }
        } catch (IOException e) {
            log.error("RegionCrudRepository findByField CSVParser Exception", e);
        }
        return result;
    }

    @Override
    public Region create(Region entity) {
        if (findById(entity.getRegionId()).isEmpty()) {
            List<Region> regionsList = findAll().get();
            regionsList.add(entity);
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(HEADER_REGION_ID, HEADER_COUNTRY_ID, HEADER_CITY_ID, HEADER_NAME)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (Region region : regionsList) {
                    csvPrinter.printRecord(region.getRegionId(), region.getCountryId(), region.getCityId(), region.getRegionName());
                }
            } catch (IOException e) {
                log.error("RegionCrudRepository create Exception", e);
            }
        } else {
            log.error("Region already exist");
        }
        return entity;
    }

    @Override
    public Region update(Region entity) {
        if (findById(entity.getRegionId()).isPresent()) {
            List<Region> regionsList = findAll().get();
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(HEADER_REGION_ID, HEADER_COUNTRY_ID, HEADER_CITY_ID, HEADER_NAME)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (Region region : regionsList) {
                    if (Objects.equals(entity.getRegionId(), region.getRegionId())) {
                        region.setRegionId(entity.getRegionId());
                        region.setCountryId(entity.getCountryId());
                        region.setCityId(entity.getCityId());
                        region.setRegionName(entity.getRegionName());
                    }
                    csvPrinter.printRecord(region.getRegionId(), region.getCountryId(), region.getCityId(), region.getRegionName());
                }
            } catch (IOException e) {
                log.error("RegionCrudRepository update Exception", e);
            }
        } else {
            log.error("Region doesn't exist");
        }
        return entity;
    }

    @Override
    public Region delete(Integer id) {
        if (findById(id).isPresent()) {
            Optional<List<Region>> regionsList = findAll();
            regionsList.get().removeIf(deletedCountry -> Objects.equals(deletedCountry.getRegionId(), id));
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(HEADER_REGION_ID, HEADER_COUNTRY_ID, HEADER_CITY_ID, HEADER_NAME)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (Region region : regionsList.get()) {
                    csvPrinter.printRecord(region.getRegionId(), region.getCountryId(), region.getCityId(), region.getRegionName());
                }
            } catch (IOException e) {
                log.error("RegionCrudRepository delete Exception", e);
            }
        } else {
            log.error("Region doesn't exist");
        }
        return null;
    }
}