package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.dml.domain.City;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//"city_id","country_id","region_id","name"
@Slf4j
public class CityRepository implements CrudRepository<City, Integer> {

    public static final String REGION_ID = "region_id";
    public static final String CITY_ID = "city_id";
    public static final String COUNTRY_ID = "country_id";
    public static final String NAME = "name";
    private final String filePath;
    private final char delimiter;

    public CityRepository(String filePath) {
        this.filePath = filePath;
        delimiter = ';';
    }

    public CityRepository(String filePath, char delim) {
        this.filePath = filePath;
        delimiter = delim;
    }

    @Override
    public Optional<List<City>> findAll() {
        Optional<List<City>> result = Optional.empty();

        final List<City> regions = new ArrayList<>();
        try {
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine : csvSourse.getRecords()) {
                City region = new City();
                region.setRegion_id(Integer.parseInt(csvLine.get(REGION_ID)));
                region.setId(Integer.parseInt(csvLine.get(CITY_ID)));
                region.setCountry_id(Integer.parseInt(csvLine.get(COUNTRY_ID)));
                region.setName(csvLine.get(NAME));
                regions.add(region);
            }
            result = Optional.of(regions);
        } catch (Exception e) {

            log.error("CSV reader:", e);
        }
        return result;
    }

    @Override
    public Optional<City> findById(Integer id) {
        Optional<City> result = Optional.empty();
        try {
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine : csvSourse.getRecords()) {
                if (Integer.parseInt(csvLine.get(CITY_ID)) == id) {
                    result = Optional.of(getCity(csvLine));
                }
            }

        } catch (Exception e) {

            log.error("CSV reader:", e);
        }
        return result;
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        Optional<List<City>> result = Optional.empty();
        try {
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            Map<String, Integer> header = csvSourse.getHeaderMap();
            List<City> cityList = new ArrayList<>();
            if (header.get(fieldName) != null) {
                if (City.class.getDeclaredField(fieldName) != null) {
                    for (CSVRecord csvLine : csvSourse.getRecords()) {
                        if (Objects.equals(csvLine.get(fieldName), value.toString())) {
                            City resultCity = getCity(csvLine);
                            cityList.add(resultCity);
                        }
                    }
                    result = Optional.of(cityList);
                }
            }
        } catch (Exception e) {
            log.error("CSV reader:", e);
        }
        return result;
    }

    @Override
    public City create(City entity) {
        Optional<City> test = findById(entity.getId());
        if (!test.isEmpty()) return test.get();
        try {
            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT.withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME).withDelimiter(delimiter));
            csvPrinter.printRecord(entity.getId(), entity.getCountry_id(), entity.getRegion_id(), entity.getName());
            csvPrinter.flush();
            csvPrinter.close();
        } catch (IOException e) {
            log.error("CSV printer:", e);
        }
        return entity;
    }

    @Override
    public City update(City entity) {
        Optional<List<City>> allRecords = findAll();
        for (City currentRegion : allRecords.get()) {
            if (currentRegion.getId().equals(entity.getId())) {
                currentRegion.setRegion_id(entity.getRegion_id());
                currentRegion.setCountry_id(entity.getCountry_id());
                currentRegion.setName(entity.getName());
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT.withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME).withDelimiter(delimiter));
                    for (City _region : allRecords.get()) {
                        csvPrinter.printRecord(_region.getId(), _region.getCountry_id(), _region.getRegion_id(), _region.getName());
                    }
                    csvPrinter.flush();
                    csvPrinter.close();
                } catch (IOException e) {
                    log.error("CSV printer:", e);
                }
                return currentRegion;
            }
        }
        return new City();
    }

    @Override
    public City delete(Integer id) {
        Optional<List<City>> allRecords = findAll();
        for (City currentRegion : allRecords.get()) {
            if (currentRegion.getId().equals(id)) {
                allRecords.get().remove(currentRegion);
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT.withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME).withDelimiter(delimiter));
                    for (City locCity : allRecords.get()) {
                        csvPrinter.printRecord(locCity.getId(), locCity.getCountry_id(), locCity.getRegion_id(), locCity.getName());
                    }
                    csvPrinter.flush();
                    csvPrinter.close();
                } catch (IOException e) {
                    log.error("CSV printer:", e);
                }
                return new City();
            }
        }
        return new City();
    }

    private City getCity(CSVRecord csvLine) {
        City region = new City();
        region.setRegion_id(Integer.parseInt(csvLine.get(REGION_ID)));
        region.setId(Integer.parseInt(csvLine.get(CITY_ID)));
        region.setCountry_id(Integer.parseInt(csvLine.get(COUNTRY_ID)));
        region.setName(csvLine.get(NAME));
        return region;
    }
}
