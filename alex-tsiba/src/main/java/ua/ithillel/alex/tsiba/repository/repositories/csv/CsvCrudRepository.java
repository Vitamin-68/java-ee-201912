package ua.ithillel.alex.tsiba.repository.repositories.csv;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.alex.tsiba.repository.entity.AbstractEntity;
import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.alex.tsiba.repository.stores.DataStore;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class CsvCrudRepository<EntityType extends AbstractEntity> extends AbstractRepository<EntityType>
        implements ua.ithillel.dnepr.common.repository.CrudRepository<EntityType, Integer> {


    public CsvCrudRepository(DataStore dataStore) {
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

    @Override
    public EntityType create(EntityType entity) {
        EntityType result = null;
        Integer id = entity.getId();
        if (id != 0) {
            throw new IllegalArgumentException("Object isn't new");
        } else {
            entity.setId(currentID++);
            data.put(entity.getId(), entity);
            try {
                dataStore.save(data.values());
                result = entity;
            } catch (DataStoreException e) {
                log.error("Failed save object", e);
            }
        }

        return result;
    }

    @Override
    public EntityType update(EntityType entity) {
        EntityType result = null;
        Integer id = entity.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Object is new");
        }
        data.put(entity.getId(), entity);
        try {
            dataStore.save(data.values());
            result = entity;
        } catch (DataStoreException e) {
            log.error("Failed save object", e);
        }

        return result;
    }

    @Override
    public EntityType delete(Integer id) {
        EntityType result = null;
        if (!data.containsKey(id)) {
            throw new IllegalStateException("Object not found");
        }
        EntityType entity = data.remove(id);
        try {
            dataStore.save(data.values());
            result = entity;
        } catch (DataStoreException e) {
            log.error("Failed save object", e);
        }
        return result;
    }
}
