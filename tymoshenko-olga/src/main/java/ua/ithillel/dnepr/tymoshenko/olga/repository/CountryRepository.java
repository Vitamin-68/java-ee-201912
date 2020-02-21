package ua.ithillel.dnepr.tymoshenko.olga.repository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.tymoshenko.olga.entity.Country;
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
public class CountryRepository implements CrudRepository<Country, Integer> {
    private final CSVFileWorker fileWorker;
    private final List<Country> listRepository = new ArrayList<>();
    private List<String> listHeaders;


    public CountryRepository(File file, char delimiter) {
        try {
            fileWorker = new CSVFileWorker(file.getPath(), delimiter);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        getRepository();
    }

    @Override
    public Optional<List<Country>> findAll() {
        return listRepository.isEmpty() ? Optional.empty() : Optional.of(listRepository);
    }

    @Override
    public Optional<Country> findById(Integer id) {
        if (listRepository.isEmpty()) {
            return Optional.empty();
        }
        Country country = null;
        for (Country record : listRepository) {
            if (record.getId().equals(id)) {
                country = record;
            }
        }
        return country == null ? Optional.empty() : Optional.of(country);
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        Objects.requireNonNull(fieldName, "Field name is undefined");
        final List<Country> countries = new ArrayList<>();
        if (fieldName == null || fieldName.isEmpty()) throw new IllegalArgumentException("Field name is empty");
        if (listRepository.isEmpty() || listHeaders.isEmpty()) {
            return Optional.empty();
        }
        if (listHeaders.contains(fieldName)) {
            for (Country record : listRepository) {
                Field[] fields = record.getClass().getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (Field field : fields) {
                    try {
                        if (field.getName().equals(fieldName) && field.get(record).equals(value)) {
                            countries.add(record);
                        }
                    } catch (IllegalAccessException e) {
                        log.error(String.valueOf(e));
                        throw new IllegalStateException("Failed to find by field country repository " + e);
                    }
                }
            }
        }
        return countries.isEmpty() ? Optional.empty() : Optional.of(countries);
    }

    @Override
    public Country create(Country entity) {
        Objects.requireNonNull(entity, "Country entity is undefined");
        Optional<Country> country = findById(entity.getId());
        if (country.isEmpty()) {
            try {
                fileWorker.CSVWriteFile(true, false, entity);
            } catch (IOException e) {
                log.error("Failed to creat country", e);
                throw new IllegalStateException("Failed to create country repository " + e);
            }
        }
        return entity;
    }

    @Override
    public Country update(Country entity) {
        Objects.requireNonNull(entity, "Country entity is undefined");
        if (listRepository.isEmpty()) {
            return null;
        }
        Optional<Country> country = findById(entity.getId());
        if (country.isEmpty()) {
            try {
                fileWorker.CSVWriteFile(true, false, entity);
            } catch (IOException e) {
                log.error("Failed to creat country", e);
                throw new IllegalStateException("Failed to create country repository " + e);
            }
        } else {
            for (Country record : listRepository) {
                if (record.getId().equals(entity.getId())) {
                    record.setCityId(entity.getCityId());
                    record.setName(entity.getName());
                }
            }
            try {
                fileWorker.CSVWriteFile(false, true, listRepository);
            } catch (IOException e) {
                log.error("Failed to update country", e);
                throw new IllegalStateException("Failed to update country repository " + e);
            }
        }
        return entity;
    }

    @Override
    public Country delete(Integer id) {
        if (listRepository.isEmpty()) {
            return null;
        }
        Optional<Country> country = findById(id);
        if (country.isEmpty()) {
            return null;
        }
        Country delCountry = null;
        for (int i = 0; i < listRepository.size(); i++) {
            if (listRepository.get(i).getId().equals(id)) {
                delCountry = listRepository.get(i);
                listRepository.remove(i);
                break;
            }
        }
        try {
            fileWorker.CSVWriteFile(false, true, listRepository);
        } catch (IOException e) {
            log.error("Failed to delete country", e);
            throw new IllegalStateException("Failed to delete country repository " + e);
        }
        return delCountry;
    }

    private Country setRecordIntoCountry(CSVRecord record) {
        final Country country = new Country();
        country.setId(Integer.parseInt(record.get(0)));
        country.setCityId(Integer.parseInt(record.get(1)));
        country.setName(record.get(2));
        return country;
    }

    private void getRepository() {
        try {
            final CSVParser csvParser = fileWorker.getCsvParser();
            for (CSVRecord record : csvParser.getRecords()) {
                listRepository.add(setRecordIntoCountry(record));
            }
            listHeaders = fileWorker.getListHeaders();
            csvParser.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get repository " + e);
        }
    }
}


