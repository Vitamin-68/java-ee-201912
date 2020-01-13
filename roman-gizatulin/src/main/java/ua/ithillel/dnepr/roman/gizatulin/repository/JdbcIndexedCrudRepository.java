package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class JdbcIndexedCrudRepository<EntityType extends AbstractEntity<IdType>, IdType>
        implements IndexedCrudRepository<EntityType, IdType> {

    private final Connection connection;
    private final Class<? extends EntityType> clazz;
    private final ImmutableRepository<EntityType, IdType> immutableRepository;
    private final MutableRepository<EntityType, IdType> mutableRepository;
    private final Set<String> indexes = Collections.synchronizedSet(new HashSet<>());

    public JdbcIndexedCrudRepository(Connection connection, Class<? extends EntityType> clazz) {
        this.connection = connection;
        this.clazz = clazz;
        immutableRepository = new JdbcImmutableRepositoryImp<>(this.connection, this.clazz);
        mutableRepository = new JdbcMutableRepositoryImp<>(this.connection, this.clazz);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return immutableRepository.findAll();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return immutableRepository.findById(id);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return immutableRepository.findByField(fieldName, value);
    }

    @Override
    public void addIndex(String field) {
        indexes.add(field);
    }

    @Override
    public void addIndexes(List<String> fields) {
        indexes.addAll(fields);
    }

    @Override
    public EntityType create(EntityType entity) {
        return mutableRepository.create(entity);
    }

    @Override
    public EntityType update(EntityType entity) {
        return mutableRepository.update(entity);
    }

    @Override
    public EntityType delete(IdType id) {
        return mutableRepository.delete(id);
    }
}