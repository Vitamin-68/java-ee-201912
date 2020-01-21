package ua.hillel.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.hillel.entity.Country;
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
public class CrudRepositoryCountry implements CrudRepository<Country, Integer> {

    private String path;
    private char delimeter;

    @Override
    public Optional<List<Country>> findAll() {
        log.info("Count of records {} ", readCsv().size());
        return Optional.of(readCsv());
    }

    @Override
    public Optional<Country> findById(Integer id) {
        Optional<Country> countries = readCsv()
                .stream()
                .filter(city -> city.getCountryId() == id)
                .findFirst();
        if (countries.isEmpty()) {
            log.info("id {} of city isn't found", id);
        }
        return countries;
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        Class<Country> countryReflect = Country.class;
        List<Country> countries = new ArrayList<>();

        try {
            Field fields = countryReflect.getDeclaredField(fieldName);
            fields.setAccessible(true);
            Object changedValueType;

            if (fields.getType().equals("int")) {
                changedValueType = Integer.parseInt(value.toString());
            } else {
                changedValueType = value;
            }

            for (Country country : readCsv()) {
                if (fields.get(country).toString().equals(changedValueType)) {
                    countries.add(country);
                }
            }

            if (countries.isEmpty()) {
                log.info("value {} not found ", value);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.info("Field not found {}", e);
        }
        return Optional.of(countries);
    }

    @Override
    public Country create(Country entity) {
        List<Country> countries = readCsv();
        boolean flag = false;

        for (Country city : countries) {
            if (city.getCityId() == entity.getCityId()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            log.error("Duplicate id {}", entity.getCityId());
        } else {
            countries.add(entity);
        }
        writeCsv(countries);
        log.info("created {}", entity);
        return entity;
    }

    @Override
    public Country update(Country entity) {
        List<Country> countries = readCsv();
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCountryId() == entity.getCountryId()) {
                countries.set(i, entity);
            }
        }
        writeCsv(countries);
        log.info("updated {}", countries);
        return entity;
    }

    @Override
    public Country delete(Integer id) {
        List<Country> cities = readCsv();
        Country country = findById(id).get();
        cities.remove(country);
        if (country.getCountryId() == id) {
            writeCsv(cities);
            log.info("deleted " + country);
        } else {
            log.info("can't find id: {}", id);
        }
        return country;
    }

    private List<Country> readCsv() {
        List<Country> countries = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(path))) {
            CSVParser csvData = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL)
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimeter)
                    .parse(reader);

            for (CSVRecord csvRecord : csvData) {
                int countryId = Integer.parseInt(csvRecord.get(0));
                int cityId = Integer.parseInt(csvRecord.get(1));
                String name = csvRecord.get(2);
                countries.add(new Country(countryId, cityId, name));
            }
        } catch (IOException e) {
            log.error("File not found: {}", e.getMessage());
        }
        return countries;
    }

    private void writeCsv(List<Country> countries) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(path));
             CSVPrinter printer = CSVFormat.DEFAULT
                     .withHeader("country_id", "city_id", "name")
                     .withQuoteMode(QuoteMode.ALL).withDelimiter(delimeter)
                     .print(writer)) {
            for (Country country : countries) {
                printer.printRecord(country.getCountryId(), country.getCityId(), country.getName());
            }
        } catch (IOException e) {
            log.error("Can't write info: {}", e.getMessage());
        }
    }
}