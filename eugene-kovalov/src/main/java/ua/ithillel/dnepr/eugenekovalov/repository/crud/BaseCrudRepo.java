package ua.ithillel.dnepr.eugenekovalov.repository.crud;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseCrudRepo<EntityType> {
    protected final Path path;
    protected final Class<EntityType> entityTypeClass;
    protected final char delimiter;

    protected BaseCrudRepo(Path path, Class<EntityType> entityTypeClass) {
        this.path = path;
        this.entityTypeClass = entityTypeClass;
        this.delimiter = ';';
    }

    protected Optional<EntityType> createRecord(CSVRecord csvRecord){
        Optional<EntityType> result = Optional.empty();

        try {
            EntityType entity = entityTypeClass.getConstructor().newInstance();
            Field fieldId = entityTypeClass.getSuperclass().getDeclaredField("id");
            fieldId.setAccessible(true);
            fieldId.set(entity, Integer.parseInt(csvRecord.get(0)));
            Field[] fields = entityTypeClass.getDeclaredFields();
            for (Field field: fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                String fieldValue = csvRecord.get(field.getName());

                if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                    field.set(entity, Integer.parseInt(fieldValue));
                } else {
                    field.set(entity, fieldType.cast(fieldValue));
                }
            }
            result = Optional.of(entity);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchFieldException | NoSuchMethodException e) {
            log.error("Creating record error ", e);
        }
        return result;
    }

    protected Optional<List<EntityType>> getAllRecords() {
        final List<EntityType> entities = new ArrayList<>();
        Optional<List<EntityType>> result = Optional.empty();
        CSVParser csvParser = null;
        try {
            csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new FileReader(path.toFile()));
        } catch (IOException e) {
            log.error("File reading error", e);
        }
        try {
            if (csvParser != null) {
                for (CSVRecord csvRecord : csvParser.getRecords()) {
                    createRecord(csvRecord).ifPresent(entities::add);
                }
            }
        } catch (IOException e) {
            log.error("Parsing error", e);
        }
        if (!entities.isEmpty()) {
            result = Optional.of(entities);
        }
        return result;
    }

    protected List<EntityType> entitiesList(Optional<List<EntityType>> entityTypes) {
        return entityTypes.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    protected List<String> formHeaders() {
        Field[] fields = entityTypeClass.getDeclaredFields();
        List<String> headers = new ArrayList<>();
        headers.add("id");
        for (Field field: fields) {
            headers.add(field.getName());
        }
        return headers;
    }
}
