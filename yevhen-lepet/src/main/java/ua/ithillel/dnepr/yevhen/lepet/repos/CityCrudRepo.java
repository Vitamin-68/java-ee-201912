package ua.ithillel.dnepr.yevhen.lepet.repos;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yevhen.lepet.entity.City;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class CityCrudRepo implements CrudRepository<City, Integer> {
    private final String filePath;
    private final char delimiter;
    private static final String CITY_ID = "city_id";
    private static final String COUNTRY_ID = "country_id";
    private static final String REGION_ID = "region_id";
    private static final String NAME = "name";

    public CityCrudRepo(String filePath, char delimiter) {
        this.filePath = filePath;
        this.delimiter = delimiter;
    }

    @Override
    public Optional<List<City>> findAll() {
        Optional<List<City>> result = Optional.empty();
        final List<City> cities = new ArrayList<>();
        try (CSVParser csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(delimiter)
                .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))))) {
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                convetCity(csvRecord);
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
        try (CSVParser csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(delimiter)
                .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))))) {
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                if (Integer.parseInt(csvRecord.get(CITY_ID)) == id) {
                    result = Optional.of(convetCity(csvRecord));
                }
            }
        } catch (IOException e) {
            log.error("Exception CSV " + e);
        }
        return result;
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        Optional<List<City>> result = Optional.empty();
        try (CSVParser csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(delimiter)
                .parse(new FileReader(filePath))) {
            Map<String, Integer> header = csvParser.getHeaderMap();
            List<City> searchCity = new ArrayList<>();
            if (header.get(fieldName) != null) {
                if (City.class.getDeclaredField(fieldName) != null) {
                    for (CSVRecord csvRecord : csvParser.getRecords()) {
                        if (Objects.equals(csvRecord.get(fieldName), value.toString())) {
                            searchCity.add(convetCity(csvRecord));
                        }
                    }
                    result = Optional.of(searchCity);
                } else {
                    log.error("?????????????? ???????? ???? ??????????????");
                }
            }
        } catch (Exception e) {
            log.error("Exception CSV " + e);
        }
        return result;
    }

    @Override
    public City create(City entity) {
        Optional<City> test = findById(entity.getId());
        if (test.isPresent()) return test.get();
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                .withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME)
                .withDelimiter(delimiter)
                .withQuoteMode(QuoteMode.ALL))) {
            for (City city : findAll().get()) {
                csvPrinter.printRecord(city.getId(),
                        city.getCountry_id(),
                        city.getRegion_id(),
                        city.getName());
                csvPrinter.flush();
                csvPrinter.close();
            }
        } catch (IOException e) {
            log.error("Exception CSV " + e);
        }
        return entity;
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
                return entity;
            }
        }
        return entity;
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

    private City convetCity(CSVRecord csvRecord) {
        City city = new City();
        city.setId(Integer.parseInt(csvRecord.get(CITY_ID)));
        city.setCountry_id(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
        city.setRegion_id(Integer.parseInt(csvRecord.get(REGION_ID)));
        city.setName(csvRecord.get(NAME));
        return city;
    }

    private void addCSVPrinter(List<City> cities) {
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                .withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME)
                .withDelimiter(delimiter))) {
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
}
