package ua.ithillel.alex.tsiba.repository.repositories.jdbc;

import ua.ithillel.alex.tsiba.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class JdbcImmutableRepository<EntityType extends AbstractEntity> extends AbstractJdbcRepository<EntityType>
        implements ImmutableRepository<EntityType, Integer> {

    public JdbcImmutableRepository(Connection connection, Class<EntityType> objectClass) {
        super(connection, objectClass);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional<EntityType> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }
}
