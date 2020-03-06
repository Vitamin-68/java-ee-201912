package ua.ithillel.dnepr.tymoshenko.olga.repository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.tymoshenko.olga.entity.Region;
import ua.ithillel.dnepr.tymoshenko.olga.util.CSVFileWorker;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class RegionRepository implements CrudRepository<Region, Integer> {
    private final CSVFileWorker fileWorker;
    private final List<Region> listRepository = new ArrayList<>();
    private List<String> listHeaders;

    public RegionRepository(File file, char delimiter) {
        try {
            fileWorker = new CSVFileWorker(file.getPath(), delimiter);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        getRepository();
    }

    @Override
    public Optional<List<Region>> findAll() {
        return listRepository.isEmpty() ? Optional.empty() : Optional.of(listRepository);
    }

    @Override
    public Optional<Region> findById(Integer id) {
        if (listRepository.isEmpty()) {
            return Optional.empty();
        }
        Region region = null;
        for (Region record : listRepository) {
            if (record.getId().equals(id)) {
                region = record;
            }
        }
        return region == null ? Optional.empty() : Optional.of(region);
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        Objects.requireNonNull(fieldName, "Field name is undefined");
        final List<Region> regions = new ArrayList<>();
        if (fieldName.isEmpty()) throw new IllegalArgumentException("Field name is empty");
        if (listRepository.isEmpty() || listHeaders.isEmpty()) {
            return Optional.empty();
        }
        if (listHeaders.contains(fieldName)) {
            for (Region record : listRepository) {
                Field[] fields = record.getClass().getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (Field field : fields) {
                    try {
                        if (field.getName().equals(fieldName) && field.get(record).equals(value)) {
                            regions.add(record);
                        }
                    } catch (IllegalAccessException e) {
                        log.error(String.valueOf(e));
                        throw new IllegalStateException("Failed to find by field region repository " + e);
                    }
                }
            }
        }
        return regions.isEmpty() ? Optional.empty() : Optional.of(regions);
    }

    @Override
    public Region create(Region entity) {
        Objects.requireNonNull(entity, "Region entity is undefined");
        Optional<Region> region = findById(entity.getId());
        if (region.isEmpty()) {
            try {
                fileWorker.CSVWriteFile(true, false, entity);
            } catch (IOException e) {
                log.error("Failed to creat region", e);
                throw new IllegalStateException("Failed to create region repository " + e);
            }
        }
        return entity;
    }

    @Override
    public Region update(Region entity) {
        Objects.requireNonNull(entity, "Region entity is undefined");
        if (listRepository.isEmpty()) {
            return null;
        }
        Optional<Region> region = findById(entity.getId());
        if (region.isEmpty()) {
            try {
                fileWorker.CSVWriteFile(true, false, entity);
            } catch (IOException e) {
                log.error("Failed to creat region", e);
                throw new IllegalStateException("Failed to create region repository " + e);
            }
        } else {
            for (Region record : listRepository) {
                if (record.getId().equals(entity.getId())) {
                    record.setCityId(entity.getCityId());
                    record.setCountryId(entity.getCountryId());
                    record.setName(entity.getName());
                }
            }
            try {
                fileWorker.CSVWriteFile(false, true, listRepository);
            } catch (IOException e) {
                log.error("Failed to update region", e);
                throw new IllegalStateException("Failed to update region repository " + e);
            }
        }
        return entity;
    }

    @Override
    public Region delete(Integer id) {
        if (listRepository.isEmpty()) {
            return null;
        }
        Optional<Region> city = findById(id);
        if (city.isEmpty()) {
            return null;
        }
        Region delRegion = null;
        for (int i = 0; i < listRepository.size(); i++) {
            if (listRepository.get(i).getId().equals(id)) {
                delRegion = listRepository.get(i);
                listRepository.remove(i);
                break;
            }
        }
        try {
            fileWorker.CSVWriteFile(false, true, listRepository);
        } catch (IOException e) {
            log.error("Failed to delete region", e);
            throw new IllegalStateException("Failed to delete region repository " + e);
        }
        return delRegion;
    }

    private Region setRecordIntoRegion(CSVRecord record) {
        final Region region = new Region();
        region.setId(Integer.parseInt(record.get(0)));
        region.setCountryId(Integer.parseInt(record.get(1)));
        region.setCityId(Integer.parseInt(record.get(2)));
        region.setName(record.get(3));
        return region;
    }

    private void getRepository() {
        try {
            final CSVParser csvParser = fileWorker.getCsvParser();
            for (CSVRecord record : csvParser.getRecords()) {
                listRepository.add(setRecordIntoRegion(record));
            }
            listHeaders = fileWorker.getListHeaders();
            csvParser.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get repository " + e);
        }
    }
}