package ua.ithillel.alex.tsiba.repository.stores;

import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.util.Collection;

public interface DataStore<T extends BaseEntity> {
    void save(Collection<T> objList) throws DataStoreException;

    T load() throws DataStoreException;

    boolean fieldExist(String name);
}
