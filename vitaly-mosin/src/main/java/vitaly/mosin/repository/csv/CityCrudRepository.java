package vitaly.mosin.repository.csv;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import vitaly.mosin.repository.entity.City;
import vitaly.mosin.repository.exceptions.ExceptionResponseCode;
import vitaly.mosin.repository.exceptions.MyRepoException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static vitaly.mosin.repository.Constants.CITY_ID;
import static vitaly.mosin.repository.Constants.COUNTRY_ID;
import static vitaly.mosin.repository.Constants.DELIMITER;
import static vitaly.mosin.repository.Constants.HEAD_NAME;
import static vitaly.mosin.repository.Constants.REGION_ID;


@Slf4j
public class CityCrudRepository implements CrudRepository<City, Integer>, MyUtils {

    private final String filePath;
    private final String[] cityCsvHeader = {CITY_ID, COUNTRY_ID, REGION_ID, HEAD_NAME};

    public CityCrudRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<City>> findAll() {
        Optional<List<City>> result = Optional.empty();
        final List<City> cities = new ArrayList<>();
        try (CSVParser csvParser = DEFAULT
                .withHeader(cityCsvHeader)
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(new FileReader(filePath, StandardCharsets.UTF_8))) {
            for (CSVRecord oneStr : csvParser) {
                City city = new City();
                setFieldsCity(oneStr, city);
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
        try (CSVParser csvParser = DEFAULT
                .withHeader(cityCsvHeader)
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(new FileReader(filePath, StandardCharsets.UTF_8))) {
            City city = new City();
            for (CSVRecord oneStr : csvParser) {
                if (Integer.parseInt(oneStr.get(0)) == id) {
                    setFieldsCity(oneStr, city);
                    result = Optional.of(city);
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        Optional<List<City>> result = Optional.empty();
        final List<City> cities = new ArrayList<>();
        try (CSVParser csvParser = DEFAULT
                .withHeader(cityCsvHeader)
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(new FileReader(filePath, StandardCharsets.UTF_8))) {
            int fieldNumber = numberOfColumn4Seek(csvParser, fieldName);
            if (fieldNumber < 0) {
                log.error("Field name is wrong");
                return result;
            }
            for (CSVRecord oneStr : csvParser) {
                City city = new City();
                if (oneStr.get(fieldNumber).equalsIgnoreCase(value.toString())) {
                    setFieldsCity(oneStr, city);
                    cities.add(city);
                }
            }
            if (cities.size() != 0) {
                result = Optional.of(cities);
            }
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }

    @SneakyThrows
    @Override
    public City create(City entity) {
        if (findById(entity.getId()).isEmpty()) {
            final List<City> cities = findAll().get();
            cities.add(entity);
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath, StandardCharsets.UTF_8), DEFAULT
                    .withHeader(cityCsvHeader)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (City city : cities) {
                    csvPrinter.printRecord(city.getId().toString(), city.getCountryId(),
                            city.getRegionId(), city.getName());
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
    public City update(City entity) {
        int entityId = entity.getId();
        if (findById(entityId).isEmpty()) {
            log.error("Update error! City with ID = {} not found.", entityId);
            throw new MyRepoException(ExceptionResponseCode.FAILED_UPDATE_CONTACT, "Update error, city not found.");
        }
        List<City> result = findAll().get();
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath, StandardCharsets.UTF_8), DEFAULT
                .withHeader(cityCsvHeader)
                .withQuoteMode(QuoteMode.ALL)
                .withDelimiter(DELIMITER))) {
            for (City city : result) {
                if (city.getId().equals(entityId)) {
                    city.setCountryId(entity.getCountryId());
                    city.setRegionId(entity.getRegionId());
                    city.setName(entity.getName());
                }
                csvPrinter.printRecord(city.getId().toString(), city.getCountryId(),
                        city.getRegionId(), city.getName());
            }
        } catch (IOException e) {
            log.error("Failed to write file", e);
        }
        return entity;
    }

    @SneakyThrows
    @Override
    public City delete(Integer id) {
        City delCity = new City();
        if (findById(id).isEmpty()) {
            log.error("Delete error! City with ID = {} not found.", id);
            throw new MyRepoException(ExceptionResponseCode.FAILED_DELETE_CONTACT_FROM_DB, "Delete error, city not found.");
        }
        List<City> result = findAll().get();
        for (City city : result) {
            if (city.getId().equals(id)) {
                setFieldsCity(city, delCity);
                result.remove(city);
                break;
            }
        }
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath, StandardCharsets.UTF_8), DEFAULT
                .withHeader(cityCsvHeader)
                .withQuoteMode(QuoteMode.ALL)
                .withDelimiter(DELIMITER))) {
            for (City city : result) {
                csvPrinter.printRecord(city.getId().toString(), city.getCountryId(),
                        city.getRegionId(), city.getName());
            }
        } catch (IOException e) {
            log.error("Failed to write file", e);
        }
        return delCity;
    }

    private void setFieldsCity(CSVRecord oneStr, City city) {
        city.setId(Integer.parseInt(oneStr.get(0)));
        city.setCountryId(Integer.parseInt(oneStr.get(1)));
        city.setRegionId(Integer.parseInt(oneStr.get(2)));
        city.setName(oneStr.get(3));
    }

    private void setFieldsCity(City sourceCity, City targetCity) {
        targetCity.setId(sourceCity.getId());
        targetCity.setCountryId(sourceCity.getCountryId());
        targetCity.setRegionId(sourceCity.getRegionId());
        targetCity.setName(sourceCity.getName());
    }

}
