package ua.ithillel.dnepr.olga.tymoshenko.repositiry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.olga.tymoshenko.entity.Country;
import ua.ithillel.dnepr.olga.tymoshenko.util.CSVFileWorker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CountryRepository implements CrudRepository<Country, Integer> {
    private final CSVFileWorker fileWorker;

    public CountryRepository(File file) {
        fileWorker = new CSVFileWorker(file.getPath());
    }

    @Override
    public Optional<List<Country>> findAll() {
        Optional<List<Country>> result;
        final List<Country> country = new ArrayList<>();

        final CSVParser csvParser = fileWorker.getCSVParser();
        try {
            for (CSVRecord record : csvParser.getRecords()) {

                country.add(setRecordIntoCountry(record));
            }
            csvParser.close();
        } catch (IOException e) {
            log.error("Failed country repository findAll", e);
        }
        result = Optional.of(country);
        return result;
    }

    @Override
    public Optional<Country> findById(Integer id) {
        Optional<Country> result = Optional.empty();
        Country country = new Country();

        final CSVParser csvParser = fileWorker.getCSVParser();
        try {
            for (CSVRecord record : csvParser.getRecords()) {
                if (Integer.parseInt(record.get(0)) == id) {
                    country = setRecordIntoCountry(record);

                }
                csvParser.close();
            }
            result = Optional.of(country);

        } catch (IOException e) {
            log.error("Failed country repository findByField", e);
        }

        return result;

    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {

        Optional<List<Country>> result = Optional.empty();
        final List<Country> country = new ArrayList<>();

        final CSVParser csvParser = fileWorker.getCSVParser();
        try {
            List<String> header = csvParser.getHeaderNames();
            if (header.contains(fieldName)) {

                for (CSVRecord record : csvParser.getRecords()) {
                    if (record.get(fieldName).equals(value.toString()))
                        country.add(setRecordIntoCountry(record));
                }
            }
            csvParser.close();
            result = Optional.of(country);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Country create(Country entity) {
        Optional<Country> city = findById(entity.getCountryId());

        if (city.isPresent()) {
            if (city.get().getCountryId() != null) {
                return new Country();
            }
        }

        if (fileWorker.CSVWriteFile(true, false, entity)) {
            return entity;
        }
        return new Country();
    }

    @Override
    public Country update(Country entity) {

        Optional<Country> country = findById(entity.getCountryId());
        if (country.isPresent()) {
            if (country.get().getCountryId() == null) {
                return new Country();
            }
        }

        Optional<List<Country>> allCountry = findAll();
        if (allCountry.isPresent()) {
            for (Country record : allCountry.get()) {
                if (record.getCountryId().equals(entity.getCountryId())) {
                    record.setCityId(entity.getCityId());
                    record.setName(entity.getName());
                }
            }


            if (fileWorker.CSVWriteFile(false, true, allCountry.get())) {
                return entity;
            }
        }

        return new Country();
    }

    @Override
    public Country delete(Integer id) {
        Optional<Country> country = findById(id);
        if (country.isPresent()) {
            if (country.get().getCountryId() == null) {
                return country.get();
            }
        }
        Optional<List<Country>> allCountry = findAll();
        Country delCountry = null;
        if (allCountry.isPresent()) {
            for (Country record : allCountry.get()) {
                if (record.getCountryId().equals(id)) {
                    delCountry = record;
                    allCountry.get().remove(allCountry.get().indexOf(record));
                    break;
                }
            }


            if (fileWorker.CSVWriteFile(false, true, allCountry.get())) {
                return delCountry;
            }
        }
        return new Country();
    }

    private Country setRecordIntoCountry(CSVRecord record) {
        final Country country = new Country();
        country.setCountryId(Integer.parseInt(record.get(0)));
        country.setCityId(Integer.parseInt(record.get(1)));
        country.setName(record.get(2));
        return country;

    }
}
