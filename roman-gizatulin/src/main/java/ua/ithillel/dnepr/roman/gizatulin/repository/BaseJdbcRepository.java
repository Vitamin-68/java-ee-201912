package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class BaseJdbcRepository<EntityType extends AbstractEntity<IdType>, IdType> {
    protected static final String QUERY_SELECT_BY_ID = "SELECT * FROM %s WHERE id = %s";
    protected static final String QUERY_COUNT_BY_ID = "SELECT COUNT(*) FROM %s WHERE id = %s";
    protected final String QUERY_CREATE_TABLE;

    protected final Connection connection;
    protected final Class<? extends EntityType> clazz;

    protected BaseJdbcRepository(Connection connection, Class<? extends EntityType> clazz) {
        Objects.requireNonNull(connection, "Connection is undefined");
        this.connection = connection;
        this.clazz = clazz;

        StringBuilder query = new StringBuilder();
        query.append(" CREATE TABLE IF NOT EXISTS ");
        query.append(this.clazz.getSimpleName());
        query.append(" ( ");
        final List<Field> declaredFields = new ArrayList<>();
        Class<?> iterateType = clazz;
        while (true) {
            declaredFields.addAll(Arrays.asList(iterateType.getDeclaredFields()));
            if (iterateType == AbstractEntity.class) {
                break;
            } else {
                iterateType = iterateType.getSuperclass();
            }
        }

        for (Field field : declaredFields) {
            query.append(field.getName()).append(" CLOB ").append(",");
        }
        query.delete(query.lastIndexOf(","), query.length());
        query.append(" ) ");
        QUERY_CREATE_TABLE = query.toString();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(QUERY_CREATE_TABLE);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create table", e);
        }
    }

    protected EntityType getEntityById(IdType id) throws
            NoSuchFieldException,
            IllegalAccessException,
            SQLException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException {
        EntityType result = null;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_SELECT_BY_ID, getTableName(), id));
            if (resultSet.next()) {
                result = clazz.getDeclaredConstructor().newInstance();
                mapField(result, resultSet);
            }
        }
        return result;
    }

    protected void mapField(EntityType entityType, ResultSet resultSet) throws SQLException, NoSuchFieldException, IllegalAccessException {
        final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            String columnName = resultSetMetaData.getColumnName(i);
            Field field = clazz.getDeclaredField(columnName);
            field.setAccessible(true);
            Object fieldValue = resultSet.getObject(i, field.getType());
            field.set(entityType, fieldValue);
        }
    }

    protected Map<String, Object> getEntityKeyValue(EntityType entity) throws IllegalAccessException {
        Map<String, Object> result = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            result.put(field.getName(), field.get(entity));
        }
        return result;
    }

    protected boolean isEntityExists(EntityType entity) throws SQLException {
        boolean result = false;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_COUNT_BY_ID, getTableName(), entity.getId()));
            while (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    result = true;
                }
            }
        }
        return result;
    }

    protected String getTableName() {
        return clazz.getSimpleName().toLowerCase();
    }
}
