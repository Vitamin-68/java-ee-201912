package ua.ithillel.alex.tsiba.repository.repositories.csv;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.alex.tsiba.repository.stores.DataStore;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AbstractRepository<EntityType extends AbstractEntity<Integer>> {
    protected Map<Integer, EntityType> data = new HashMap<>();
    protected DataStore dataStore;

    protected Integer currentID = 0;

    public AbstractRepository(DataStore dataStore) {
        this.dataStore = dataStore;

        EntityType item = null;
        try {
            item = (EntityType) dataStore.load();
            while (item != null) {
                Integer id = item.getId();
                if (id > currentID) {
                    currentID = id;
                }
                data.put(id, item);

                item = (EntityType) dataStore.load();
            }
        } catch (DataStoreException e) {
            log.error("Failed load information from DataStore", e);
        }
    }
}
