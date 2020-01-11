package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
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
        Optional<List<EntityType>> result;
        try (Statement statement = connection.createStatement()) {
            final List<EntityType> entities = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_SELECT_ALL, clazz.getName()));
            while (resultSet.next()) {
                EntityType entity = clazz.getDeclaredConstructor().newInstance();
                mapField(entity, resultSet);
                entities.add(entity);
            }
            result = Optional.of(entities);
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
        Optional<List<EntityType>> result;
        try (Statement statement = connection.createStatement()) {
            final List<EntityType> entities = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(String.format(
                    QUERY_SELECT_BY_FIELD,
                    clazz.getName(),
                    fieldName,
                    value));
            while (resultSet.next()) {
                EntityType entity = clazz.getDeclaredConstructor().newInstance();
                mapField(entity, resultSet);
                entities.add(entity);
            }
            result = Optional.of(entities);
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


}
