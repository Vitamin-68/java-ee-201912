package ua.ithillel.dnepr.tymoshenko.olga.repository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.tymoshenko.olga.entity.City;
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
public class CityRepository implements CrudRepository<City, Integer> {
    private final CSVFileWorker fileWorker;
    private final List<City> listRepository = new ArrayList<>();
    private List<String> listHeaders;

    public CityRepository(File file, char delimiter) {
        try {
            fileWorker = new CSVFileWorker(file.getPath(), delimiter);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        getRepository();
    }

    @Override
    public Optional<List<City>> findAll() {
        return listRepository.isEmpty() ? Optional.empty() : Optional.of(listRepository);
    }

    @Override
    public Optional<City> findById(Integer id) {
        if (listRepository.isEmpty()) {
            return Optional.empty();
        }
        City city = null;
        for (City record : listRepository) {
            if (record.getId().equals(id)) {
                city = record;
            }
        }
        return city == null ? Optional.empty() : Optional.of(city);
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) throws IllegalArgumentException {
        Objects.requireNonNull(fieldName, "Field name is undefined");
        final List<City> cities = new ArrayList<>();
        if (fieldName.isEmpty())
            throw new IllegalArgumentException("Field name is empty");
        if (listRepository.isEmpty() || listHeaders.isEmpty()) {
            return Optional.empty();
        }
        if (listHeaders.contains(fieldName)) {
            for (City record : listRepository) {
                Field[] fields = record.getClass().getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (Field field : fields) {
                    try {
                        if (field.getName().equals(fieldName) && field.get(record).equals(value)) {
                            cities.add(record);
                        }
                    } catch (IllegalAccessException e) {
                        log.error(String.valueOf(e));
                        throw new IllegalStateException("Failed to find by field in city repository " + e);
                    }
                }
            }
        }
        return cities.isEmpty() ? Optional.empty() : Optional.of(cities);
    }

    @Override
    public City create(City entity) {
        Objects.requireNonNull(entity, "City entity is undefined");
        Optional<City> city = findById(entity.getId());
        if (city.isEmpty()) {
            try {
                fileWorker.CSVWriteFile(true, false, entity);
            } catch (IOException e) {
                log.error("Failed to creat city", e);
                throw new IllegalStateException("Failed to create city repository " + e);
            }
        }
        return entity;
    }

    @Override
    public City update(City entity) {
        Objects.requireNonNull(entity, "City entity is undefined");
        if (listRepository.isEmpty()) {
            return null;
        }
        Optional<City> city = findById(entity.getId());
        if (city.isEmpty()) {
            try {
                fileWorker.CSVWriteFile(true, false, entity);
            } catch (IOException e) {
                log.error("Failed to creat city", e);
                throw new IllegalStateException("Failed to create city repository " + e);
            }
        } else {
            for (City record : listRepository) {
                if (record.getId().equals(entity.getId())) {
                    record.setRegionId(entity.getRegionId());
                    record.setCountryId(entity.getCountryId());
                    record.setName(entity.getName());
                }
            }
            try {
                fileWorker.CSVWriteFile(false, true, listRepository);
            } catch (IOException e) {
                log.error("Failed to update city", e);
                throw new IllegalStateException("Failed to update city repository " + e);
            }
        }
        return entity;
    }

    @Override
    public City delete(Integer id) {
        if (listRepository.isEmpty()) {
            return null;
        }
        Optional<City> city = findById(id);
        if (city.isEmpty()) {
            return null;
        }
        City delCity = null;
        for (int i = 0; i < listRepository.size(); i++) {
            if (listRepository.get(i).getId().equals(id)) {
                delCity = listRepository.get(i);
                listRepository.remove(i);
                break;
            }
        }
        try {
            fileWorker.CSVWriteFile(false, true, listRepository);
        } catch (IOException e) {
            log.error("Failed to delete city", e);
            throw new IllegalStateException("Failed to delete city repository " + e);

        }
        return delCity;
    }

    private City setRecordIntoCity(CSVRecord record) {
        final City city = new City();
        city.setId(Integer.parseInt(record.get(0)));
        city.setCountryId(Integer.parseInt(record.get(1)));
        city.setRegionId(Integer.parseInt(record.get(2)));
        city.setName(record.get(3));
        return city;
    }

    private void getRepository() {
        try {
            final CSVParser csvParser = fileWorker.getCsvParser();
            for (CSVRecord record : csvParser.getRecords()) {
                listRepository.add(setRecordIntoCity(record));
            }
            listHeaders = fileWorker.getListHeaders();
            csvParser.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get repository " + e);
        }
    }
}

