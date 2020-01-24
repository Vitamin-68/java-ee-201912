package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class BaseJdbcRepository<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {
    protected static final String QUERY_SELECT_BY_ID = "SELECT * FROM %s WHERE id = %s";
    protected static final String QUERY_COUNT_BY_ID = "SELECT COUNT(*) FROM %s WHERE id = %s";

    protected final Connection connection;
    protected final Class<? extends EntityType> clazz;
    protected Map<String, Field> fields = new HashMap<>();
    protected final String tableName;

    protected BaseJdbcRepository(Connection connection, Class<? extends EntityType> clazz) {
        Objects.requireNonNull(connection, "Connection is undefined");
        this.connection = connection;
        this.clazz = clazz;
        this.tableName = clazz.getSimpleName();
        createTable(createEntity());
    }

    protected EntityType getEntityById(IdType id) {
        EntityType result = null;
        final String query = String.format("SELECT * FROM %s WHERE id = ?", getTableName());
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            final Field idField = getFieldByName("id");
            Objects.requireNonNull(idField, "Id field is undefined");
            if (H2TypeUtils.toH2Type(idField.getType()).equals(H2TypeUtils.H2Types.OTHER.getH2Type())) {
                statement.setObject(1, id, Types.JAVA_OBJECT);
            } else {
                statement.setObject(1, id);
            }
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = createEntity();
                mapField(result, resultSet);
            }
        } catch (SQLException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    protected void mapField(EntityType entity, ResultSet resultSet) throws SQLException, IllegalAccessException {
        final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            final String columnName = resultSetMetaData.getColumnName(i);
            final Field field = getFieldByName(columnName);
            Objects.requireNonNull(field, "Field is undefined");
            Object fieldValue = null;
            if (field.getType().isPrimitive()) {
                try {
                    final Method method = resultSet.getClass().getMethod(
                            "get" + StringUtils.capitalize(field.getType().getName()),
                            int.class);
                    fieldValue = method.invoke(resultSet, i);
                } catch (NoSuchMethodException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                if (H2TypeUtils.toH2Type(field.getType()).equals(H2TypeUtils.H2Types.OTHER.getH2Type())) {
                    fieldValue = resultSet.getObject(i);
                } else {
                    fieldValue = resultSet.getObject(i, field.getType());
                }
            }
            field.set(entity, fieldValue);
        }
    }

    protected boolean isEntityExists(EntityType entity) throws SQLException {
        final String idFieldName = "id";
        boolean result = false;
        final String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", getTableName(), idFieldName);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            Field idField = null;
            for (Field field : getAllFields()) {
                if (idFieldName.equalsIgnoreCase(field.getName())) {
                    idField = field;
                    break;
                }
            }
            Objects.requireNonNull(idField, "Id field is undefined");
            if (H2TypeUtils.toH2Type(idField.getType()).equals(H2TypeUtils.H2Types.OTHER.getH2Type())) {
                statement.setObject(1, idField.get(entity), Types.JAVA_OBJECT);
            } else {
                statement.setObject(1, idField.get(entity));
            }
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1) > 0;
            }
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        }
        return result;
    }

    protected String getTableName() {
        return this.tableName;
    }

    protected EntityType createEntity() {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Failed to create entity instance", e);
        }
    }

    protected List<Field> getAllFields() {
        if (fields.isEmpty()) {
            Class<?> iterateType = clazz;
            while (true) {
                for (Field field : iterateType.getDeclaredFields()) {
                    fields.put(field.getName(), field);
                }
                if (iterateType == BaseEntity.class || iterateType == AbstractEntity.class || iterateType == Object.class) {
                    break;
                } else {
                    iterateType = iterateType.getSuperclass();
                }
            }
            fields.forEach((name, field) -> field.setAccessible(true));
        }
        return new ArrayList<>(fields.values());
    }

    private Field getFieldByName(String fieldName) {
        Field result = null;
        for (Field field : getAllFields()) {
            if (fieldName.equalsIgnoreCase(field.getName())) {
                result = field;
                break;
            }
        }
        return result;
    }

    private void createTable(EntityType entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        final StringBuilder query = new StringBuilder();
        query.append(" CREATE TABLE IF NOT EXISTS ");
        query.append(entity.getClass().getSimpleName());
        query.append(" ( ");

        for (final Field field : getAllFields()) {
            final String h2Type = H2TypeUtils.toH2Type(field.getType());
            query.append(field.getName()).append(" ").append(h2Type).append(" , ");
        }
        query.delete(query.lastIndexOf(","), query.length());
        query.append(" ) ");
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query.toString());
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create table", e);
        }
    }
}
