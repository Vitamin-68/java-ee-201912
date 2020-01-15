package ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc;

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
        return Optional.empty();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public void addIndex(String field) {

    }

    @Override
    public void addIndexes(List<String> fields) {

    }

    @Override
    public EntityType create(EntityType entity) {
        return null;
    }

    @Override
    public EntityType update(EntityType entity) {
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        return null;
    }
}
