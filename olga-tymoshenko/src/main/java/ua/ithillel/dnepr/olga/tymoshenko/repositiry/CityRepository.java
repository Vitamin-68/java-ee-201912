package ua.ithillel.dnepr.olga.tymoshenko.repositiry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.olga.tymoshenko.entity.City;
import ua.ithillel.dnepr.olga.tymoshenko.util.CSVFileWorker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CityRepository implements CrudRepository<City, Integer> {

    private final CSVFileWorker fileWorker;

    public CityRepository(File file) {
        fileWorker = new CSVFileWorker(file.getPath());
    }

    @Override
    public Optional<List<City>> findAll() {
        Optional<List<City>> result;
        final List<City> cities = new ArrayList<>();

        final CSVParser csvParser = fileWorker.getCSVParser();
        try {
            for (CSVRecord record : csvParser.getRecords()) {
                cities.add(setRecordIntoCity(record));
            }
            csvParser.close();
        } catch (IOException e) {
            log.error("Failed region repository findAll", e);
        }
        result = Optional.of(cities);
        return result;
    }

    @Override
    public Optional<City> findById(Integer id) {
        Optional<City> result = Optional.empty();
        City city = new City();

        final CSVParser csvParser = fileWorker.getCSVParser();
        try {
            for (CSVRecord record : csvParser.getRecords()) {
                if (Integer.parseInt(record.get(0)) == id) {
                    city = setRecordIntoCity(record);
                }
                csvParser.close();
            }
            result = Optional.of(city);
        } catch (IOException e) {
            log.error("Failed region repository findById", e);
        }
        return result;
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        Optional<List<City>> result = Optional.empty();
        final List<City> cities = new ArrayList<>();
        final CSVParser csvParser = fileWorker.getCSVParser();
        try {
            List<String> header = csvParser.getHeaderNames();
            if (header.contains(fieldName)) {
                for (CSVRecord record : csvParser.getRecords()) {
                    if (record.get(fieldName).equals(value.toString()))
                        cities.add(setRecordIntoCity(record));
                }
            }
            csvParser.close();
            result = Optional.of(cities);

        } catch (IOException e) {
            log.error("Failed city findByField", e);
        }
        return result;
    }

    @Override
    public City create(City entity) {
        Optional<City> city = findById(entity.getCountryId());
        if (city.isPresent()) {
            if (city.get().getCountryId() != null) {
                return new City();
            }
        }
        if (fileWorker.CSVWriteFile(true, false, entity)) {
            return entity;
        }
        return new City();
    }

    @Override
    public City update(City entity) {

        Optional<City> city = findById(entity.getCityId());
        if (city.isPresent()) {
            if (city.get().getCityId() == null) {
                return new City();
            }
        }
        Optional<List<City>> allCity = findAll();
        if (allCity.isPresent()) {
            for (City record : allCity.get()) {
                if (record.getCityId().equals(entity.getCityId())) {
                    record.setRegionId(entity.getRegionId());
                    record.setCountryId(entity.getCountryId());
                    record.setName(entity.getName());
                }
            }

            if (fileWorker.CSVWriteFile(false, true, allCity.get())) {
                return entity;
            }
        }
        return new City();
    }

    @Override
    public City delete(Integer id) {
        Optional<City> city = findById(id);
        if (city.isPresent()) {
            if (city.get().getCityId() == null) {
                return city.get();
            }
        }
        Optional<List<City>> allCity = findAll();
        City delCity = null;
        if (allCity.isPresent()) {
            for (City record : allCity.get()) {
                if (record.getCityId().equals(id)) {
                    delCity = record;
                    allCity.get().remove(allCity.get().indexOf(record));
                    break;
                }
            }

            if (fileWorker.CSVWriteFile(false, true, allCity.get())) {
                return delCity;
            }
        }
        return new City();
    }

    private City setRecordIntoCity(CSVRecord record) {
        final City city = new City();
        city.setCityId(Integer.parseInt(record.get("city_id")));
        city.setCountryId(Integer.parseInt(record.get("country_id")));
        city.setRegionId(Integer.parseInt(record.get("region_id")));
        city.setName(record.get("name"));
        return city;
    }
}

