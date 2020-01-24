package ua.ithillel.dnepr.eugenekovalov.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.City;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class CityCrudRepo implements CrudRepository<City, Integer> {

    private final char DIVIDER;
    private final Path PATH;
    private final String CITY_ID = "city_id";
    private final String COUNTRY_ID = "country_id";
    private final String REGION_ID = "region_id";
    private final String NAME = "name";

    public CityCrudRepo(Path path, char divider) {
        PATH = path;
        DIVIDER = divider;
    }

    @Override
    public Optional<List<City>> findAll() {
        Optional<List<City>> result = Optional.empty();
        List<City> cities = new ArrayList<>();

        try {
            Reader in = new FileReader(PATH.toFile());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withDelimiter(DIVIDER)
                    .withFirstRecordAsHeader()
                    .parse(in);

            for (CSVRecord record : records) {
                City city = new City();

                city.setId(Integer.parseInt(record.get(CITY_ID)));
                city.setCountryId(Integer.parseInt(record.get(COUNTRY_ID)));
                city.setRegionId(Integer.parseInt(record.get(REGION_ID)));
                city.setName(record.get(NAME));

                cities.add(city);
            }

            result = Optional.of(cities);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    @Override
    public Optional<City> findById(Integer id) {
        Optional<List<City>> cities = findAll();
        return cities.stream()
                .flatMap(List::stream)
                .filter(city -> city.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        List<City> listResult = new ArrayList<>();
        List<City> cities = citiesList(findAll());

        Object entityFieldValue = null;
        Field declaredField = null;

        for (City city : cities) {
            try {
                declaredField = city.getClass().getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                entityFieldValue = declaredField.get(city);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error(e.getMessage());
            }

            if (Objects.equals(value, entityFieldValue)) {
                listResult.add(city);
            }
        }

        return Optional.of(listResult);
    }

    @Override
    public City create(City entity) {
        writeCsv(List.of(entity));

        return entity;
    }

    @Override
    public City update(City entity) {
        if (findById(entity.getId()).isEmpty()) {
            log.error("Entity with ID: " + entity.getId() + " not found");
            return entity;
        }

        List<City> cities = findAll()
                .stream()
                .flatMap(List::stream)
                .map(o -> o.getId().equals(entity.getId()) ? entity : o)
                .collect(Collectors.toList());

        writeCsv(cities);

        return entity;
    }

    @Override
    public City delete(Integer id) {
        List<City> cities = citiesList(findAll());
        City city = findById(id).get();

        cities.removeIf(city1 -> city.getId().equals(city1.getId()));
        writeCsv(cities);

        return city;
    }

    private void writeCsv(List<City> cities) {
        try (FileWriter writer = new FileWriter(PATH.toFile());
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME)
                     .withDelimiter(DIVIDER))) {
            for (City city : cities) {
                csvPrinter.printRecord(
                        city.getId(),
                        city.getCountryId(),
                        city.getRegionId(),
                        city.getName());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private List<City> citiesList(Optional<List<City>> cities) {
        return cities.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
