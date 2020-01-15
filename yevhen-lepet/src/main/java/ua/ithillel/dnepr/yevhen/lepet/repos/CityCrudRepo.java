package ua.ithillel.dnepr.yevhen.lepet.repos;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yevhen.lepet.entity.City;
import ua.ithillel.dnepr.yevhen.lepet.entity.Country;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class CityCrudRepo implements CrudRepository<City, Integer> {
    private final String filePath;
    private final char delimiter;
    private final String CITY_ID = "city_id";
    private final String COUNTRY_ID = "country_id";
    private final String REGION_ID = "region_id";
    private final String NAME = "name";

    public CityCrudRepo(String filePath) {
        this.filePath = filePath;
        this.delimiter = ';';
    }

    @Override
    public Optional<List<City>> findAll() {
        Optional<List<City>> result = Optional.empty();
        final List<City> cities = new ArrayList<>();
        try {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                getCity(csvRecord);
                City city = new City();
                cities.add(city);
            }
            result = Optional.of(cities);
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }

    @Override
    public Optional<City> findById(Integer id) {
        Optional<City> result = Optional.empty();
        try {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                if (Integer.parseInt(csvRecord.get(CITY_ID)) == id) {
                    result = Optional.of(getCity(csvRecord));
                }
            }
        } catch (IOException e) {
            log.error("Exception CSV " + e);
        }
        return result;
    }

    private City getCity(CSVRecord csvRecord) {
        City city = new City();
        city.setId(Integer.parseInt(csvRecord.get(CITY_ID)));
        city.setCountry_id(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
        city.setRegion_id(Integer.parseInt(csvRecord.get(REGION_ID)));
        city.setName(csvRecord.get(NAME));
        return city;
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        Optional<List<City>> result = Optional.empty();
        try {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new FileReader(filePath));
            Map<String, Integer> header = csvParser.getHeaderMap();
            List<City> searchCity = new ArrayList<>();
            if (header.get(fieldName) != null) {
                if (City.class.getDeclaredField(fieldName) != null) {
                    for (CSVRecord csvRecord : csvParser.getRecords()) {
                        if (Objects.equals(csvRecord.get(fieldName), value.toString())) {
                            searchCity.add(getCity(csvRecord));
                        }
                    }
                    result = Optional.of(searchCity);
                }
            }
        } catch (Exception e) {
            log.error("Exception CSV " + e);
        }
        return result;
    }

    @Override
    public City create(City entity) {
        if (findById(entity.getId()).isEmpty()) {
            final List<City> cities = findAll().get();
            cities.add(entity);
            try {
                CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                        .withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME)
                        .withDelimiter(delimiter)
                        .withQuoteMode(QuoteMode.ALL));
                for (City city : cities) {
                    csvPrinter.printRecord(city.getId(),
                            city.getCountry_id(),
                            city.getRegion_id(),
                            city.getName());
                    csvPrinter.flush();
                    csvPrinter.close();
                }
                return entity;
            } catch (IOException e) {
                log.error("Exception CSV " + e);
            }
        }
        return null;
    }

    @Override
    public City update(City entity) {
        Optional<List<City>> cities = findAll();
        for (City city : cities.get()) {
            if (city.getId().equals(entity.getId())) {
                city.setCountry_id(entity.getCountry_id());
                city.setRegion_id(entity.getRegion_id());
                city.setName(entity.getName());
                addCSVPrinter((List<City>) city);
                return city;
            }
        }
        return new City();
    }

    private void addCSVPrinter(List<City> cities) {
        try {
            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME)
                    .withDelimiter(delimiter));
            for (City someCity : cities) {
                csvPrinter.printRecord(someCity.getId(),
                        someCity.getCountry_id(),
                        someCity.getRegion_id(),
                        someCity.getName());
                csvPrinter.flush();
                csvPrinter.close();
            }
        } catch (IOException e) {
            log.error("Exception CSV " + e);
        }
    }

    @Override
    public City delete(Integer id) {
        Optional<List<City>> cities = findAll();
        for (City city : cities.get()) {
            if (city.getId().equals(id)) {
                cities.get().remove(city);
                addCSVPrinter((List<City>) city);
                return city;
            }
        }
        return new City();
    }
}
