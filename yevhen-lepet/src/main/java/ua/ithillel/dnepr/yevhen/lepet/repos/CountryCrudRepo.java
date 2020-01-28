package ua.ithillel.dnepr.yevhen.lepet.repos;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yevhen.lepet.entity.Country;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class CountryCrudRepo implements CrudRepository<Country, Integer> {
    private final String filePath;
    private final char delimiter;
    private static final String COUNTRY_ID = "country_id";
    private static final String CITY_ID = "city_id";
    private static final String NAME = "name";

    public CountryCrudRepo(String filePath, char delimiter) {
        this.filePath = filePath;
        this.delimiter = delimiter;
    }

    @Override
    public Optional<List<Country>> findAll() {
        Optional<List<Country>> result = Optional.empty();
        final List<Country> countries = new ArrayList<>();
        try (CSVParser csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(delimiter)
                .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))))){
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                convertCountry(csvRecord);
                Country country = new Country();
                countries.add(country);
            }
            result = Optional.of(countries);
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }

    @Override
    public Optional<Country> findById(Integer id) {
        Optional<Country> result = Optional.empty();
        try {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                if (Integer.parseInt(csvRecord.get(COUNTRY_ID)) == id) {
                    result = Optional.of(convertCountry(csvRecord));
                }
            }
        } catch (IOException e) {
            log.error("Exception CSV " + e);
        }
        return result;
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        Optional<List<Country>> result = Optional.empty();
        try {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new FileReader(filePath));
            Map<String, Integer> header = csvParser.getHeaderMap();
            List<Country> searchCountry = new ArrayList<>();
            if (header.get(fieldName) != null) {
                if (Country.class.getDeclaredField(fieldName) != null) {
                    for (CSVRecord csvRecord : csvParser.getRecords()) {
                        if (Objects.equals(csvRecord.get(fieldName), value.toString())) {
                            searchCountry.add(convertCountry(csvRecord));
                        }
                    }
                    result = Optional.of(searchCountry);
                } else {
                    log.error("Искомое поле не найдено");
                }
            }
        } catch (Exception e) {
            log.error("Exception CSV " + e);
        }
        return result;
    }

    @Override
    public Country create(Country entity) {
        if (findById(entity.getId()).isEmpty()) {
            final List<Country> countries = findAll().get();
            countries.add(entity);
            try {
                CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                        .withHeader(COUNTRY_ID, CITY_ID, NAME)
                        .withDelimiter(delimiter));
                for (Country country : countries) {
                    csvPrinter.printRecord(country.getId(), country.getName());
                    csvPrinter.flush();
                    csvPrinter.close();
                }
            } catch (IOException e) {
                log.error("Exception CSV " + e);
            }
        }
        return entity;
    }

    @Override
    public Country update(Country entity) {
        Optional<List<Country>> countries = findAll();
        for (Country country : countries.get()) {
            if (country.getId().equals(entity.getId())) {
                country.setCity_id(entity.getCity_id());
                country.setName(entity.getName());
                addCSVPrinter(Collections.singletonList(country));
                return entity;
            }
        }
        return entity;
    }

    @Override
    public Country delete(Integer id) {
        Optional<List<Country>> countries = findAll();
        for (Country country : countries.get()) {
            if (country.getId().equals(id)) {
                countries.get().remove(country);
                addCSVPrinter(Collections.singletonList(country));
                return country;
            }
        }
        return new Country();
    }

    private void addCSVPrinter(List<Country> countries) {
        try {
            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(COUNTRY_ID, CITY_ID, NAME)
                    .withDelimiter(delimiter));
            for (Country someCountry : countries) {
                csvPrinter.printRecord(someCountry.getId(), someCountry.getName());
                csvPrinter.flush();
                csvPrinter.close();
            }
        } catch (IOException e) {
            log.error("Exception CSV " + e);
        }
    }

    private Country convertCountry(CSVRecord csvRecord) {
        Country country = new Country();
        country.setName(csvRecord.get(NAME));
        country.setCity_id(Integer.parseInt(csvRecord.get(CITY_ID)));
        country.setId(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
        return country;
    }
}
