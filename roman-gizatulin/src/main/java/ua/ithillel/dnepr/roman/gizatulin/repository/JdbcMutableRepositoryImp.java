package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class JdbcMutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseJdbcRepository<EntityType, IdType>
        implements MutableRepository<EntityType, IdType> {

    public JdbcMutableRepositoryImp(Connection connection, Class<? extends EntityType> clazz) {
        super(connection, clazz);
    }

    @Override
    public EntityType create(EntityType entity) {
        try {
            if (isEntityExists(entity)) {
                throw new IllegalArgumentException("Entity already exists");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to check entity existing");
        }
        return createOrUpdateEntity(entity);
    }

    @Override
    public EntityType update(EntityType entity) {
        try {
            if (!isEntityExists(entity)) {
                throw new IllegalArgumentException("Entity doesn't exist");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to check entity existing");
        }
        return createOrUpdateEntity(entity);
    }

    @Override
    public EntityType delete(IdType id) {
        EntityType result;
        try {
            result = getEntityById(id);
        } catch (NoSuchFieldException |
                IllegalAccessException |
                SQLException |
                NoSuchMethodException |
                InvocationTargetException |
                InstantiationException e) {
            throw new IllegalStateException("Failed to get entity by id", e);
        }
        try (Statement statement = connection.createStatement()) {
            String query = " DELETE FROM " + clazz.getName() + " WHERE id = " + id;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete entity", e);
        }
        return result;
    }

    private EntityType createOrUpdateEntity(EntityType entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        Objects.requireNonNull(entity.getId(), "Entity id is undefined");
        boolean isEntityExists;
        try {
            isEntityExists = isEntityExists(entity);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to check entity existing");
        }
        Map<String, Object> entityKeyValue;
        try {
            entityKeyValue = getEntityKeyValue(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to get entity keys and values");
        }
        StringBuilder query = new StringBuilder();
        if (isEntityExists) {
            query.append(" UPDATE ");
            query.append(clazz.getName());
            query.append(" SET ");
            for (Map.Entry<String, Object> objectsEntry : entityKeyValue.entrySet()) {
                query.append(objectsEntry).append(" = ").append(objectsEntry.getValue()).append(" , ");
            }
            query.append(" WHERE id = ").append(entity.getId());
        } else {
            query.append(" INSERT INTO ").append(clazz.getName());
            StringBuilder keys = new StringBuilder();
            StringBuilder values = new StringBuilder();
            keys.append(" ( ");
            values.append(" ( ");
            for (Map.Entry<String, Object> objectsEntry : entityKeyValue.entrySet()) {
                keys.append(objectsEntry.getKey()).append(" , ");
                values.append(objectsEntry.getValue()).append(" , ");
            }
            keys.append(" ) ");
            values.append(" ) ");
            query.append(keys).append(" ").append(values);
        }
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query.toString());
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create entity", e);
        }
        return entity;
    }
}
