package ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;
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

public class BaseCqrsRepository<EntityType extends AbstractEntity<IdType>, IdType> {
    protected static final String QUERY_SELECT_BY_ID = "SELECT * FROM %s WHERE id = %s";
    protected static final String QUERY_COUNT_BY_ID = "SELECT COUNT(*) FROM %s WHERE id = %s";
    protected static final String SQL_INSERT = "insert";
    protected static final String SQL_UPDATE = "update";
    protected static final String SQL_DELETE = "delete";
    protected final String QUERY_CREATE_TABLE;
    protected final String QUERY_CREATE_LOG_TABLE = String.valueOf("CREATE TABLE IF NOT EXISTS logs (entityName TEXT, timestamp TEXT, fieldName TEXT, oldValue TEXT, newValue TEXT)");
    protected final Connection connection;
    protected final Class<? extends EntityType> clazz;

    protected BaseCqrsRepository(Connection connection, Class<? extends EntityType> clazz) {
        Objects.requireNonNull(connection, "Connection is undefined");
        this.connection = connection;
        this.clazz = clazz;
        QUERY_CREATE_TABLE = getSQLCreateString();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(QUERY_CREATE_TABLE);
            statement.executeUpdate(QUERY_CREATE_LOG_TABLE);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create table", e);
        }

        createTrigger(SQL_INSERT);
        createTrigger(SQL_DELETE);
        createTrigger(SQL_UPDATE);
    }

    private void createTrigger(String command){
        try (Statement statement = connection.createStatement()) {
            List<Field> fields = getAllEntityFields();
            for (Field field : fields) {
                statement.executeUpdate(getTriggerString(command, field.getName()));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("createTriggers error", e);
        }
    }

    private String getTriggerString(String command, String fieldName) {
        StringBuilder query = new StringBuilder();
        query.append(" CREATE TRIGGER IF NOT EXISTS ");
        query.append(command.concat("_").concat(fieldName));
        query.append(" BEFORE ");
        query.append(command);
        query.append(" ON ");
        query.append(getTableName());
        query.append(" BEGIN INSERT INTO logs (entityName, timestamp, fieldName, oldValue, newValue) VALUES ('");
        query.append(getTableName());
        query.append("',datetime('now'), '");
        query.append(fieldName.concat("',"));
        if (command.equals(SQL_INSERT)) {
            query.append(" null ");
        } else {
            query.append(" OLD.".concat(fieldName));
        }
        if (command.equals(SQL_DELETE)) {
            query.append(", null ");
        } else{
            query.append(", NEW.".concat(fieldName));
        }
        query.append("); END");
        return query.toString();
    }

    protected void mapField(EntityType entityType, ResultSet resultSet) throws SQLException, NoSuchFieldException, IllegalAccessException {
        final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        Map<String, Object> entityFieldsMap = getEntityKeyValue(entityType);
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            String columnName = resultSetMetaData.getColumnName(i);
            Field field;
            if(entityFieldsMap.containsKey(columnName)){
                field = clazz.getDeclaredField(columnName);
            }else{
                field = clazz.getSuperclass().getDeclaredField(columnName);
            }
            field.setAccessible(true);
            Object fieldValue = resultSet.getObject(i);
            field.set(entityType, fieldValue);
        }
    }

    protected Map<String, Object> getEntityKeyValue(EntityType entity) throws IllegalAccessException {
        Map<String, Object> result = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields){
            field.setAccessible(true);
            result.put(field.getName(), field.get(entity));
        }
        return result;
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

    protected List<Field> getAllEntityFields(){
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    private String getSQLCreateString(){
        StringBuilder query = new StringBuilder();
        query.append(" CREATE TABLE IF NOT EXISTS ");
        query.append(getTableName());
        query.append(" ( ");
        List<Field> fields = getAllEntityFields();

        for (Field field : fields) {
            query.append(field.getName()).append(" ").append(H2TypeUtils.toH2Type(field.getType()));
            if(field.getName().equals("id")){
                query.append(" primary key ");
            }
            query.append(" ,");
        }
        query.delete(query.lastIndexOf(","), query.length());
        query.append(" ) ");
        return query.toString();
    }
}
