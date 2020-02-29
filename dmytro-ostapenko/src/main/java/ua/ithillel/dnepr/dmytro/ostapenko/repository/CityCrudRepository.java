package ua.ithillel.dnepr.dmytro.ostapenko.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.dmytro.ostapenko.repository.entity.City;

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
public class CityCrudRepository implements CrudRepository<City, Integer> {
    private final String filePath;

    public CityCrudRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<City>> findAll() {
        Optional<List<City>> result = Optional.empty();
        List<City> cities = new ArrayList<>();
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(DELIMITER)
                    .parse(Files.newBufferedReader(Paths.get(filePath)));

            for (CSVRecord record : records) {
                City city = new City();
                city.setCityId(Integer.parseInt(record.get(HEADER_CITY_ID)));
                city.setCountryId(Integer.parseInt(record.get(HEADER_COUNTRY_ID)));
                city.setRegionId(Integer.parseInt(record.get(HEADER_REGION_ID)));
                city.setCityName(record.get(HEADER_NAME));
                cities.add(city);
            }
            result = Optional.of(cities);
        } catch (IOException e) {
            log.error("CityCrudRepository findAll CSVRecord Exception", e);
        }
        return result;
    }

    @Override
    public Optional<City> findById(Integer id) {
        Optional<City> result = Optional.empty();
        City city = new City();
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(DELIMITER)
                    .parse(Files.newBufferedReader(Paths.get(filePath)));
            for (CSVRecord record : records) {
                if (Objects.equals(Integer.parseInt(record.get(HEADER_CITY_ID)), id)) {
                    city.setCityId(Integer.parseInt(record.get(HEADER_CITY_ID)));
                    city.setCountryId(Integer.parseInt(record.get(HEADER_COUNTRY_ID)));
                    city.setRegionId(Integer.parseInt(record.get(HEADER_REGION_ID)));
                    city.setCityName(record.get(HEADER_NAME));
                    result = Optional.of(city);
                }
            }
        } catch (IOException e) {
            log.error("CityCrudRepository findById CSVRecord Exception", e);
        }
        return result;
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        Optional<List<City>> result = Optional.empty();
        List<City> cities = new ArrayList<>();
        City city = new City();
        try (CSVParser records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(Files.newBufferedReader(Paths.get(filePath)))) {
            List<String> headers = records.getHeaderNames();
            if (headers.contains(fieldName)) {
                for (CSVRecord record : records.getRecords()) {
                    if (Objects.equals(record.get(fieldName), value)) {
                        city.setCityId(Integer.parseInt(record.get(HEADER_CITY_ID)));
                        city.setCountryId(Integer.parseInt(record.get(HEADER_COUNTRY_ID)));
                        city.setRegionId(Integer.parseInt(record.get(HEADER_REGION_ID)));
                        city.setCityName(record.get(HEADER_NAME));
                        cities.add(city);
                        result = Optional.of(cities);
                    }
                }
            }
        } catch (IOException e) {
            log.error("CityCrudRepository findByField CSVRecord Exception", e);
        }
        return result;
    }

    @Override
    public City create(City entity) {
        if (findById(entity.getCityId()).isEmpty()) {
            List<City> citiesList = findAll().get();
            citiesList.add(entity);
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(HEADER_CITY_ID, HEADER_COUNTRY_ID, HEADER_REGION_ID, HEADER_NAME)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (City city : citiesList) {
                    csvPrinter.printRecord(city.getCityId(), city.getCountryId(), city.getRegionId(), city.getCityName());
                }
            } catch (IOException e) {
                log.error("CityCrudRepository create Exception", e);
            }
        } else {
            log.error("City already exist");
        }
        return entity;
    }

    @Override
    public City update(City entity) {
        if (findById(entity.getCityId()).isPresent()) {
            List<City> citiesList = findAll().get();
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(HEADER_CITY_ID, HEADER_COUNTRY_ID, HEADER_REGION_ID, HEADER_NAME)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (City city : citiesList) {
                    if (Objects.equals(entity.getCityId(), city.getCityId())) {
                        city.setCountryId(entity.getCountryId());
                        city.setRegionId(entity.getRegionId());
                        city.setCityName(entity.getCityName());
                    }
                    csvPrinter.printRecord(city.getCityId(), city.getCountryId(), city.getRegionId(), city.getCityName());
                }
            } catch (IOException e) {
                log.error("CityCrudRepository update Exception", e);
            }
        } else {
            log.error("City doesn't exist");
        }
        return entity;
    }

    @Override
    public City delete(Integer id) {
        if (findById(id).isPresent()) {
            Optional<List<City>> citiesList = findAll();
            citiesList.get().removeIf(deletedCountry -> Objects.equals(deletedCountry.getCityId(), id));
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(HEADER_CITY_ID, HEADER_COUNTRY_ID, HEADER_REGION_ID, HEADER_NAME)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (City city : citiesList.get()) {
                    csvPrinter.printRecord(city.getCityId(), city.getCountryId(), city.getRegionId(), city.getCityName());
                }
            } catch (IOException e) {
                log.error("CityCrudRepository delete Exception", e);
            }
        } else {
            log.error("City doesn't exist");
        }
        return null;
    }
}