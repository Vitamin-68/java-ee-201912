package ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BaseJdbcRepository <EntityType extends AbstractEntity<IdType>, IdType> {
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
}
