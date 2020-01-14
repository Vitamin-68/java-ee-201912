package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class JdbcMutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        extends BaseJdbcRepository<EntityType, IdType>
        implements MutableRepository<EntityType, IdType> {
//    private final Map

    public JdbcMutableRepositoryImp(Connection connection, Class<? extends EntityType> clazz) {
        super(connection, clazz);
        //getEntityById()
    }

    @Override
    public EntityType create(EntityType entity) {
        try {
            if (isEntityExists(entity)) {
                throw new IllegalArgumentException("Entity already exists");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to check entity existing", e);
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
        result = getEntityById(id);
        try (Statement statement = connection.createStatement()) {
            String query = " DELETE FROM " + getTableName() + " WHERE id = " + id;
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
        final List<Field> entityFields = getAllFields();
        final StringBuilder query = new StringBuilder();
        if (isEntityExists) {
            query.append(" UPDATE ");
            query.append(getTableName());
            query.append(" SET ");
            for (Field objectsEntry : entityFields) {
                query.append(objectsEntry).append(" = ").append(objectsEntry.getName()).append(" , ");
            }
            query.delete(query.lastIndexOf(","), query.length());
            query.append(" WHERE id = ? ");
        } else {
            query.append(" INSERT INTO ").append(getTableName());
            final StringBuilder keys = new StringBuilder();
            final StringBuilder values = new StringBuilder();
            keys.append(" ( ");
            values.append(" ( ");
            for (Field objectsEntry : entityFields) {
                keys.append(objectsEntry.getName()).append(" , ");
                values.append(" ? ").append(" , ");
            }
            keys.delete(keys.lastIndexOf(","), keys.length());
            values.delete(values.lastIndexOf(","), values.length());
            keys.append(" ) ");
            values.append(" ) ");
            query.append(keys).append(" ").append(" VALUES ").append(values);
        }
        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            if (isEntityExists) {
                statement.setObject(1, entity.getId());
            } else {
                for (int i = 0; i < entityFields.size(); i++) {
                    final Field field = entityFields.get(i);
                    if (H2TypeUtils.toH2Type(field.getType()).equals(H2TypeUtils.H2Types.OTHER.getH2Type())) {
                        statement.setObject(i + 1, field.get(entity), Types.JAVA_OBJECT);
                    } else {
                        statement.setObject(i + 1, field.get(entity));
                    }
                }
            }
            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to create entity", e);
        }
        return entity;
    }
}
