package vitaly.mosin.repository.csv;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import vitaly.mosin.repository.entity.Country;
import vitaly.mosin.repository.exceptions.ExceptionResponseCode;
import vitaly.mosin.repository.exceptions.MyRepoException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static vitaly.mosin.repository.Constants.CITY_ID;
import static vitaly.mosin.repository.Constants.COUNTRY_ID;
import static vitaly.mosin.repository.Constants.DELIMITER;
import static vitaly.mosin.repository.Constants.FILE_CITY;
import static vitaly.mosin.repository.Constants.FILE_PATH_TMP;
import static vitaly.mosin.repository.Constants.FILE_REGION;
import static vitaly.mosin.repository.Constants.HEAD_NAME;

@Slf4j
public class CountryCrudRepository implements CrudRepository<Country, Integer>, MyUtils {
    private final String filePath;
    private final String[] countryCsvHeader = {COUNTRY_ID, CITY_ID, HEAD_NAME};

    public CountryCrudRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<Country>> findAll() {
        Optional<List<Country>> result = Optional.empty();
        final List<Country> countries = new ArrayList<>();
        try (CSVParser csvParser = DEFAULT
                .withHeader(countryCsvHeader)
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(new FileReader(filePath))) {
            for (CSVRecord oneStr : csvParser) {
                Country country = new Country();
                setFieldsCountry(oneStr, country);
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
        try (CSVParser csvParser = DEFAULT
                .withHeader(countryCsvHeader)
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(new FileReader(filePath))) {
            Country country = new Country();
            for (CSVRecord oneStr : csvParser) {
                if (Integer.parseInt(oneStr.get(0)) == id) {
                    setFieldsCountry(oneStr, country);
                    result = Optional.of(country);
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        Optional<List<Country>> result = Optional.empty();
        final List<Country> countries = new ArrayList<>();
        try (CSVParser csvParser = DEFAULT
                .withHeader(countryCsvHeader)
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(new FileReader(filePath))) {
            int fieldNumber = numberOfColumn4Seek(csvParser, fieldName);
            if (fieldNumber < 0) {
                log.error("Field name is wrong");
                return result;
            }
            for (CSVRecord oneStr : csvParser) {
                Country country = new Country();
                if (oneStr.get(fieldNumber).equalsIgnoreCase(value.toString())) {
                    setFieldsCountry(oneStr, country);
                    countries.add(country);
                }
            }
            if (countries.size() != 0) {
                result = Optional.of(countries);
            }
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }

    @SneakyThrows
    @Override
    public Country create(Country entity) {
        if (findById(entity.getId()).isEmpty()) {
            final List<Country> countries = findAll().get();
            countries.add(entity);
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), DEFAULT
                    .withHeader(countryCsvHeader)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (Country country : countries) {
                    csvPrinter.printRecord(country.getId().toString(), 0, country.getName());
                }
                return entity;
            } catch (IOException e) {
                log.error("Failed to write file", e);
            }
        }
        log.error("Entity already exist");
        throw new MyRepoException(ExceptionResponseCode.FAILED_CREATE_CONTACT, "Entity already exist");
    }

    @SneakyThrows
    @Override
    public Country update(Country entity) {
        int entityId = entity.getId();
        if (findById(entityId).isEmpty()) {
            log.error("Update error! Country with ID = {} not found.", entityId);
            throw new MyRepoException(ExceptionResponseCode.FAILED_UPDATE_CONTACT, "Update error, country not found.");
        }
        List<Country> result = findAll().get();
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), DEFAULT
                .withHeader(countryCsvHeader)
                .withQuoteMode(QuoteMode.ALL)
                .withDelimiter(DELIMITER))) {
            for (Country country : result) {
                if (country.getId().equals(entityId)) {
                    country.setName(entity.getName());
                }
                csvPrinter.printRecord(country.getId().toString(), 0, country.getName());
            }
        } catch (IOException e) {
            log.error("Failed to write file", e);
        }
        return entity;
    }

    @SneakyThrows
    @Override
    public Country delete(Integer id) {
        Country delCountry = new Country();
        if (findById(id).isEmpty()) {
            log.error("Delete error! Country with ID = {} not found.", id);
            throw new MyRepoException(ExceptionResponseCode.FAILED_DELETE_CONTACT_FROM_DB,
                    "Delete error, country not found.");
        }

        if (countryHasCitiesOrRegions(id)) {
            log.error("Delete denied! Country with ID = {} has some linked cities or regions in DB.", id);
            throw new MyRepoException(ExceptionResponseCode.FAILED_DELETE_CONTACT_FROM_DB,
                    "Delete denied! Country has some linked cities or regions in DB.");
        }
        List<Country> result = findAll().get();
        for (Country country : result) {
            if (country.getId().equals(id)) {
                setFieldsCountry(country, delCountry);
                result.remove(country);
                break;
            }
        }
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), DEFAULT
                .withHeader(countryCsvHeader)
                .withQuoteMode(QuoteMode.ALL)
                .withDelimiter(DELIMITER))) {
            for (Country country : result) {
                csvPrinter.printRecord(country.getId().toString(), 0, country.getName());
            }
        } catch (IOException e) {
            log.error("Failed to write file", e);
        }
        return delCountry;
    }

    private void setFieldsCountry(CSVRecord oneStr, Country country) {
        country.setId(Integer.parseInt(oneStr.get(0)));
        country.setName(oneStr.get(2));
    }

    private void setFieldsCountry(Country sourceCountry, Country targetCountry) {
        targetCountry.setId(sourceCountry.getId());
        targetCountry.setName(sourceCountry.getName());
    }

    private boolean countryHasCitiesOrRegions(Integer id) {
        CityCrudRepository cityCrudRepository = new CityCrudRepository(FILE_PATH_TMP + FILE_CITY);
        RegionCrudRepository regionCrudRepository = new RegionCrudRepository(FILE_PATH_TMP + FILE_REGION);
        return (cityCrudRepository.findByField(COUNTRY_ID, id).isPresent() ||
                regionCrudRepository.findByField(COUNTRY_ID, id).isPresent());
    }
}
