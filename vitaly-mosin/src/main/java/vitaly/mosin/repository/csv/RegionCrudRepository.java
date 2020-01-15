package vitaly.mosin.repository.csv;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import vitaly.mosin.repository.entity.Region;
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
import static vitaly.mosin.repository.Constants.HEAD_NAME;
import static vitaly.mosin.repository.Constants.REGION_ID;

@Slf4j
public class RegionCrudRepository implements CrudRepository<Region, Integer>, MyUtils {

    private final String filePath;
    private final String[] regionCsvHeader = {REGION_ID, COUNTRY_ID, CITY_ID, HEAD_NAME};

    public RegionCrudRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<Region>> findAll() {
        Optional<List<Region>> result = Optional.empty();
        final List<Region> cities = new ArrayList<>();
        try (CSVParser csvParser = DEFAULT
                .withHeader(regionCsvHeader)
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(new FileReader(filePath))) {
            for (CSVRecord oneStr : csvParser) {
                Region region = new Region();
                setFieldsRegion(oneStr, region);
                cities.add(region);
            }
            result = Optional.of(cities);
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }

    @Override
    public Optional<Region> findById(Integer id) {
        Optional<Region> result = Optional.empty();
        try (CSVParser csvParser = DEFAULT
                .withHeader(regionCsvHeader)
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(new FileReader(filePath))) {
            Region city = new Region();
            for (CSVRecord oneStr : csvParser) {
                if (Integer.parseInt(oneStr.get(0)) == id) {
                    setFieldsRegion(oneStr, city);
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
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        Optional<List<Region>> result = Optional.empty();
        final List<Region> regions = new ArrayList<>();
        try (CSVParser csvParser = DEFAULT
                .withHeader(regionCsvHeader)
                .withFirstRecordAsHeader()
                .withDelimiter(DELIMITER)
                .parse(new FileReader(filePath))) {
            int fieldNumber = numberOfColumn4Seek(csvParser, fieldName);
            if (fieldNumber < 0) {
                log.error("Field name is wrong");
                return result;
            }
            for (CSVRecord oneStr : csvParser) {
                Region region = new Region();
                if (oneStr.get(fieldNumber).equalsIgnoreCase(value.toString())) {
                    setFieldsRegion(oneStr, region);
                    regions.add(region);
                }
            }
            if (regions.size() != 0) {
                result = Optional.of(regions);
            }
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }

    @SneakyThrows
    @Override
    public Region create(Region entity) {
        if (findById(entity.getId()).isEmpty()) {
            final List<Region> regions = findAll().get();
            regions.add(entity);
            try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), DEFAULT
                    .withHeader(regionCsvHeader)
                    .withQuoteMode(QuoteMode.ALL)
                    .withDelimiter(DELIMITER))) {
                for (Region region : regions) {
                    csvPrinter.printRecord(region.getId().toString(), region.getCountryId(),
                            0, region.getName());
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
    public Region update(Region entity) {
        int entityId = entity.getId();
        if (findById(entityId).isEmpty()) {
            log.error("Update error! Region with ID = {} not found.", entityId);
            throw new MyRepoException(ExceptionResponseCode.FAILED_UPDATE_CONTACT, "Update error, region not found.");
        }
        List<Region> result = findAll().get();
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), DEFAULT
                .withHeader(regionCsvHeader)
                .withQuoteMode(QuoteMode.ALL)
                .withDelimiter(DELIMITER))) {
            for (Region region : result) {
                if (region.getId().equals(entityId)) {
                    region.setCountryId(entity.getCountryId());
                    region.setName(entity.getName());
                }
                csvPrinter.printRecord(region.getId().toString(), region.getCountryId(),
                        0, region.getName());
            }
        } catch (IOException e) {
            log.error("Failed to write file", e);
        }
        return entity;
    }

    @SneakyThrows
    @Override
    public Region delete(Integer id) {
        Region delRegion = new Region();
        if (findById(id).isEmpty()) {
            log.error("Delete error! Region with ID = {} not found.", id);
            throw new MyRepoException(ExceptionResponseCode.FAILED_DELETE_CONTACT_FROM_DB, "Delete error, region not found.");
        }

        if (regionHasCities(id)) {
            log.error("Delete denied! Country with ID = {} has some linked cities in DB.", id);
            throw new MyRepoException(ExceptionResponseCode.FAILED_DELETE_CONTACT_FROM_DB,
                    "Delete denied! Country has some linked cities in DB.");
        }
        List<Region> result = findAll().get();
        for (Region region : result) {
            if (region.getId().equals(id)) {
                setFieldsRegion(region, delRegion);
                result.remove(region);
                break;
            }
        }
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), DEFAULT
                .withHeader(regionCsvHeader)
                .withQuoteMode(QuoteMode.ALL)
                .withDelimiter(DELIMITER))) {
            for (Region region : result) {
                csvPrinter.printRecord(region.getId().toString(), region.getCountryId(),
                        0, region.getName());
            }
        } catch (IOException e) {
            log.error("Failed to write file", e);
        }
        return delRegion;
    }

    private void setFieldsRegion(CSVRecord oneStr, Region region) {
        region.setId(Integer.parseInt(oneStr.get(0)));
        region.setCountryId(Integer.parseInt(oneStr.get(1)));
        region.setName(oneStr.get(3));
    }

    private void setFieldsRegion(Region sourceRegion, Region targetRegion) {
        targetRegion.setId(sourceRegion.getId());
        targetRegion.setCountryId(sourceRegion.getCountryId());
        targetRegion.setName(sourceRegion.getName());
    }

    private boolean regionHasCities(Integer id) {
        CityCrudRepository cityCrudRepository = new CityCrudRepository(FILE_PATH_TMP + FILE_CITY);
        return cityCrudRepository.findByField(REGION_ID, id).isPresent();
    }
}
