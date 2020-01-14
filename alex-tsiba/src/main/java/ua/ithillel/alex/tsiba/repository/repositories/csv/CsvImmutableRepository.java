package ua.ithillel.alex.tsiba.repository.repositories.csv;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.alex.tsiba.repository.entity.AbstractEntity;
import ua.ithillel.alex.tsiba.repository.stores.DataStore;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CsvImmutableRepository<EntityType extends AbstractEntity> extends AbstractRepository<EntityType>
        implements ImmutableRepository<EntityType, Integer> {
    public CsvImmutableRepository(DataStore dataStore) {
        super(dataStore);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        List result = new ArrayList();

        for (EntityType item : data.values()) {
            result.add(item);
        }

        return Optional.of(result);
    }

    @Override
    public Optional<EntityType> findById(Integer id) {
        Optional result = Optional.empty();
        if (data.containsKey(id)) {
            result = Optional.of(data.get(id));
        }
        return result;
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        if (!dataStore.fieldExist(fieldName)) {
            throw new IllegalArgumentException("Field doesn't exist in object");
        }
        List result = new ArrayList();
        for (EntityType item : data.values()) {
            try {
                Field field = item.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                if (value.equals(field.get(item))) {
                    result.add(item);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("Field not found or not read!", e);
            }
        }
        return Optional.of(result);
    }
}
