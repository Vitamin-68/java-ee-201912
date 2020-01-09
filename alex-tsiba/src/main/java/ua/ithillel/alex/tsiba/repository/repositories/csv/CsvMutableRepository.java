package ua.ithillel.alex.tsiba.repository.repositories.csv;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.alex.tsiba.repository.entity.AbstractEntity;
import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.alex.tsiba.repository.stores.DataStore;
import ua.ithillel.dnepr.common.repository.MutableRepository;

@Slf4j
public class CsvMutableRepository<EntityType extends AbstractEntity> extends AbstractRepository<EntityType>
        implements MutableRepository<EntityType, Integer> {

    public CsvMutableRepository(DataStore dataStore) {
        super(dataStore);
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
