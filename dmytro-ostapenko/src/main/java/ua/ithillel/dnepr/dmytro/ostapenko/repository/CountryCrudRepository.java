package ua.ithillel.dnepr.dmytro.ostapenko.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.dmytro.ostapenko.repository.entity.Country;

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

@Slf4j
public class CountryCrudRepository implements CrudRepository<Country, Integer> {
    private final String filePath;

    public CountryCrudRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<Country>> findAll() {
        Optional<List<Country>> result = Optional.empty();
        List<Country> countries = new ArrayList<>();
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(DELIMITER)
                    .parse(Files.newBufferedReader(Paths.get(filePath)));

            for (CSVRecord record : records) {
                Country country = new Country();
                country.setCountryId(Integer.parseInt(record.get(HEADER_COUNTRY_ID)));
                country.setCountryName(record.get(HEADER_NAME));
                countries.add(country);
            }
            result = Optional.of(countries);
        } catch (IOException e) {
            log.error("CountryCrudRepository findAll CSVRecord Exception", e);
        }
        return result;
    }

    @Override
    public Optional<Country> findById(Integer id) {
        Optional<Country> result = Optional.empty();
        Country country = new Country();
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(DELIMITER)
                    .parse(Files.newBufferedReader(Paths.get(filePath)));
            for (CSVRecord record : records) {
                if (Objects.equals(Integer.parseInt(record.get(HEADER_COUNTRY_ID)), id)) {
                    country.setCountryId(Integer.parseInt(record.get(HEADER_COUNTRY_ID)));
                    country.setCountryName(record.get(HEADER_NAME));
                    result = Optional.of(country);
                }
            }
        } catch (IOException e) {
            log.error("CountryCrudRepository findById CSVRecord Exception", e);
        }
        return result;
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        Optional<List<Country>> result = Optional.empty();
        List<Country> countries = new ArrayList<>();
        Country country = new Country();
        try (CSVParser records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(Files.newBufferedReader(Paths.get(filePath)))) {
            List<String> headers = records.getHeaderNames();
            if (headers.contains(fieldName)) {
                for (CSVRecord record : records.getRecords()) {
                    if (Objects.equals(record.get(fieldName), value)) {
                        country.setCountryId(Integer.parseInt(record.get(HEADER_COUNTRY_ID)));
                        country.setCityId(Integer.parseInt(record.get(HEADER_CITY_ID)));
                        country.setCountryName(record.get(HEADER_NAME));
                        countries.add(country);
                        result = Optional.of(countries);
                    }
                }
            }
        } catch (IOException e) {
            log.error("CountryCrudRepository findByField CSVRecord Exception", e);
        }
        return result;
    }

    @Override
    public Country create(Country entity) {
        if (findById(entity.getCountryId()).isEmpty()) {
            List<Country> countriesList = findAll().get();
            countriesList.add(entity);
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(HEADER_COUNTRY_ID, HEADER_CITY_ID, HEADER_NAME)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (Country country : countriesList) {
                    csvPrinter.printRecord(country.getCountryId(), country.getCityId(), country.getCountryName());
                }
            } catch (IOException e) {
                log.error("CountryCrudRepository create Exception", e);
            }
        } else {
            log.error("Country already exist");
        }
        return entity;
    }

    @Override
    public Country update(Country entity) {
        if (findById(entity.getCountryId()).isPresent()) {
            List<Country> countriesList = findAll().get();
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(HEADER_COUNTRY_ID, HEADER_CITY_ID, HEADER_NAME)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (Country country : countriesList) {
                    if (Objects.equals(entity.getCountryId(), country.getCountryId())) {
                        country.setCityId(entity.getCityId());
                        country.setCountryName(entity.getCountryName());
                    }
                    csvPrinter.printRecord(country.getCountryId(), country.getCityId(), country.getCountryName());
                }
            } catch (IOException e) {
                log.error("CountryCrudRepository update Exception", e);
            }
        } else {
            log.error("Country doesn't exist");
        }
        return entity;
    }

    @Override
    public Country delete(Integer id) {
        if (findById(id).isPresent()) {
            Optional<List<Country>> countriesList = findAll();
            countriesList.get().removeIf(deletedCountry -> Objects.equals(deletedCountry.getCountryId(), id));
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(HEADER_COUNTRY_ID, HEADER_CITY_ID, HEADER_NAME)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (Country country : countriesList.get()) {
                    csvPrinter.printRecord(country.getCountryId(), country.getCityId(), country.getCountryName());
                }
            } catch (IOException e) {
                log.error("CountryCrudRepository delete Exception", e);
            }
        } else {
            log.error("Country doesn't exist");
        }
        return null;
    }
}