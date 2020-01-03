package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.dml.domain.Country;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class CountryRepository implements CrudRepository<Country, Integer> {

    public static final String COUNTRY_ID = "country_id";
    public static final String CITY_ID = "city_id";
    public static final String NAME = "name";
    private final String filePath;
    private final char delimiter;

    public CountryRepository(String filePath) {
        this.delimiter = ';';
        this.filePath = filePath;
    }

    public CountryRepository(String filePath, char delim) {
        this.delimiter = delim;
        this.filePath = filePath;
    }

    @Override
    public Optional<List<Country>> findAll() {
        Optional<List<Country>> result = Optional.empty();

        final List<Country> countries = new ArrayList<>();
        try {
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine : csvSourse.getRecords()
            ) {

                Country country = new Country();

                country.setCity_id(Integer.parseInt(csvLine.get(CITY_ID)));
                country.setId(Integer.parseInt(csvLine.get(COUNTRY_ID)));
                country.setName(csvLine.get(NAME));
                countries.add(country);
            }
            result = Optional.of(countries);
        } catch (Exception e) {

            log.error("CSV reader:", e);
        }
        return result;
    }

    @Override
    public Optional<Country> findById(Integer id) {
        Optional<Country> result = Optional.empty();
        try {
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine : csvSourse.getRecords()
            ) {
                if (Integer.parseInt(csvLine.get(COUNTRY_ID)) == id) {
                    result = Optional.of(getCountry(csvLine));
                }
            }
        } catch (Exception e) {
            log.error("CSV reader:", e);
        }
        return result;
    }

    private Country getCountry(CSVRecord csvLine) {
        Country country = new Country();
        country.setCity_id(Integer.parseInt(csvLine.get(CITY_ID)));
        country.setId(Integer.parseInt(csvLine.get(COUNTRY_ID)));
        country.setName(csvLine.get(NAME));
        return country;
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        Optional<List<Country>> result = Optional.empty();
        try {
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            Map<String, Integer> header = csvSourse.getHeaderMap();
            List<Country> resultCountry = new ArrayList<>();
            if (header.get(fieldName) != null) {
                if (Country.class.getDeclaredField(fieldName) != null) {
                    for (CSVRecord csvLine : csvSourse.getRecords()
                    ) {
                        if (Objects.equals(csvLine.get(fieldName), value.toString())) {
                            resultCountry.add(getCountry(csvLine));
                        }
                    }
                    result = Optional.of(resultCountry);
                }
            }
        } catch (Exception e) {
            log.error("CSV reader:", e);
        }
        return result;
    }

    @Override
    public Country create(Country entity) {
        Optional<Country> test = findById(entity.getId());
        if (!test.isEmpty()) return test.get();
        try {
            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT.withHeader(COUNTRY_ID, CITY_ID, NAME).withDelimiter(delimiter));
            csvPrinter.printRecord(entity.getId(), entity.getCity_id(), entity.getName());
            csvPrinter.flush();
            csvPrinter.close();
        } catch (IOException e) {
            log.error("CSV printer:", e);
        }
        return entity;
    }

    @Override
    public Country update(Country entity) {
        Optional<List<Country>> allRecords = findAll();
        for (Country currentCountry : allRecords.get()
        ) {
            if (currentCountry.getId().equals(entity.getId())) {
                currentCountry.setCity_id(entity.getCity_id());
                currentCountry.setName(entity.getName());
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT.withHeader(COUNTRY_ID, CITY_ID, NAME).withDelimiter(delimiter));
                    for (Country locCountry : allRecords.get()
                    ) {
                        csvPrinter.printRecord(locCountry.getId(), locCountry.getCity_id(), locCountry.getName());
                    }
                    csvPrinter.flush();
                    csvPrinter.close();
                } catch (IOException e) {
                    log.error("CSV printer:", e);
                }
                return currentCountry;
            }
        }
        return new Country();
    }

    @Override
    public Country delete(Integer id) {
        Optional<List<Country>> allRecords = findAll();
        for (Country currentRegion : allRecords.get()
        ) {
            if (currentRegion.getId().equals(id)) {
                allRecords.get().remove(currentRegion);
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT.withHeader(COUNTRY_ID, CITY_ID, NAME).withDelimiter(delimiter));
                    for (Country _region : allRecords.get()
                    ) {
                        csvPrinter.printRecord(_region.getId(), _region.getCity_id(), _region.getName());
                    }
                    csvPrinter.flush();
                    csvPrinter.close();
                } catch (IOException e) {
                    log.error("CSV printer:", e);
                }
                return new Country();
            }
        }
        return new Country();
    }
}
