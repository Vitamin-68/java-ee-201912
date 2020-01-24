package ua.ithillel.dnepr.eugenekovalov.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.Country;

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
public class CountryCrudRepo implements CrudRepository<Country, Integer>  {

    private final char DIVIDER;
    private final Path PATH;
    private final String COUNTRY_ID = "country_id";
    private final String CITY_ID = "city_id";
    private final String NAME = "name";

    public CountryCrudRepo(Path path, char divider) {
        PATH = path;
        DIVIDER = divider;
    }

    @Override
    public Optional<List<Country>> findAll() {
        Optional<List<Country>> result = Optional.empty();
        List<Country> countries = new ArrayList<>();

        try {
            Reader in = new FileReader(PATH.toFile());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withDelimiter(DIVIDER)
                    .withFirstRecordAsHeader()
                    .parse(in);

            for (CSVRecord record : records) {
                Country country = new Country();

                country.setId(Integer.parseInt(record.get(COUNTRY_ID)));
                country.setCityId(Integer.parseInt(record.get(CITY_ID)));
                country.setName(record.get(NAME));

                countries.add(country);
            }

            result = Optional.of(countries);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return result;
    }


    @Override
    public Optional<Country> findById(Integer id) {
        Optional<List<Country>> countries = findAll();
        return countries.stream()
                .flatMap(List::stream)
                .filter(city -> city.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        List<Country> listResult = new ArrayList<>();
        List<Country> countries = countriesList(findAll());

        Object entityFieldValue = null;
        Field declaredField = null;

        for (Country country : countries) {
            try {
                declaredField = country.getClass().getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                entityFieldValue = declaredField.get(country);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error(e.getMessage());
            }

            if (Objects.equals(value, entityFieldValue)) {
                listResult.add(country);
            }
        }

        return Optional.of(listResult);    }

    @Override
    public Country create(Country entity) {
        writeCsv(List.of(entity));

        return entity;
    }

    @Override
    public Country update(Country entity) {
        if (findById(entity.getId()).isEmpty()) {
            log.error("Entity with ID: " + entity.getId() + " not found");
            return entity;
        }

        List<Country> countries = findAll()
                .stream()
                .flatMap(List::stream)
                .map(o -> o.getId().equals(entity.getId()) ? entity : o)
                .collect(Collectors.toList());

        writeCsv(countries);

        return entity;
    }

    @Override
    public Country delete(Integer id) {
        List<Country> countries = countriesList(findAll());
        Country country = findById(id).get();

        countries.removeIf(country1 -> country.getId().equals(country.getId()));
        writeCsv(countries);

        return country;
    }

    private void writeCsv(List<Country> countries) {
        try (FileWriter writer = new FileWriter(PATH.toFile());
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader(COUNTRY_ID, CITY_ID, NAME)
                     .withDelimiter(DIVIDER))) {
            for (Country country: countries) {
                csvPrinter.printRecord(
                        country.getId(),
                        country.getCityId(),
                        country.getName());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private List<Country> countriesList(Optional<List<Country>> countries) {
        return countries.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
