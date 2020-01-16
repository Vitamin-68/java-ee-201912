package ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc;

import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcImmutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseJdbcRepository<EntityType, IdType>
        implements ImmutableRepository<EntityType, IdType> {
        private static final String QUERY_SELECT_ALL = "SELECT * FROM %s";
        private static final String QUERY_SELECT_BY_FIELD = "SELECT * FROM %s WHERE %s = %s";

        public JdbcImmutableRepositoryImp(Connection connection, Class<? extends EntityType> clazz) {
                super(connection, clazz);
        }

    @Override
    public Optional<List<EntityType>> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        Optional<EntityType> result;
        try {
            result = Optional.ofNullable(getEntityById(id));
        } catch (SQLException |
                NoSuchMethodException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchFieldException e) {
            throw new IllegalStateException("Failed to create entity", e);
        }
        return result;
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }
}
