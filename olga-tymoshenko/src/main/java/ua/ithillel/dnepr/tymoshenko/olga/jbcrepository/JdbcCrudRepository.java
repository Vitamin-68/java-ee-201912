package ua.ithillel.dnepr.tymoshenko.olga.jbcrepository;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.tymoshenko.olga.index.JdbcIndex;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcCrudRepository<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements IndexedCrudRepository<EntityType, IdType> {

    private final ImmutableRepository<EntityType, IdType> immutableRepository;
    private final MutableRepository<EntityType, IdType> mutableRepository;
    private final JdbcIndex jdbsIndex;

    public JdbcCrudRepository(Connection connection, Class<? extends EntityType> clazz) throws SQLException {
        immutableRepository = new JdbcImmutableRepositoryImp<>(connection, clazz);
        mutableRepository = new JdbcMutableRepositoryImp<>(connection, clazz);
        jdbsIndex = new JdbcIndex(connection, clazz);
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
        jdbsIndex.addIndex(field);
    }

    @Override
    public void addIndexes(List<String> fields) {
        jdbsIndex.addIndexes(fields);
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

