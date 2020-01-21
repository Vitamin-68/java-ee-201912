package ua.hillel.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.hillel.entity.City;
import ua.ithillel.dnepr.common.repository.CrudRepository;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class CrudRepositoryCity implements CrudRepository<City, Integer> {

    private String path;
    private char delimeter;

    @Override
    public Optional<List<City>> findAll() {
        log.info("Count of records {}", readCsv().size());
        return Optional.of(readCsv());
    }

    @Override
    public Optional<City> findById(Integer id) {
        Optional<City> cities = readCsv()
                .stream()
                .filter(city -> city.getCityId() == id)
                .findFirst();
        if (cities.isEmpty()) {
            log.info("id {} not found", id);
        }
        return cities;
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        Class<City> cityReflect = City.class;
        List<City> cities = new ArrayList<>();

        try {
            Field fields = cityReflect.getDeclaredField(fieldName);
            fields.setAccessible(true);
            Object changedValueType;

            if (fields.getType().equals("int")) {
                changedValueType = Integer.parseInt(value.toString());
            } else {
                changedValueType = value;
            }

            for (City city : readCsv()) {
                if (fields.get(city).toString().equals(changedValueType)) {
                    cities.add(city);
                }
            }

            if (cities.isEmpty()) {
                log.info("value {} not found", value);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.info("Field not found {}", e.getMessage());
        }
        return Optional.of(cities);
    }

    @Override
    public City create(City entity) {
        List<City> cities = readCsv();
        boolean flag = false;

        for (City city : cities) {
            if (city.getCityId() == entity.getCityId()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            log.error("Duplicate id  {}", entity.getCityId());
        } else {
            cities.add(entity);
        }
        writeCsv(cities);
        log.info(entity + " created");
        return entity;
    }

    @Override
    public City update(City entity) {
        List<City> cities = readCsv();
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getCityId() == entity.getCityId()) {
                cities.set(i, entity);
            }
        }
        writeCsv(cities);
        log.info("{} updated", cities);
        return entity;
    }

    @Override
    public City delete(Integer id) {
        List<City> cities = readCsv();
        City city = findById(id).get();
        cities.remove(city);

        if (city.getCityId() == id) {
            writeCsv(cities);
            log.info("deleted {}", city);
        } else {
            log.info("can't find id {}", id);
        }
        return city;
    }

    private List<City> readCsv() {
        List<City> cities = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(path))) {
            CSVParser csvData = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL)
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimeter)
                    .parse(reader);

            for (CSVRecord csvRecord : csvData) {
                int cityId = Integer.parseInt(csvRecord.get(0));
                int countryId = Integer.parseInt(csvRecord.get(1));
                int regionId = Integer.parseInt(csvRecord.get(2));
                String name = csvRecord.get(3);
                cities.add(new City(cityId, countryId, regionId, name));
            }
        } catch (IOException e) {
            log.error("File not found {}", e.getMessage());
        }
        return cities;
    }

    private void writeCsv(List<City> cities) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(path));
             CSVPrinter printer = CSVFormat.DEFAULT
                     .withHeader("city_id", "country_id", "region_id", "name")
                     .withQuoteMode(QuoteMode.ALL).withDelimiter(delimeter)
                     .print(writer)) {
            for (City city : cities) {
                printer.printRecord(city.getCityId(), city.getCountryId(), city.getRegionId(), city.getName());
            }
        } catch (IOException e) {
            log.error("Can't write info {}", e.getMessage());
        }
    }
}