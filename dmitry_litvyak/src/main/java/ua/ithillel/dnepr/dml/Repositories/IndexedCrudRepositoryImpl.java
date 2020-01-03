package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

@Slf4j

public class IndexedCrudRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType> implements IndexedCrudRepository {

    @Override
    public Optional<List<EntityType>> findAll() {
        Optional<List<EntityType>> result = Optional.empty();

        return result;
    }

    @Override
    public Optional findById(Object id) {
        return Optional.empty();
    }

    @Override
    public Optional<List> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public void addIndex(String field) {

    }

    @Override
    public void addIndexes(List<String> fields) {

    }

    @Override
    public BaseEntity create(BaseEntity entity) {
        return null;
    }

    @Override
    public BaseEntity update(BaseEntity entity) {
        return null;
    }

    @Override
    public BaseEntity delete(Object id) {
        return null;
    }
}
